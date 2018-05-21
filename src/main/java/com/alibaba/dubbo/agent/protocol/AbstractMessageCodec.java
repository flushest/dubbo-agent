package com.alibaba.dubbo.agent.protocol;

import com.alibaba.dubbo.agent.protocol.buffer.ByteBuf;
import com.alibaba.dubbo.agent.protocol.model.RpcInvocation;
import com.alibaba.dubbo.agent.protocol.model.RpcResult;

import java.util.List;

/**
 * Created by Administrator on 2018/5/19 0019.
 */
public abstract class AbstractMessageCodec implements MessageCodec {
    @Override
    public void encode(Object message, ByteBuf byteBuf) {
        //编码
        if(message instanceof RpcInvocation) {

        }
    }

    protected abstract void encodeRequestBody(RpcInvocation invocation, ByteBuf byteBuf);

    protected abstract void encodeResponseBody(RpcResult result, ByteBuf byteBuf);


    @Override
    public void decode(List<Object> messages, ByteBuf byteBuf) {
        while(byteBuf.hasMore()) {
            if(isRequestForDecode(byteBuf)) {
                messages.add(decodeRequestBody(byteBuf));
            }else {
                messages.add(decodeResponseBody(byteBuf));
            }
        }
    }

    protected abstract boolean isRequestForDecode(ByteBuf byteBuf);

    protected abstract RpcInvocation decodeRequestBody(ByteBuf byteBuf);

    protected abstract RpcResult decodeResponseBody(ByteBuf byteBuf);
}
