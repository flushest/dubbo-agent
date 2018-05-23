package com.alibaba.dubbo.agent.serialize.support;

import com.alibaba.dubbo.agent.serialize.Serialization;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class SerializationScanner implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Serialization) {
            Serialization serialization = (Serialization) bean;
            SerializationRegistry.registerSerialization(serialization);
        }
        return bean;
    }
}
