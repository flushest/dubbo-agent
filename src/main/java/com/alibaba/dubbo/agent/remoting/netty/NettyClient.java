package com.alibaba.dubbo.agent.remoting.netty;

import com.alibaba.dubbo.agent.common.RemotingException;
import com.alibaba.dubbo.agent.common.URL;
import com.alibaba.dubbo.agent.remoting.AbstractClient;
import io.netty.channel.ChannelHandler;

public class NettyClient extends AbstractClient {

    private ChannelHandler handler;

    public NettyClient(URL url, ChannelHandler handler) {
        super(url);
        this.handler = handler;
    }

    @Override
    public void send(Object message) throws RemotingException {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }
}
