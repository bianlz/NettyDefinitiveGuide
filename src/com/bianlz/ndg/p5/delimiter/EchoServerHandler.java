package com.bianlz.ndg.p5.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub
		String msgStr = (String)msg;
		System.out.println(msgStr);
		ByteBuf buf = Unpooled.copiedBuffer("welcome to server$_".getBytes());
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
		System.err.println("server has occured exception !");
		cause.printStackTrace();
		ctx.close();
	}

}
