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
    public int writeByte(byte b) {
        return write(new byte[]{b});
    }

    @Override
    public int writeBytes(byte[] src, int index, int length) {
        byte[] needWriteBytes = new byte[length];
        for(int i=0; i<length; i++) {
            needWriteBytes[i] = src[index+i];
        }
        return write(needWriteBytes);
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

    public byte readByte() {
        return read(1)[0];
    }

    public void readBytes(byte[] dst, int dstIndex, int length) {
        if(dst.length < dstIndex + length) {
            throw new IndexOutOfBoundsException();
        }
        byte[] readBytes = read(length);
        for(int i=0; i<length; i++) {
            dst[dstIndex+i] = readBytes[i];
        }
    }

    //获取可用字节数
    @Override
    public int readableBytes() {
        return writerIndex - readerIndex;
    }

    @Override
    public boolean readable() {
        return readableBytes() > 0;
    }

    @Override
    public int readerIndex() {
        return readerIndex;
    }

    @Override
    public int writerIndex() {
        return writerIndex;
    }

    public void skipBytes(int length) {
        read(length);
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

    protected void checkReadableBytes(int minimumReadableBytes) {
        if (readableBytes() < minimumReadableBytes) {
            throw new IndexOutOfBoundsException();
        }
    }



}
