package com.alibaba.dubbo.agent.remoting;

import com.alibaba.dubbo.agent.common.URL;
import io.netty.channel.Channel;

import java.io.Serializable;
import java.net.InetSocketAddress;

public interface Endpoint extends Serializable{
    /** 获取URL */
    URL getUrl();

    /** 获取本地地址 */
    InetSocketAddress getLocalAddress();

    /** 关闭 */
    void close();

    /** 是否关闭 */
    boolean isClosed();

    int getTimeout();

    void addChannel(Channel channel);

    void removeIfDisconnect(Channel channel);
}
