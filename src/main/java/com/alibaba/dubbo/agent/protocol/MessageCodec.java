package com.alibaba.dubbo.agent.protocol;

import com.alibaba.dubbo.agent.protocol.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

/**
 * 消息编码解码接口
 */
public interface MessageCodec {
    void encode(Object message, ByteBuf byteBuf)  throws IOException;
    void decode(List<Object> messages, ByteBuf byteBuf)  throws IOException;
}
