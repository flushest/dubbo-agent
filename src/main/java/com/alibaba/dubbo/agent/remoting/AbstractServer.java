package com.alibaba.dubbo.agent.remoting;

import com.alibaba.dubbo.agent.common.URL;

import java.net.InetSocketAddress;

public abstract class AbstractServer extends AbstractEndpoint implements Server {

    private InetSocketAddress bindAddress;

    public AbstractServer(URL url) {
        super(url);
        bindAddress = new InetSocketAddress(url.getHost(), url.getPort());
    }


    public InetSocketAddress getBindAddress() {
        return bindAddress;
    }
}
