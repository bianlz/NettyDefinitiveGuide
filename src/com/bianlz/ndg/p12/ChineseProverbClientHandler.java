package com.bianlz.ndg.p12;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class ChineseProverbClientHandler extends
		SimpleChannelInboundHandler<DatagramPacket> {

	@Override
	protected void messageReceived(ChannelHandlerContext arg0,
			DatagramPacket arg1) throws Exception {
		// TODO Auto-generated method stub
		String meg = arg1.content().toString(CharsetUtil.UTF_8);
		System.out.println(meg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		cause.printStackTrace();
		ctx.close();
	}

}
