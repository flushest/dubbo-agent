package com.alibaba.dubbo.agent.protocol.dubbo;

import com.alibaba.dubbo.agent.protocol.buffer.ByteBuf;
import com.alibaba.dubbo.agent.protocol.buffer.SimpleByteBuf;
import lombok.Builder;
import lombok.Getter;

/**
 * dubbo协议报文解析
 */
@Builder
@Getter
public class DubboMessage {
    private static final int PROTOCOL_IDENTIFIER = 0xdabb;
    private boolean reqOrRes;//消息类型：true-request false-response
    private boolean twoWay;//是否需要应答 请求时有效
    private boolean event;//是否事件消息
    private int serializationId;//序列化类型
    private int status;//响应状态
    private long requestId;//唯一请求id
    private int dataLength;//数据长度
    private byte[] variablePart;//变长部分

    public void encode(ByteBuf byteBuf) {
        byteBuf.writeBuf(encode());
    }

    public ByteBuf encode() {
        ByteBuf byteBuf = new SimpleByteBuf();
        byteBuf.writeInt(combineFirstFourBytes());//前4个字节
        byteBuf.writeLong(requestId);//请求id
        byteBuf.writeInt(dataLength);//数据长度
        byteBuf.write(variablePart);//边长部分
        return byteBuf;
    }

    public DubboMessage(ByteBuf byteBuf) {
        decodeFirstFourBytes(byteBuf);
        requestId = byteBuf.readLong();
        dataLength = byteBuf.readInt();
        variablePart = byteBuf.read(dataLength);
    }

    private int combineFirstFourBytes() {
        int result = PROTOCOL_IDENTIFIER << 16;//协议标识符
        result += reqOrRes?1<<15:0;//请求或响应
        result += twoWay?1<<14:0;//是否需要响应
        result += event?1<<13:0;//是否事件类型
        result += (serializationId&0x1F)<<8;//序列化方式
        result += status&0xFF;//响应状态
        return result;
    }

    private void decodeFirstFourBytes(ByteBuf byteBuf) {
        byte[] headBytes = byteBuf.read(4);
        reqOrRes = (headBytes[3]&0x80)==1;//请求或响应
        twoWay = (headBytes[3]&0x40)==1;//是否需要响应
        event = (headBytes[3]&0x20)==1;//是否事件类型
        serializationId = (headBytes[3]&0x1F);//序列化方式
        status = headBytes[4];//响应状态
    }
}