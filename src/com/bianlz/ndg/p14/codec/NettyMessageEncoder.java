package com.bianlz.ndg.p14.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.util.Map;

import com.bianlz.ndg.p14.message.NettyMessage;

public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {
	private MarshallingEncoder encoder;
	
	public NettyMessageEncoder()throws IOException{
		encoder = new MarshallingEncoder();
	}
	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg,
			ByteBuf buf) throws Exception {
		// TODO Auto-generated method stub
		if(msg==null||msg.getHeader()==null){
			throw new Exception("the encode message is null");
		}
		buf.writeInt(msg.getHeader().getCrcCode());
		buf.writeInt(msg.getHeader().getLength());
		buf.writeLong(msg.getHeader().getSessionId());
		buf.writeByte(msg.getHeader().getType());
		buf.writeByte(msg.getHeader().getPriority());
		buf.writeInt(msg.getHeader().getAttachment().size());
		String key = null;
		byte[] keyArray = null;
		Object value = null;
		for(Map.Entry<String, Object> param:msg.getHeader().getAttachment().entrySet()){
			key = param.getKey();
			keyArray = key.getBytes("UTF-8");
			buf.writeInt(keyArray.length);
			buf.writeBytes(keyArray);
			value = param.getValue();
			encoder.encode(value, buf);
		}
		key = null;
		keyArray = null;
		value = null;
		if(msg.getBody()!=null){
			encoder.encode(msg.getBody(),buf );
		}else{
			buf.writeInt(0);
		}
		buf.setInt(4, buf.readableBytes()-8);
	}

}
