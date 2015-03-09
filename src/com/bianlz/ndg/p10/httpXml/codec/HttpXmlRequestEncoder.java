package com.bianlz.ndg.p10.httpXml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.net.InetAddress;
import java.util.List;

public class HttpXmlRequestEncoder extends AbstractHttpXmlEncoder<HttpXmlRequest> {

	@Override
	protected void encode(ChannelHandlerContext ctx, HttpXmlRequest msg,
			List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		ByteBuf buf = encode0(ctx, msg.getBody());
		FullHttpRequest request = msg.getRequest();
		if(null==request){
			request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/do", buf);
			HttpHeaders header = request.headers();
			header.set(HttpHeaders.Names.HOST, InetAddress.getLocalHost().getHostAddress());
			header.set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.CLOSE);
			header.set(HttpHeaders.Names.ACCEPT_ENCODING,HttpHeaders.Values.GZIP.toString()+','+HttpHeaders.Values.DEFLATE.toString());
			header.set(HttpHeaders.Names.ACCEPT_CHARSET,"ISO-8859-1,UTF-8;q=0.7,*;q=0.7");
			header.set(HttpHeaders.Names.ACCEPT_LANGUAGE,"zh");
			header.set(HttpHeaders.Names.USER_AGENT,"Netty xml http client side");
			header.set(HttpHeaders.Names.ACCEPT,"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		}
		HttpHeaders.setContentLength(request, buf.readableBytes());
		out.add(request);
	}
	
}
