package com.bianlz.ndg.p4.frame;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

public class TimeServerHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub
		String body = (String)msg;
		@SuppressWarnings("deprecation")
		String currentTime = body.equalsIgnoreCase("query time")?(new Date(System.currentTimeMillis()).toGMTString()+System.getProperty("line.separator")):"bad command"+System.getProperty("line.separator");
		ByteBuf buf = Unpooled.copiedBuffer(currentTime.getBytes());  
		ctx.write(buf);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		System.err.println("server occured error !");
		cause.printStackTrace();
		ctx.close();
	}

}
