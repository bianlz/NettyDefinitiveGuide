package com.bianlz.ndg.p14.codec;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import org.jboss.marshalling.ByteInput;

public class ChannelBufferByteInput implements ByteInput {
	private ByteBuf buf;
	public ChannelBufferByteInput(ByteBuf buf){
		this.buf = buf;
	}
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public int available() throws IOException {
		// TODO Auto-generated method stub
		return buf.readableBytes();
	}

	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		if (buf.isReadable()) {
            return buf.readByte() & 0xff;
        }
        return -1;
	}

	@Override
	public int read(byte[] array) throws IOException {
		// TODO Auto-generated method stub
		return read(array, 0, array.length);
	}

	@Override
	public int read(byte[] dst, int dstIndex, int length) throws IOException {
		// TODO Auto-generated method stub
		int available = available();
        if (available == 0) {
            return -1;
        }

        length = Math.min(available, length);
        buf.readBytes(dst, dstIndex, length);
        return length;

	}

	@Override
	public long skip(long bytes) throws IOException {
		// TODO Auto-generated method stub
		int readable = buf.readableBytes();
        if (readable < bytes) {
            bytes = readable;
        }
        buf.readerIndex((int) (buf.readerIndex() + bytes));
        return bytes;

	}

}
