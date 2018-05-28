package com.alibaba.dubbo.agent.remoting;

import com.alibaba.dubbo.agent.common.RemotingException;

import java.net.InetSocketAddress;

public interface Client extends Endpoint {
    /** 发送消息 */
    void send(Object message) throws RemotingException;

    /** 获取远程地址 */
    InetSocketAddress getRemoteAddress();

    /** 是否连接上 */
    boolean isConnected();
}
