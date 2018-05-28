package com.alibaba.dubbo.agent.remoting.netty;

import com.alibaba.dubbo.agent.common.RemotingException;
import com.alibaba.dubbo.agent.common.URL;
import com.alibaba.dubbo.agent.remoting.Client;
import com.alibaba.dubbo.agent.remoting.Server;
import com.alibaba.dubbo.agent.remoting.Transporter;
import io.netty.channel.ChannelHandler;

public class NettyTransporter implements Transporter {
    @Override
    public Server bind(URL url, ChannelHandler handler) throws RemotingException {
        return new NettyServer(url, handler);
    }

    @Override
    public Client connect(URL url, ChannelHandler handler) throws RemotingException {
        return new NettyClient(url, handler);
    }
}
