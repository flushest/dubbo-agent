package com.alibaba.dubbo.agent.protocol.buffer;

/**
 * 字节缓冲区
 */
public interface ByteBuf {
    //写缓冲区
    int write(byte[] bytes);

    //读缓存区
    byte[] read(int length);

    //读取整个缓冲区
    byte[] readAll();

    //获取可用字节数
    int getCapacity();

    //重置写位置
    void resetWriterIndex();

    //重置读位置
    void resetReaderIndex();

    //重置读写位置
    void reset();
}
