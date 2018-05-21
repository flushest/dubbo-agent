package com.alibaba.dubbo.agent.protocol.dubbo;

/**
 * dubbo协议报文解析
 */
public class DubboMessage {
    private boolean reqOrRes;//消息类型：true-request false-response
    private boolean twoWay;//是否需要应答 请求时有效


}