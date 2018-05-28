package com.alibaba.dubbo.agent.remoting;

import com.alibaba.dubbo.agent.common.URL;
import com.alibaba.dubbo.agent.common.util.NetUtils;

import java.net.InetSocketAddress;

public abstract class AbstractEndpoint implements Endpoint {
    private URL url;
    private InetSocketAddress localAddress;

    public AbstractEndpoint(URL url) {
        this.url = url;
        localAddress = new InetSocketAddress(NetUtils.getLocalHost(), 0);
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }
}
