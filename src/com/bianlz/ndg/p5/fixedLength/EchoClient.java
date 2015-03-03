package com.bianlz.ndg.p5.fixedLength;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoClient {
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
									arg0.pipeline().addLast(new FixedLengthFrameDecoder(17));
									arg0.pipeline().addLast(new StringDecoder());
									arg0.pipeline().addLast(new EchoClientHandler());
								}
							});
			ChannelFuture f = client.connect(host, port).sync();
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
	public static void main(String[] args){
		int port = 8080;
		if(null!=args&&args.length>0){
			try{
				port = Integer.valueOf(port);
			}catch(Exception ex){
				System.err.println("port must be number ! ");
			}
		}
		try {
			new EchoClient().connect("127.0.0.1", 8080);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("server start error!");
		}
	}
}
