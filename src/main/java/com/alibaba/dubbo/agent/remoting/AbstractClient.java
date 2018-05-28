package com.alibaba.dubbo.agent.remoting;

import com.alibaba.dubbo.agent.common.URL;

import java.net.InetSocketAddress;

public abstract class AbstractClient extends AbstractEndpoint implements Client {

    private InetSocketAddress remoteAddress;

    public AbstractClient(URL url) {
        super(url);
        remoteAddress = new InetSocketAddress(url.getHost(), url.getPort());
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }
}
