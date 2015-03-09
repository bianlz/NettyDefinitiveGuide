package com.bianlz.ndg.p10.httpXml.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.ArrayList;
import java.util.List;

import com.bianlz.ndg.p10.httpXml.codec.HttpXmlRequest;
import com.bianlz.ndg.p10.httpXml.pojo.Address;
import com.bianlz.ndg.p10.httpXml.pojo.Order;

public class HttpXmlServerHandler extends SimpleChannelInboundHandler<HttpXmlRequest> {

	@Override
	protected void messageReceived(final ChannelHandlerContext ctx,
			HttpXmlRequest xmlRequest) throws Exception {
		// TODO Auto-generated method stub
		FullHttpRequest request = xmlRequest.getRequest();
		Order order = (Order)xmlRequest.getBody();
		System.out.println("server has recieved message:"+order);
		doBusiness(order);
		ChannelFuture future = ctx.writeAndFlush(new HttpXmlRequest(null, order));
		if(!HttpHeaders.isKeepAlive(request)){
			future.addListener(new GenericFutureListener<Future<? super Void>>() {
				@Override
				public void operationComplete(Future<? super Void> arg0)
						throws Exception {
					// TODO Auto-generated method stub
					ctx.close();
				}
			});
		}
	}
	
	public void doBusiness(Order order){
		order.getCustomer().setFirstName("郭");
		order.getCustomer().setLastName("德纲");
		List<String> midNames = new ArrayList<String>();
		midNames.add("于谦");
		order.getCustomer().setMideleNames(midNames);
		Address address = order.getBillTo();
		address.setCity("北京");
		address.setCountry("东城区");
		address.setState("德云社");
		order.setBillTo(address);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		System.err.println("server has occured some problems!");
		cause.printStackTrace();
		if(ctx.channel().isActive()){
			sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
	}
	private static void sendError(ChannelHandlerContext ctx,HttpResponseStatus status){
		FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,Unpooled.copiedBuffer("失败"+status.toString()+"\r\n",CharsetUtil.UTF_8));
		resp.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain;charset=UTF-8");
		ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
	}

}
