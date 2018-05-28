package com.alibaba.dubbo.agent.protocol.exchange;

import com.alibaba.dubbo.agent.common.util.Bytes;
import com.alibaba.dubbo.agent.common.util.StringUtils;
import com.alibaba.dubbo.agent.protocol.AbstractMessageCodec;
import com.alibaba.dubbo.agent.protocol.buffer.ByteBuf;
import com.alibaba.dubbo.agent.protocol.buffer.ByteBufInputStream;
import com.alibaba.dubbo.agent.protocol.buffer.ByteBufOutputStream;
import com.alibaba.dubbo.agent.protocol.model.Request;
import com.alibaba.dubbo.agent.protocol.model.Response;
import com.alibaba.dubbo.agent.serialize.Cleanable;
import com.alibaba.dubbo.agent.serialize.ObjectInput;
import com.alibaba.dubbo.agent.serialize.ObjectOutput;
import com.alibaba.dubbo.agent.serialize.Serialization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@Component
public class ExchangeCodec extends AbstractMessageCodec {

    // header length.
    protected static final int HEADER_LENGTH = 16;
    // magic header.
    protected static final short MAGIC = (short) 0xdabb;
    protected static final byte MAGIC_HIGH = Bytes.short2bytes(MAGIC)[0];
    protected static final byte MAGIC_LOW = Bytes.short2bytes(MAGIC)[1];
    // message flag.
    protected static final byte FLAG_REQUEST = (byte) 0x80;
    protected static final byte FLAG_TWOWAY = (byte) 0x40;
    protected static final byte FLAG_EVENT = (byte) 0x20;
    protected static final int SERIALIZATION_MASK = 0x1f;

    protected short getMagic() {
        return MAGIC;
    }


    @Override
    public String name() {
        return "exchange";
    }

    @Override
    public void encode(Object message, ByteBuf byteBuf) throws IOException {
        if(message instanceof Request) {//请求
            encodeRequest((Request) message, byteBuf);
        }else if(message instanceof Response) {//响应
            encodeResponse((Response)message, byteBuf);
        }else {//作为普通消息对象
            encodeObject(message, byteBuf);
        }
    }

    protected void encodeRequest(Request request, ByteBuf byteBuf) throws IOException {
        Serialization serialization = getSerialization();
        // header.
        byte[] header = new byte[HEADER_LENGTH];
        // set magic number.
        Bytes.short2bytes(MAGIC, header);

        // set request and serialization flag.
        header[2] = (byte) (FLAG_REQUEST | serialization.getContentTypeId());

        if (request.isTwoWay()) header[2] |= FLAG_TWOWAY;
        if (request.isEvent()) header[2] |= FLAG_EVENT;

        // set request id.
        Bytes.long2bytes(request.getId(), header, 4);

        // encode request data.
        int savedWriteIndex = byteBuf.writerIndex();
        byteBuf.writerIndex(savedWriteIndex + HEADER_LENGTH);
        ByteBufOutputStream bos = new ByteBufOutputStream(byteBuf);
        ObjectOutput out = serialization.serialize(bos);
        if (request.isEvent()) {
            encodeEventData(out, request.getData());
        } else {
            encodeRequestData(out, request.getData());
        }
        out.flushBuffer();
        if (out instanceof Cleanable) {
            ((Cleanable) out).cleanup();
        }
        bos.flush();
        bos.close();
        int len = bos.writtenBytes();
        Bytes.int2bytes(len, header, 12);

        // write
        byteBuf.writerIndex(savedWriteIndex);
        byteBuf.write(header); // write header.
        byteBuf.writerIndex(savedWriteIndex + HEADER_LENGTH + len);
    }

    protected void encodeEventData(ObjectOutput out, Object data) throws IOException {
        out.writeObject(data);
    }

    protected void encodeRequestData(ObjectOutput out, Object data) throws IOException {
        out.writeObject(data);
    }

    protected void encodeResponse(Response response, ByteBuf byteBuf) throws IOException {
        int savedWriteIndex = byteBuf.writerIndex();
        try {
            Serialization serialization = getSerialization();
            // header.
            byte[] header = new byte[HEADER_LENGTH];
            // set magic number.
            Bytes.short2bytes(MAGIC, header);
            // set request and serialization flag.
            header[2] = serialization.getContentTypeId();
            if (response.isEvent()) header[2] |= FLAG_EVENT;
            // set response status.
            byte status = response.getStatus();
            header[3] = status;
            // set request id.
            Bytes.long2bytes(response.getId(), header, 4);

            byteBuf.writerIndex(savedWriteIndex + HEADER_LENGTH);
            ByteBufOutputStream bos = new ByteBufOutputStream(byteBuf);
            ObjectOutput out = serialization.serialize(bos);
            // encode response data or error message.
            if (status == Response.OK) {
                if (response.isEvent()) {
                    encodeEventData(out, response.getResult());
                } else {
                    encodeResponseData(out, response.getResult());
                }
            } else out.writeUTF(response.getErrorMsg());
            out.flushBuffer();
            if (out instanceof Cleanable) {
                ((Cleanable) out).cleanup();
            }
            bos.flush();
            bos.close();

            int len = bos.writtenBytes();
            Bytes.int2bytes(len, header, 12);
            // write
            byteBuf.writerIndex(savedWriteIndex);
            byteBuf.write(header); // write header.
            byteBuf.writerIndex(savedWriteIndex + HEADER_LENGTH + len);
        } catch (Throwable t) {
            // clear buffer
            byteBuf.writerIndex(savedWriteIndex);
            // send error message to Consumer, otherwise, Consumer will wait till timeout.
            if (!response.isEvent() && response.getStatus() != Response.BAD_RESPONSE) {
                Response.ResponseBuilder builder = Response.builder()
                        .id(response.getId())
                        .status(Response.BAD_RESPONSE);

                log.warn("Fail to encode response: " + response + ", send bad_response info instead, cause: " + t.getMessage(), t);
                builder.errorMsg("Failed to send response: " + response + ", cause: " + StringUtils.toString(t));
                encodeResponse(builder.build(), byteBuf);
                return;
            }

            // Rethrow exception
            if (t instanceof IOException) {
                throw (IOException) t;
            } else if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else if (t instanceof Error) {
                throw (Error) t;
            } else {
                throw new RuntimeException(t.getMessage(), t);
            }
        }
    }

    protected void encodeResponseData(ObjectOutput out, Object data) throws IOException {
        out.writeObject(data);
    }

    protected void encodeObject(Object message, ByteBuf byteBuf) throws IOException {
        if(message instanceof String) {
            byteBuf.write((message + "\r\n").getBytes(Charset.forName("UTF-8")));
        }else {
            ObjectOutput out = getSerialization().serialize(new ByteBufOutputStream(byteBuf));
            out.writeObject(message);
        }
    }

    @Override
    public Object decode(ByteBuf byteBuf) throws IOException {
        if(byteBuf.readableBytes() == 0) {
            return DecodeResult.NEED_MORE_INPUT;
        }
        byte[] header;
        if(HEADER_LENGTH > byteBuf.readableBytes()) {
            header = new byte[4];
        }else {
            header = byteBuf.read(HEADER_LENGTH);
        }

        return decodeBody(byteBuf, header);
    }

    protected Object decodeBody(ByteBuf byteBuf, byte[] header) throws IOException {
        if(header[0]!=MAGIC_HIGH || header[1]!=MAGIC_LOW) {//普通消息
            return decodeMessage(byteBuf);
        }

        Serialization serialization = getSerialization(header[2]&SERIALIZATION_MASK);
        ByteBufInputStream bis = new ByteBufInputStream(byteBuf);
        ObjectInput in = serialization.deserialize(bis);
        if((header[3]&FLAG_REQUEST)==1) {//请求
            return decodeRequest(in, header);
        }else {//响应
            return decodeResponse(in, header);
        }
    }

    //解析普通消息
    protected  Object decodeMessage(ByteBuf byteBuf) throws IOException {
        int savedReaderIndex = byteBuf.readerIndex();
        int length = byteBuf.readableBytes();
        byteBuf.readerIndex(savedReaderIndex + length - 2);
        byte[] end = byteBuf.readAll();
        if(end[0]=='\r' && end[1]=='\n') {//字符串消息
            byteBuf.readerIndex(savedReaderIndex);
            return new String(byteBuf.read(length-2), Charset.forName("UTF-8"));
        }else {
            Serialization serialization = getSerialization();
            ObjectInput in = serialization.deserialize(new ByteBufInputStream(byteBuf));
            try {
                return in.readObject();
            }catch (ClassNotFoundException e) {
                log.warn("fail to deserialize message", e);
                throw new IOException("fail to deserialize message");
            }
        }
    }

    protected  Object decodeRequest(ObjectInput in, byte[] header) throws IOException {
        long id = Bytes.bytes2long(Bytes.copyOf(header, 4, 8));
        boolean twoWay = (header[2]&FLAG_EVENT)==1;
        boolean event = (header[2]&FLAG_EVENT)==1;
        Object data;
        if(event) {
            data = decodeEventData(in);
        }else {
            data = decodeRequestData(in);
        }
        return Request.builder()
                .id(id)
                .twoWay(twoWay)
                .event(event)
                .data(data)
                .build();
    }

    protected Object decodeEventData(ObjectInput in) throws IOException {
        try {
            return in.readObject();
        } catch (ClassNotFoundException e) {
            log.warn("fail to deserialize message", e);
            throw new IOException("fail to deserialize message");
        }
    }

    protected Object decodeRequestData(ObjectInput in) throws IOException {
        try {
            return in.readObject();
        } catch (ClassNotFoundException e) {
            log.warn("fail to deserialize message", e);
            throw new IOException("fail to deserialize message");
        }
    }

    protected  Object decodeResponse(ObjectInput in, byte[] header) throws IOException {
        long id = Bytes.bytes2long(Bytes.copyOf(header, 4, 8));
        boolean event = (header[2]&FLAG_EVENT)==1;
        byte status = header[3];

        Object data = null;
        String errorMsg = null;
        if(status == Response.OK) {
            if(event) {
                data = decodeEventData(in);
            }else {
                data = decodeResponseData(in);
            }
        }else {
            errorMsg = in.readUTF();
        }

        return Response.builder()
                .event(event)
                .status(status)
                .id(id)
                .errorMsg(errorMsg)
                .result(data)
                .build();
    }

    protected Object decodeResponseData(ObjectInput in) throws IOException {
        try {
            return in.readObject();
        } catch (ClassNotFoundException e) {
            log.warn("fail to deserialize message", e);
            throw new IOException("fail to deserialize message");
        }
    }
}
