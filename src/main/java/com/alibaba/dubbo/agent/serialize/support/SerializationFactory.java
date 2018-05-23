package com.alibaba.dubbo.agent.serialize.support;

import com.alibaba.dubbo.agent.common.util.ConfigUtils;
import com.alibaba.dubbo.agent.serialize.Serialization;

public class SerializationFactory {
    public static String CONFIG_APPLICATION_SERIALIZATION_KEY = "dubbo.application.serialization";
    public static String DEFAULT_APPLICATION_SERIALIZATION = "fastjson";

    public static Serialization serialization() {
        String serializationName = ConfigUtils.get(CONFIG_APPLICATION_SERIALIZATION_KEY);
        if(serializationName == null) {
            serializationName = DEFAULT_APPLICATION_SERIALIZATION;
        }
        Serialization serialization = SerializationRegistry.getSerialization(serializationName);
        if (serialization == null) {
            throw new IllegalStateException("无法找到序列化方式:" + serializationName);
        }
        return serialization;
    }
}
