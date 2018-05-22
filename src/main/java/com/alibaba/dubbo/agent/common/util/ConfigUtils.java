package com.alibaba.dubbo.agent.common.util;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public class ConfigUtils implements EnvironmentAware{
    private static Environment ENVIRONMENT;
    @Override
    public void setEnvironment(Environment environment) {
        ENVIRONMENT = environment;
    }

    public static String get(String key) {
        return ENVIRONMENT.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return ENVIRONMENT.getProperty(key, defaultValue);
    }

    public static <T> T get(String key, Class<T> targetType) {
        return ENVIRONMENT.getProperty(key, targetType);
    }

    public static <T> T get(String key, Class<T> targetType, T defaultValue) {
        return ENVIRONMENT.getProperty(key, targetType, defaultValue);
    }
}
