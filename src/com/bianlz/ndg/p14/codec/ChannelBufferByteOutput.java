package com.bianlz.ndg.p14.codec;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import org.jboss.marshalling.ByteOutput;

public class ChannelBufferByteOutput implements ByteOutput {
	
	private final ByteBuf buf;
	
	public ChannelBufferByteOutput(ByteBuf buf){
		this.buf = buf;
	}
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void flush() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(int arg0) throws IOException {
		// TODO Auto-generated method stub
		buf.writeByte(arg0);
	}

	@Override
	public void write(byte[] arg0) throws IOException {
		// TODO Auto-generated method stub
		buf.writeBytes(arg0);
	}

	@Override
	public void write(byte[] bytes, int srcIndex, int length) throws IOException {
		// TODO Auto-generated method stub
		buf.writeBytes(bytes,srcIndex,length);
	}
	
	ByteBuf getBuf(){
		return buf;
	}
}
