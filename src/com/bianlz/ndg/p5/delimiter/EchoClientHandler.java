package com.bianlz.ndg.p5.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoClientHandler extends ChannelHandlerAdapter {
	private final String msgStr="hello server ! $_";
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ByteBuf message = null;
		for(int i=0;i<10;i++){
			message = Unpooled.copiedBuffer(msgStr.getBytes());
			ctx.writeAndFlush(message);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub
		String req = (String)msg;
		System.out.println(req);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		System.err.println("client has occured exception!");
		cause.printStackTrace();
	}

}
