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
        return serialization(serializationName);
    }

    public static Serialization serialization(int id) {
        Serialization serialization = SerializationRegistry.getSerialization(id);
        if (serialization == null) {
            throw new IllegalStateException("cannot find any serialization for id:" + id);
        }
        return serialization;
    }

    public static Serialization serialization(String name) {
        Serialization serialization = SerializationRegistry.getSerialization(name);
        if (serialization == null) {
            throw new IllegalStateException("cannot find any serialization for name:" + name);
        }
        return serialization;
    }
}
