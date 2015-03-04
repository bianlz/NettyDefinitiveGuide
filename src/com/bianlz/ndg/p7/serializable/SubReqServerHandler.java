package com.bianlz.ndg.p7.serializable;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SubReqServerHandler extends ChannelHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub
		SubscribeReq req = (SubscribeReq)msg;
		System.out.println("server recieve : "+req.toString());
		if("client".equalsIgnoreCase(req.getUserName())){
			ctx.writeAndFlush(resp(req.getSubReqID()));
		}
	}
	
	private SubScribeResp resp(int reqId){
		SubScribeResp resp = new SubScribeResp();
		resp.setSubReqID(reqId);
		resp.setDesc("<<Netty Definitive Guide>>是一本写关于netty的书籍。");
		resp.setRespCode(0);
		return resp;
	}

	

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		System.err.println("server has occured some exceptions !");
		cause.printStackTrace();
		ctx.close();
	}

}
