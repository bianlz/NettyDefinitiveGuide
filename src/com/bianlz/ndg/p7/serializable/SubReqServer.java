package com.bianlz.ndg.p7.serializable;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class SubReqServer {
	public void bind(int port)throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup, workGroup)
					.channel(NioServerSocketChannel.class)
						.option(ChannelOption.SO_BACKLOG, 1024)
							.handler(new LoggingHandler(LogLevel.INFO))
								.childHandler(new ChannelInitializer<SocketChannel>() {
									@Override
									protected void initChannel(SocketChannel arg0)
											throws Exception {
										// TODO Auto-generated method stub
										arg0.pipeline().addLast(new ObjectDecoder(1024*1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
										arg0.pipeline().addLast(new ObjectEncoder());
										arg0.pipeline().addLast(new SubReqServerHandler());
									}
								});
			ChannelFuture f = server.bind(port).sync();
			f.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}

	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port = 8080;
		if(null!=args&&args.length>0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(NumberFormatException ex){
				System.err.println("port must be number !");
			}
		}
		try{
			new SubReqServer().bind(port);
		}catch(Exception ex){
			System.err.println("server has occured some exceptions");
			ex.printStackTrace();
		}
		
	
	}
	

}
