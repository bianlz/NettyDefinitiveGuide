package com.bianlz.ndg.p8.protoBuf;

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
	
	public SubscribeReqProto.SubscribeReq req(){
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setAddress("地球中国");
		builder.setPhoneNumber("135XXXXXXXX");
		builder.setProductName("netty权威指南");
		builder.setUserName("client");
		builder.setSubReqID((int)(Math.random()*10000));
		return builder.build();
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
		System.out.println("client recieve response:"+((SubscribeRespProto.SubscribeResp)msg).toString());
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
