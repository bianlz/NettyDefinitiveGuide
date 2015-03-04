package com.bianlz.ndg.p7.serializable;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SubRespClientHandler extends ChannelHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		for(int i=0;i<10;i++){
			ctx.write(req());
		}
		ctx.flush();
	}
	
	public SubscribeReq req(){
		SubscribeReq req = new SubscribeReq();
		req.setAddress("地球中国");
		req.setPhoneNumber("135XXXXXXXX");
		req.setProductName("Netty权威指南");
		req.setUserName("client");
		req.setSubReqID((int)(Math.random()*10000));
		return req;
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("client recieve response:"+((SubScribeResp)msg).toString());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		System.err.println("client has occured some exceptions!");
		cause.printStackTrace();
		ctx.close();
	}

}
