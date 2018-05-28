package com.alibaba.dubbo.agent.common.spring;

import com.alibaba.dubbo.agent.protocol.CodecFactory;
import com.alibaba.dubbo.agent.protocol.MessageCodec;
import com.alibaba.dubbo.agent.serialize.Serialization;
import com.alibaba.dubbo.agent.serialize.support.SerializationRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class ComponentScanner implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Serialization) {
            Serialization serialization = (Serialization) bean;
            SerializationRegistry.registerSerialization(serialization);
        }

        if(bean instanceof MessageCodec) {
            CodecFactory.register((MessageCodec) bean);
        }
        return bean;
    }
}
