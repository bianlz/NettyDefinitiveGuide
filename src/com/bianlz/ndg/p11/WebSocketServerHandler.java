package com.bianlz.ndg.p11;

import io.netty.buffer.ByteBuf;
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
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import java.util.Date;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
	private WebSocketServerHandshaker handShaker;
	@Override
	protected void messageReceived(ChannelHandlerContext arg0, Object arg1)
			throws Exception {
		// TODO Auto-generated method stub
		if(arg1 instanceof FullHttpRequest){
			handleHttpRequest(arg0, (FullHttpRequest)arg1);
		}else if(arg1 instanceof WebSocketFrame){
			handleWebSocket(arg0, (WebSocketFrame)arg1);
		}
	}
	
	public void handleHttpRequest(ChannelHandlerContext ctx,FullHttpRequest request)throws Exception{
		if(!request.getDecoderResult().isSuccess()||!"websocket".equals(request.headers().get("Upgrade"))){
			sendHttpResponse(ctx, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}
		WebSocketServerHandshakerFactory shakerFac = new WebSocketServerHandshakerFactory("ws:localhost:8080/websocket", null, false);
		handShaker = shakerFac.newHandshaker(request);
		if(handShaker==null){
			WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
		}else{
			handShaker.handshake(ctx.channel(), request);
		}
	}
	
	public void handleWebSocket(ChannelHandlerContext ctx,WebSocketFrame frame)throws Exception{
		if(frame instanceof CloseWebSocketFrame){
			handShaker.close(ctx.channel(), (CloseWebSocketFrame)frame.retain());
			return;
		}
		if(frame instanceof PingWebSocketFrame){
			ctx.channel().write(new PingWebSocketFrame(frame.content().retain()));
			return;
		}
		if(!(frame instanceof TextWebSocketFrame)){
			throw new UnsupportedOperationException(String.format("%s framne type is not supported", frame.getClass().getName()));
		}
		String request = ((TextWebSocketFrame)frame).text();
		System.out.println("server recieved : " + request);
		ctx.channel().write(new TextWebSocketFrame(request+", welcome ,now is : [ "+new Date(System.currentTimeMillis())+" ]"));
	}
	public static void sendHttpResponse(ChannelHandlerContext ctx,FullHttpRequest req,FullHttpResponse resp){
		if(resp.getStatus().code()!=200){
			ByteBuf buf = Unpooled.copiedBuffer(resp.getStatus().toString(),CharsetUtil.UTF_8);
			resp.content().writeBytes(buf);
			buf.release();
			HttpHeaders.setContentLength(resp, resp.content().writableBytes());
		}
		ChannelFuture future = ctx.channel().writeAndFlush(resp);
		if(!HttpHeaders.isKeepAlive(req)||resp.getStatus().code()!=200){
			future.addListener(ChannelFutureListener.CLOSE);
		}
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
		cause.printStackTrace();
		ctx.close();
	}
	
}
