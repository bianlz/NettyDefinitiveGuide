package com.bianlz.ndg.p4.frame;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeClient {
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
									arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));
									arg0.pipeline().addLast(new StringDecoder());
									arg0.pipeline().addLast(new TimeClientHandler());
								}
							});
			ChannelFuture f = client.connect(host, port).sync();
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port = 8080;
		if(null!=args&&args.length>0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(Exception ex){
				System.err.println("port must be number !");
			}
		}
		try{
			new TimeClient().connect("127.0.0.1", port);
		}catch(Exception ex){
			System.err.println("client has occured exception!");
			ex.printStackTrace();			
		}
	}

}
