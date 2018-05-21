package com.alibaba.dubbo.agent.protocol.dubbo;

import com.alibaba.dubbo.agent.protocol.AbstractMessageCodec;
import com.alibaba.dubbo.agent.protocol.buffer.ByteBuf;
import com.alibaba.dubbo.agent.protocol.model.RpcInvocation;
import com.alibaba.dubbo.agent.protocol.model.RpcResult;

public class DubboCodec extends AbstractMessageCodec {

    @Override
    protected void encodeRequestBody(RpcInvocation invocation, ByteBuf byteBuf) {

    }

    @Override
    protected void encodeResponseBody(RpcResult result, ByteBuf byteBuf) {

    }

    @Override
    protected boolean isRequestForDecode(ByteBuf byteBuf) {
        return false;
    }

    @Override
    protected RpcInvocation decodeRequestBody(ByteBuf byteBuf) {
        return null;
    }

    @Override
    protected RpcResult decodeResponseBody(ByteBuf byteBuf) {
        return null;
    }
}
