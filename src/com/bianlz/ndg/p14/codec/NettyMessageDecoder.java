package com.bianlz.ndg.p14.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.bianlz.ndg.p14.message.Header;
import com.bianlz.ndg.p14.message.NettyMessage;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {
	private MarshallingDecoder decoder ;
	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset,
			int lengthFieldLength) throws IOException{
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		// TODO Auto-generated constructor stub
		decoder = new MarshallingDecoder();
	}
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in)
			throws Exception {
		// TODO Auto-generated method stub
		ByteBuf frame = (ByteBuf)super.decode(ctx, in);
		if(frame==null){
			return null;
		}
		NettyMessage msg = new NettyMessage();
		Header header = new Header();
		header.setCrcCode(frame.readInt());
		header.setLength(frame.readInt());
		header.setSessionId(frame.readLong());
		header.setType(frame.readByte());
		header.setPriority(frame.readByte());
		int size = frame.readInt();
		if(size>0){
			Map<String,Object> attach = new HashMap<String,Object>(size);
			int keySize = 0;
			byte[] keyArray = null;
			String key = null;
			for(int i=0;i<size;i++){
				keySize = frame.readInt();
				keyArray = new byte[keySize];
				frame.readBytes(keyArray);
				key = new String(keyArray,"UTF-8");
				attach.put(key, decoder.decode(frame));
			}
			keyArray = null;
			key = null;
			header.setAttachment(attach);
		}
		if(frame.readableBytes()>4){
			msg.setBody(decoder.decode(frame));
		}
		msg.setHeader(header);
		return msg;
	}
	

}
