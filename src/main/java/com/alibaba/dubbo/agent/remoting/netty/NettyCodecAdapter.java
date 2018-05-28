/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.agent.remoting.netty;

import com.alibaba.dubbo.agent.protocol.MessageCodec;
import com.alibaba.dubbo.agent.protocol.buffer.ByteBuf;
import com.alibaba.dubbo.agent.protocol.buffer.SimpleByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.util.List;

/**
 * NettyCodecAdapter.
 */
final class NettyCodecAdapter {

    private final ChannelHandler encoder = new InternalEncoder();

    private final ChannelHandler decoder = new InternalDecoder();

    private final MessageCodec codec;

    private final NettyServer server;

    public NettyCodecAdapter(MessageCodec codec, NettyServer server) {
        this.codec = codec;
        this.server = server;
    }

    public ChannelHandler getEncoder() {
        return encoder;
    }

    public ChannelHandler getDecoder() {
        return decoder;
    }

    private class InternalEncoder extends MessageToByteEncoder {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, io.netty.buffer.ByteBuf out) throws Exception {
            Channel channel = ctx.channel();
            server.addChannel(channel);
            try {
                ByteBuf byteBuf = new SimpleByteBuf();
                codec.encode(msg, byteBuf);
                out.writeBytes(byteBuf.readAll());
            }finally {
                server.removeIfDisconnect(channel);
            }

        }
    }

    private class InternalDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, io.netty.buffer.ByteBuf input, List<Object> out) throws Exception {

            ByteBuf message = new SimpleByteBuf();
            message.write(input.array());

            Object msg;

            int saveReaderIndex;

            Channel channel = ctx.channel();
            server.addChannel(channel);
            try {
                // decode object.
                do {
                    saveReaderIndex = message.readerIndex();
                    try {
                        msg = codec.decode(message);
                    } catch (IOException e) {
                        throw e;
                    }
                    if (msg == MessageCodec.DecodeResult.NEED_MORE_INPUT) {
                        message.readerIndex(saveReaderIndex);
                        break;
                    } else {
                        //is it possible to go here ?
                        if (saveReaderIndex == message.readerIndex()) {
                            throw new IOException("Decode without read data.");
                        }
                        if (msg != null) {
                            out.add(msg);
                        }
                    }
                } while (message.readable());
            }finally {
                server.removeIfDisconnect(channel);
            }

        }
    }
}
