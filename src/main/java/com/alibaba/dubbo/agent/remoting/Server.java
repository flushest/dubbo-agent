package com.alibaba.dubbo.agent.remoting;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

import java.net.InetSocketAddress;
import java.util.Collection;

public interface Server extends Endpoint {

    /** 获取ChannelHandler */
    ChannelHandler getChannelHandler();

    /** 获取已连接Channel */
    Collection<Channel> getChannels();

    /** 根据远程地址获取Channel */
    Channel getChannel(InetSocketAddress remoteAddress);

}
