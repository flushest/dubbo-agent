package com.alibaba.dubbo.agent.protocol.dubbo;


import com.alibaba.dubbo.agent.protocol.exchange.ExchangeCodec;
import org.springframework.stereotype.Component;

@Component
public class DubboCodec extends ExchangeCodec {
    @Override
    public String name() {
        return "dubbo";
    }
}
