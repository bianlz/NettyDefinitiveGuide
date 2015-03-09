package com.bianlz.ndg.p10.httpXml.client;

import com.bianlz.ndg.p10.httpXml.codec.HttpXmlRequest;
import com.bianlz.ndg.p10.httpXml.codec.HttpXmlResponse;
import com.bianlz.ndg.p10.httpXml.pojo.OrderFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HttpXmlClientHandler extends SimpleChannelInboundHandler<HttpXmlResponse> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		HttpXmlRequest req = new HttpXmlRequest(null, OrderFactory.create());
		ctx.writeAndFlush(req);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext arg0,
			HttpXmlResponse arg1) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("client recieve message's header is :"+arg1.getHttpResponse().headers().names());
		System.out.println("client recieve message's body is :"+arg1.getResult());
	}
	
	
}
