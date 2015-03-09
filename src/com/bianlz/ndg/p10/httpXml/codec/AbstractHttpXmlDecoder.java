package com.bianlz.ndg.p10.httpXml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.io.StringReader;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;

public abstract class AbstractHttpXmlDecoder<T> extends MessageToMessageDecoder<T> {
	private IBindingFactory factory;
	private StringReader reader ;
	private Class<?> classz;
	private boolean isPrint;
	
	public AbstractHttpXmlDecoder(Class<?> classz){
		this(classz,false);
	}
	public AbstractHttpXmlDecoder(Class<?> classz,boolean isPrint){
		this.classz = classz;
		this.isPrint = isPrint;
	}
	protected Object decode0(ChannelHandlerContext ctx,ByteBuf buf)throws Exception{
		this.factory = BindingDirectory.getFactory(classz);
		String content = buf.toString(CharsetUtil.UTF_8);
		if(this.isPrint){
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
