package com.alibaba.dubbo.agent.protocol;

import com.alibaba.dubbo.agent.protocol.buffer.ByteBuf;
import com.alibaba.dubbo.agent.protocol.model.RpcInvocation;
import com.alibaba.dubbo.agent.protocol.model.RpcResult;
import com.alibaba.dubbo.agent.serialize.Serialization;
import com.alibaba.dubbo.agent.serialize.support.SerializationFactory;

import java.io.IOException;
import java.util.List;

/**
 * 待扩展
 */
public abstract class AbstractMessageCodec implements MessageCodec {
    protected Serialization getSerialization() {
        return SerializationFactory.serialization();
    }

    protected Serialization getSerialization(int id) {
        return SerializationFactory.serialization(id);
    }
}
