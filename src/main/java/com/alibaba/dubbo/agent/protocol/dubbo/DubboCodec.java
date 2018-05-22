package com.alibaba.dubbo.agent.protocol.dubbo;

import com.alibaba.dubbo.agent.common.CodecException;
import com.alibaba.dubbo.agent.protocol.AbstractMessageCodec;
import com.alibaba.dubbo.agent.protocol.buffer.ByteBuf;
import com.alibaba.dubbo.agent.protocol.model.RpcInvocation;
import com.alibaba.dubbo.agent.protocol.model.RpcResult;

public class DubboCodec extends AbstractMessageCodec {

    @Override
    protected void encodeBody(Object obj, ByteBuf byteBuf) {
        if(obj instanceof RpcInvocation) {

        }else if(obj instanceof RpcResult) {

        }else {
            throw new CodecException("不支持的编码类型:" + obj.getClass().getName());
        }
    }

    @Override
    protected Object decodeBody(ByteBuf byteBuf) {
        return null;
    }
}
