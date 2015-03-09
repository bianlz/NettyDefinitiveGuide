package com.bianlz.ndg.p10.httpXml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.io.StringReader;
import java.nio.charset.Charset;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;

public abstract class AbstractHttpXmlDecoder<T> extends MessageToMessageDecoder<T> {
	private IBindingFactory factory;
	private StringReader reader ;
	private Class<?> classz;
	private boolean isPrint;
	private final static String CHARSET_NAME="UTF-8";
	private final static Charset UTF_8=Charset.forName(CHARSET_NAME);
	
	public AbstractHttpXmlDecoder(Class<?> classz){
		this(classz,false);
	}
	public AbstractHttpXmlDecoder(Class<?> classz,boolean isPrint){
		this.classz = classz;
		this.isPrint = isPrint;
	}
	protected Object decode0(ChannelHandlerContext ctx,ByteBuf body)throws Exception{
		factory = BindingDirectory.getFactory(classz);
		String content = body.toString(UTF_8);
		if(isPrint){
			System.out.println("body is "+content);
		}
		reader = new StringReader(content);
		IUnmarshallingContext umsc = factory.createUnmarshallingContext();
		Object result = umsc.unmarshalDocument(reader);
		reader.close();
		reader = null;
		return result;
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		cause.printStackTrace();
		if(reader!=null){
			reader.close();
			reader = null;
		}
	};
}
