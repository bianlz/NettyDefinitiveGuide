package com.bianlz.ndg.p10.httpXml.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import java.util.List;

public class HttpXmlResponseDecoder extends
		AbstractHttpXmlDecoder<DefaultFullHttpResponse> {
		public HttpXmlResponseDecoder(Class<?> classz){
			this(classz,false);
		}
		public HttpXmlResponseDecoder(Class<?> classz,boolean isPrintLog){
			super(classz,isPrintLog);
		}
		@Override
		protected void decode(ChannelHandlerContext ctx,
				DefaultFullHttpResponse msg, List<Object> out) throws Exception {
			// TODO Auto-generated method stub
			HttpXmlResponse resp = new HttpXmlResponse(msg, decode0(ctx,msg.content()));
			out.add(resp);
		}
}
