package com.alibaba.dubbo.agent.protocol.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * rpc请求结果
 */
@Builder
public class RpcResult implements Serializable {
    @Getter
    private Throwable throwable;
    @Getter
    private Object result;
    private Map<String, String> attachments;

    public RpcResult(Throwable throwable, Object Result, Map<String, String> attachments) {
        this.throwable = throwable;
        this.result = result;
        if(attachments == null) {
            this.attachments = new HashMap<>();
        }else {
            this.attachments = attachments;
        }
    }

    public String getAttachment(String key) {
        return attachments.get(key);
    }

    public void addAttachment(String key, String value) {
        attachments.put(key, value);
    }

    public void addAttachments(Map<String, String> attachments) {
        this.attachments.putAll(attachments);
    }

    public boolean hasAttachment(String key) {
        return attachments.containsKey(key);
    }

    public void clearAttachments() {
        attachments.clear();
    }

    public boolean hasException() {
        return throwable!=null;
    }
}
