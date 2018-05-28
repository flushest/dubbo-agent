package com.alibaba.dubbo.agent.remoting;

import com.alibaba.dubbo.agent.common.RemotingException;
import com.alibaba.dubbo.agent.common.URL;
import io.netty.channel.ChannelHandler;

public interface Transporter {

    Server bind(URL url, ChannelHandler handler) throws RemotingException;

    Client connect(URL url, ChannelHandler handler) throws RemotingException;
}
