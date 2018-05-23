package com.alibaba.dubbo.agent.protocol.buffer;

/**
 * thread unsafe
 */
public class SimpleByteBuf extends AbstractByteBuf {

    public SimpleByteBuf() {
    }

    public SimpleByteBuf(int initLen) {
        super(initLen);
    }

    @Override
    public int write(byte[] bytes) {
        if(writerIndex + bytes.length > capacity) {//自动扩展长度
            expandCapacity(writerIndex + bytes.length - capacity);
        }
        for(byte b : bytes) {
            this.bytes[writerIndex++] = b;
        }
        return bytes.length;
    }



    @Override
    public byte[] read(int length) {
        checkReadableBytes(length);

        byte[] readBytes = new byte[length];
        for(int i=0; i<length; i++) {
            readBytes[i] = bytes[readerIndex++];
        }
        return readBytes;
    }

    @Override
    public byte[] readAll() {
        int length = writerIndex - readerIndex;
        return read(length);
    }

    //扩展缓冲区长度
    protected byte[] expandCapacity(int leastLen) {
        int newCapacity = capacity + leastLen;
        byte[] newBytes = new byte[newCapacity];
        for(int i=0; i< capacity; i++) {
            newBytes[i] = bytes[i];
        }
        bytes = newBytes;
        return newBytes;
    }
}
