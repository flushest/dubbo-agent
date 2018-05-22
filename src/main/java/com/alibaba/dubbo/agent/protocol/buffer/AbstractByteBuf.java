package com.alibaba.dubbo.agent.protocol.buffer;

import com.alibaba.dubbo.agent.common.util.Bytes;

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

    public int writeInt(int d) {
        return write(Bytes.int2bytes(d));
    }

    public int writeLong(long d) {
        return write(Bytes.long2bytes(d));
    }

    public int writeBuf(ByteBuf d) {
        return write(d.readAll());
    }


    @Override
    public int readInt() {
        byte[] bs = read(4);
        return Bytes.bytes2int(bs);
    }

    @Override
    public long readLong() {
        byte[] bs = read(8);
        return Bytes.bytes2long(bs);
    }

    //获取缓存区长度
    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean hasMore() {
        return readerIndex < writerIndex;
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
