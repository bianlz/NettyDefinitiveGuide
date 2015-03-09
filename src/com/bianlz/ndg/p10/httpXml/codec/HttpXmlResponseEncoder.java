package com.bianlz.ndg.p10.httpXml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.util.List;

public class HttpXmlResponseEncoder extends
		AbstractHttpXmlEncoder<HttpXmlResponse> {

	@Override
	protected void encode(ChannelHandlerContext ctx, HttpXmlResponse xmlResp,
			List<Object> list) throws Exception {
		// TODO Auto-generated method stub
		ByteBuf buf = encode0(ctx, xmlResp.getResult());
		FullHttpResponse resp = xmlResp.getHttpResponse();
		if(resp==null){
			resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,buf);
		}else{
			resp = new DefaultFullHttpResponse(xmlResp.getHttpResponse().getProtocolVersion(), 
					xmlResp.getHttpResponse().getStatus(), buf);
		}
		resp.headers().set(HttpHeaders.Names.CONTENT_TYPE,"text/xml");
		HttpHeaders.setContentLength(resp, buf.readableBytes());
		list.add(resp);
	}

}
