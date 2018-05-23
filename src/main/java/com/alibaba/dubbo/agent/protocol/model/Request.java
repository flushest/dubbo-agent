package com.alibaba.dubbo.agent.protocol.model;

import com.alibaba.dubbo.agent.protocol.buffer.ByteBuf;
import com.alibaba.dubbo.agent.protocol.buffer.SimpleByteBuf;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 请求
 */
@Builder
@Getter
public class Request implements Serializable {
    private static final AtomicLong REQUEST_ID_SEQUENCE = new AtomicLong(0L);
    private boolean twoWay;//是否需要应答
    private boolean event;//是否事件消息
    private long id;//唯一请求id
    private Object data;//数据对象

    public static long getNextId() {
        return REQUEST_ID_SEQUENCE.getAndIncrement();
    }

    /*public void encode(ByteBuf byteBuf) {
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

    public Request(ByteBuf byteBuf) {
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
    }*/
}