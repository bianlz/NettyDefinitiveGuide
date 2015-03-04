package com.bianlz.ndg.p7.serializable;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class SubRespClient {
	public void connect(String host,int port)throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap client = new Bootstrap();
			client.group(group)
					.channel(NioSocketChannel.class)
						.option(ChannelOption.TCP_NODELAY, true)
							.handler(new ChannelInitializer<SocketChannel>() {
								@Override
								protected void initChannel(SocketChannel arg0)
										throws Exception {
									// TODO Auto-generated method stub
									arg0.pipeline().addLast(new ObjectDecoder(1024, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
									arg0.pipeline().addLast(new ObjectEncoder());
									arg0.pipeline().addLast(new SubRespClientHandler());
								}
							});
			ChannelFuture f = client.connect("127.0.0.1", port).sync();
			f.channel().closeFuture().sync();
		}catch(Exception ex){
			group.shutdownGracefully();
		}
	}
	public static void main(String[] args){
		int port = 8080;
		if(null!=args&&args.length>0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(NumberFormatException ex){
				System.err.println("port must be number !");
			}
		}
		try{
			new SubRespClient().connect("127.0.0.1", port);
		}catch(Exception ex){
			System.err.println("client has occured some exceptions");
			ex.printStackTrace();
		}
	}
}
