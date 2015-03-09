package com.bianlz.ndg.p10.httpXml.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.util.List;

public class HttpXmlRequestDecoder extends
		AbstractHttpXmlDecoder<FullHttpRequest> {

	public HttpXmlRequestDecoder(Class<?> classz) {
		this(classz,false);
		// TODO Auto-generated constructor stub
	}
	public HttpXmlRequestDecoder(Class<?> classz,boolean isPrintLog){
		super(classz,isPrintLog);
	}
	@Override
	protected void decode(ChannelHandlerContext ctx, FullHttpRequest fullReq,
			List<Object> list) throws Exception {
		// TODO Auto-generated method stub
		if(!fullReq.getDecoderResult().isSuccess()){
			sendError(ctx, HttpResponseStatus.BAD_REQUEST);
			return;
		}
		HttpXmlRequest req = new HttpXmlRequest(fullReq, decode0(ctx,fullReq.content()));
		list.add(req);
	}
	private static void sendError(ChannelHandlerContext ctx,HttpResponseStatus status){
		FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
				Unpooled.copiedBuffer("Failure:"+status.toString(),CharsetUtil.UTF_8));
		resp.headers().set(HttpHeaders.Names.CONTENT_TYPE,"text/plain;charset=UTF-8");
		ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
		
	}
	
}
