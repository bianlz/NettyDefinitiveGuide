package com.bianlz.ndg.p3.basic;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
	public void bind(int port)throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup,workerGroup)
				  .channel(NioServerSocketChannel.class)
				  .option(ChannelOption.SO_BACKLOG, 1024)
				  .childHandler(new ChildChannelHandler());
			ChannelFuture f = server.bind(port).sync();
			f.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			// TODO Auto-generated method stub
			arg0.pipeline().addLast(new TimeServerHandler());
		}
		
	}
	
	public static void main(String[] args){
		int port = 8080;
		if(null!=args&&args.length>0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(Exception ex){
				System.err.println("port must be number !");
				System.exit(1);
			}
		}
		try{
			new TimeServer().bind(port);
		}catch(Exception ex){
			ex.printStackTrace();
			System.err.println("start server error ！");
		}
	}
}
