package com.bianlz.ndg.p10.httpFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

public class HttpFileServerHandler extends
		SimpleChannelInboundHandler<FullHttpRequest> {
	private String url ;
	private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");
	private static final Pattern ALLOWED_FILE_NAME=Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");
	public HttpFileServerHandler(String url){
		this.url = url;
	}
	@Override
	protected void messageReceived(ChannelHandlerContext ctx,
			FullHttpRequest request) throws Exception {
		// TODO Auto-generated method stub
		if(!request.getDecoderResult().isSuccess()){
			sendError(ctx, HttpResponseStatus.BAD_REQUEST);
			return;
		}
		if(request.getMethod()!= HttpMethod.GET){
			sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
			return;
		}
		final String uri = request.getUri();
		final String path = sanitizeUri(uri);
		if(path==null){
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
			return;
		}
		File file = new File(path);
		if(file.isHidden()||!file.exists()){
			sendError(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}
		if(file.isDirectory()){
			if(uri.endsWith("/")){
				sendListing(ctx, file);
			}else{
				sendRedirect(ctx, uri+"/");
			}
			return;
		}
		if(!file.isFile()){
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
			return;
		}
		RandomAccessFile accFile = null;
		try{
			accFile = new RandomAccessFile(file, "r");
		}catch(FileNotFoundException ex){
			sendError(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}
		long fileLength = accFile.length();
		HttpResponse resp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		HttpHeaders.setContentLength(resp, fileLength);
		setContextTypeHeader(resp, file);
		if(HttpHeaders.isKeepAlive(request)){
			resp.headers().set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.KEEP_ALIVE);
		}
		ctx.write(resp);
		ChannelFuture cf = ctx.write(new ChunkedFile(accFile,0,fileLength,8192),ctx.newProgressivePromise());
		cf.addListener(new ChannelProgressiveFutureListener() {
			
			@Override
			public void operationComplete(ChannelProgressiveFuture arg0)
					throws Exception {
				// TODO Auto-generated method stub
				System.out.println("transfer complete");
			}
			
			@Override
			public void operationProgressed(ChannelProgressiveFuture future, long progress,
					long total) throws Exception {
				// TODO Auto-generated method stub
				if(total<0){
					System.err.println("transfer process:"+progress);
				}else{
					System.err.println("transfer progress:"+progress+"/"+total);
				}
			}
		});
		ChannelFuture fu = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		if(!HttpHeaders.isKeepAlive(request)){
			fu.addListener(ChannelFutureListener.CLOSE);
		}
	}
	private static void sendListing(ChannelHandlerContext ctx,File dir){
		FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		resp.headers().set(HttpHeaders.Names.CONTENT_TYPE,"text/html;charset=UTF-8");
		StringBuilder buffer = new StringBuilder();
		String dirPath = dir.getPath();
		buffer.append("<!DOCTYPE html>\r\n");
		buffer.append("<html><head><title>");
		buffer.append(dirPath).append(" 目 录 ：");
		buffer.append("</head></title><body>");
		buffer.append("<h3>");
		buffer.append(dirPath).append(" 目 录 ：");
		buffer.append("</h3>\r\n");
		buffer.append("<ul>");
		buffer.append("<li>链接：<a href\".../\">..</a></li>\r\n");
		for(File f :dir.listFiles()){
			if(f.isHidden()||!f.canRead()){
				continue;
			}
			String name = f.getName();
			if(!ALLOWED_FILE_NAME.matcher(name).matches()){
				continue;
			}
			buffer.append("<li>链接：<a href=\"");
			buffer.append(name);
			buffer.append("\">");
			buffer.append(name);
			buffer.append("</a></li>\r\n");
		}
		buffer.append("</ul></body></html>\r\n");
		ByteBuf buf = Unpooled.copiedBuffer(buffer.toString().getBytes());
		resp.content().writeBytes(buf);
		buf.release();
		ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
	}
	private static void sendRedirect(ChannelHandlerContext ctx,String newUri){
		FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
		resp.headers().set(HttpHeaders.Names.LOCATION,newUri);
		ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
	}
	private String sanitizeUri(String uri){
		try{
			uri = URLDecoder.decode(uri, "UTF-8");
		}catch(UnsupportedEncodingException e){
			try{
				uri = URLDecoder.decode(uri, "ISO-8859-1");
			}catch(UnsupportedEncodingException ex){
				throw new Error();
			}
		}
		if(!uri.startsWith(url)){
			return null;
		}
		if(!uri.startsWith("/")){
			return null;
		}
		uri = uri.replace('/', File.separatorChar);
		if(uri.contains(File.separator+".")||uri.contains("."+File.separator)
				||uri.startsWith(".")||uri.endsWith(".")||INSECURE_URI.matcher(uri).matches()){
			return null;
		}
		return System.getProperty("user.dir")+File.separator+uri;
	}
	private static void sendError(ChannelHandlerContext ctx,HttpResponseStatus status){
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("Failure:"+status.toString()+"\r\n",CharsetUtil.UTF_8));
		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
	private static void setContextTypeHeader(HttpResponse resp,File file){
		MimetypesFileTypeMap map = new MimetypesFileTypeMap();
		resp.headers().set(HttpHeaders.Names.CONTENT_TYPE,map.getContentType(file.getPath()));
	}
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	    throws Exception {
		cause.printStackTrace();
		if (ctx.channel().isActive()) {
		    sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
    }
}
