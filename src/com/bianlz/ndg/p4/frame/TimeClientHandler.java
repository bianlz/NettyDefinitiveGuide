package com.bianlz.ndg.p4.frame;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeClientHandler extends ChannelHandlerAdapter {
	private byte[] req ;
	private int counter=0;
	public TimeClientHandler(){
		req = ("query time"+System.getProperty("line.separator")).getBytes();
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ByteBuf message = null;
		for(int i=0;i<100;i++){
			message = Unpooled.buffer(req.length);
			message.writeBytes(req);
			ctx.writeAndFlush(message);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub
		String time = (String)msg;
		System.out.println("now :"+time +" ,count:"+(++counter));
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		System.err.println("client occured error!");
		cause.printStackTrace();
		ctx.close();
		
	}

}
