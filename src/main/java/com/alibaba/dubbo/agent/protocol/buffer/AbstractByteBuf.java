package com.alibaba.dubbo.agent.protocol.buffer;

public abstract class AbstractByteBuf implements ByteBuf {
    protected byte[] bytes;
    protected int capacity;

    protected int writerIndex;
    protected int readerIndex;

    public AbstractByteBuf() {
        this(1024);
    }

    public AbstractByteBuf(int initLen) {
        capacity = initLen;
        bytes = new byte[capacity];
    }

    //获取缓存区长度
    @Override
    public int getCapacity() {
        return capacity;
    }

    //重置写位置
    @Override
    public void resetWriterIndex() {
        writerIndex = 0;

    }

    //重置读位置
    @Override
    public void resetReaderIndex() {
        readerIndex = 0;
    }

    //重置读写位置
    @Override
    public void reset() {
        resetWriterIndex();
        resetReaderIndex();
    }



}
