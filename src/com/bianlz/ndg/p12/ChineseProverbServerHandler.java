package com.bianlz.ndg.p12;

import java.util.concurrent.ThreadLocalRandom;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class ChineseProverbServerHandler extends
		SimpleChannelInboundHandler<DatagramPacket> {
	private static final String[] DIC = {"良禽择木而栖","飞鸟尽良弓藏","燕雀安知鸿鹄之志","志不强者智不达,言不信者行不果"};
	private String nextQuote(){
		return DIC[ThreadLocalRandom.current().nextInt(DIC.length)];
	}
	@Override
	protected void messageReceived(ChannelHandlerContext arg0,
			DatagramPacket arg1) throws Exception {
		// TODO Auto-generated method stub
		String req = arg1.content().toString(CharsetUtil.UTF_8);
		System.out.println("server has recieved message : "+ req);
		if("query proverb".equals(req)){
			arg0.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(nextQuote(),CharsetUtil.UTF_8),arg1.sender()));
		}
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		cause.printStackTrace();
		ctx.close();
	}
	
}
