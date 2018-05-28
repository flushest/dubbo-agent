package com.alibaba.dubbo.agent.protocol;

import com.alibaba.dubbo.agent.common.util.ConfigUtils;
import com.alibaba.dubbo.agent.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CodecFactory {
    public static final String CONFIG_CODEC_TYPE_KEY = "dubbo.application.protocol";
    public static final String DEFAULT_CODEC = "dubbo";

    public static Map<String, MessageCodec> codecMap = new ConcurrentHashMap<>();

    public static MessageCodec get(String key) {
        return codecMap.get(key);
    }

    public static void register(MessageCodec codec) {
        codecMap.put(codec.name(), codec);
        log.info("registered protocol:" + codec.name() + "->" + codec.getClass().getName());
    }

    public static MessageCodec get() {
        String protocolType = ConfigUtils.get(CONFIG_CODEC_TYPE_KEY);
        if(StringUtils.isEmpty(protocolType)) {
            protocolType = DEFAULT_CODEC;
        }

        return get(protocolType);
    }
}
