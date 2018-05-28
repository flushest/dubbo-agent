package com.alibaba.dubbo.agent.protocol;

import com.alibaba.dubbo.agent.protocol.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

/**
 * 消息编码解码接口
 */
public interface MessageCodec {
    String name();
    void encode(Object message, ByteBuf byteBuf)  throws IOException;
    Object decode(ByteBuf byteBuf)  throws IOException;

    enum DecodeResult {
        NEED_MORE_INPUT, SKIP_SOME_INPUT
    }
}
