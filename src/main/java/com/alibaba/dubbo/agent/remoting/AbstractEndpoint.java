package com.alibaba.dubbo.agent.remoting;

import com.alibaba.dubbo.agent.common.Constants;
import com.alibaba.dubbo.agent.common.URL;
import com.alibaba.dubbo.agent.common.util.NetUtils;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractEndpoint implements Endpoint {
    private URL url;
    private InetSocketAddress localAddress;
    private int timeout;
    protected Map<InetSocketAddress, Channel> remoteChannelMap = new ConcurrentHashMap<>();

    public AbstractEndpoint(URL url) {
        this.url = url;
        this.localAddress = new InetSocketAddress(NetUtils.getLocalHost(), 0);
        this.timeout = url.getPositiveParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT);
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void addChannel(Channel channel) {
        InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
        remoteChannelMap.putIfAbsent(remoteAddress, channel);
    }

    @Override
    public void removeIfDisconnect(Channel channel) {
        if(channel != null && !channel.isActive()) {
            InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
            remoteChannelMap.remove(remoteAddress);
        }
    }
}
