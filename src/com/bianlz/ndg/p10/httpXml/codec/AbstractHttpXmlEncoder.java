package com.bianlz.ndg.p10.httpXml.codec;

import java.io.StringWriter;
import java.nio.charset.Charset;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public abstract class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {
	private IBindingFactory factory;
	private StringWriter writer;
	private static final String CHARSET="UTF-8";
	private final static Charset UTF_8 = Charset.forName(CHARSET);
	public ByteBuf encode0(ChannelHandlerContext ctx,Object body)throws Exception{
		factory = BindingDirectory.getFactory(body.getClass());
		writer = new StringWriter();
		IMarshallingContext msc = factory.createMarshallingContext();
		msc.setIndent(2);
		msc.marshalDocument(body, CHARSET, null, writer);;
		String xmlStr = writer.toString();
		writer.close();
		writer = null;
		ByteBuf buf = Unpooled.copiedBuffer(xmlStr,UTF_8);
		return buf;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		System.err.println("requestEncoder has occured some exception!");
		cause.printStackTrace();
		if(null!=writer){
			writer.close();
			writer = null;
		}
	}

}
