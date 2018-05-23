package com.alibaba.dubbo.agent.protocol.exchange;

import com.alibaba.dubbo.agent.common.util.Bytes;
import com.alibaba.dubbo.agent.protocol.AbstractMessageCodec;
import com.alibaba.dubbo.agent.protocol.buffer.ByteBuf;
import com.alibaba.dubbo.agent.protocol.buffer.ByteBufOutputStream;
import com.alibaba.dubbo.agent.protocol.model.Request;
import com.alibaba.dubbo.agent.protocol.model.Response;
import com.alibaba.dubbo.agent.serialize.ObjectOutput;
import com.alibaba.dubbo.agent.serialize.Serialization;
import com.alibaba.dubbo.agent.serialize.support.SerializationFactory;

import java.io.IOException;

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
    protected void encodeBody(Object msg, ByteBuf byteBuf)  throws IOException {

        if (msg instanceof Request) {
            encodeRequest(buffer, (Request) msg);
        } else if (msg instanceof Response) {
            encodeResponse(channel, buffer, (Response) msg);
        } else {
            super.encode(channel, buffer, msg);
        }
        Serialization serialization = SerializationFactory.serialization();
        ObjectOutput objectOutput = serialization.serialize(new ByteBufOutputStream(byteBuf));

        if(obj instanceof Request) {
            Request request = (Request) obj;

            if(request.isEvent()) {

            } else {

            }

            int headLow = serialization.getContentTypeId()&SERIALIZATION_MASK;
            headLow += FLAG_REQUEST;
            if(request.isTwoWay()) {
                headLow += FLAG_TWOWAY;
            }
            if(request.isEvent()) {
                headLow += FLAG_EVENT;
            }
            byteBuf.writeShort((short)headLow);
            byteBuf.writeLong(request.getId());


        }
    }

    //编码请求数据对象
    protected void encodeRequestData(ObjectOutput out, Object data) throws IOException {
        out.writeObject(data);
    }

    protected void encodeResponseData(ObjectOutput out, Object data) throws IOException {
        out.writeObject(data);
    }

    @Override
    protected Object decodeBody(ByteBuf byteBuf)  throws IOException {
        return null;
    }
}
