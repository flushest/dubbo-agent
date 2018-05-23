package com.alibaba.dubbo.agent.protocol.dubbo;

import com.alibaba.dubbo.agent.common.CodecException;
import com.alibaba.dubbo.agent.common.RpcException;
import com.alibaba.dubbo.agent.protocol.AbstractMessageCodec;
import com.alibaba.dubbo.agent.protocol.buffer.ByteBuf;
import com.alibaba.dubbo.agent.protocol.model.Request;
import com.alibaba.dubbo.agent.protocol.model.RpcInvocation;
import com.alibaba.dubbo.agent.protocol.model.RpcResult;

public class DubboCodec extends AbstractMessageCodec {
    private static final int PROTOCOL_IDENTIFIER = 0xdabb;

    @Override
    protected void encodeBody(Object obj, ByteBuf byteBuf) {

    }

    @Override
    protected Object decodeBody(ByteBuf byteBuf) {
        return null;
    }
}
