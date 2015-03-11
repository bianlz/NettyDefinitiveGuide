package com.bianlz.ndg.p13;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.RandomAccessFile;

public class FileServerHandler extends SimpleChannelInboundHandler<String> {
	private static final String CR = System.getProperty("line.separator");
	@Override
	protected void messageReceived(ChannelHandlerContext arg0, String arg1)
			throws Exception {
		// TODO Auto-generated method stub
		File file = new File(arg1);
		if(file.exists()){
			if(!file.isFile()){
				arg0.writeAndFlush("not a file :"+file+CR);
				return;
			}
			arg0.write(file+" " + file.length() + CR);
			RandomAccessFile randomFile = new RandomAccessFile(arg1, "r");
			FileRegion region = new DefaultFileRegion(randomFile.getChannel(), 0, randomFile.length());
			arg0.write(region);
			arg0.writeAndFlush(CR);
			randomFile.close();
		}else{
			arg0.writeAndFlush("file not found :"+file +CR);
		}
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		cause.printStackTrace();
		ctx.close();
	}

}
