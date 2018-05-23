package com.alibaba.dubbo.agent.protocol;

import com.alibaba.dubbo.agent.protocol.buffer.ByteBuf;
import com.alibaba.dubbo.agent.protocol.model.RpcInvocation;
import com.alibaba.dubbo.agent.protocol.model.RpcResult;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2018/5/19 0019.
 */
public abstract class AbstractMessageCodec implements MessageCodec {
    @Override
    public void encode(Object message, ByteBuf byteBuf)  throws IOException {
        encodeBody(message, byteBuf);
    }

    protected abstract void encodeBody(Object obj, ByteBuf byteBuf) throws IOException;

    @Override
    public void decode(List<Object> messages, ByteBuf byteBuf)  throws IOException {
        while(byteBuf.readable()) {
            messages.add(decodeBody(byteBuf));
        }
    }

    protected abstract Object decodeBody(ByteBuf byteBuf)  throws IOException;

}
