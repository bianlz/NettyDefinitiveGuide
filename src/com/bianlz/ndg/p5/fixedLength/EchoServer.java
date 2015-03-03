package com.bianlz.ndg.p5.fixedLength;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoServer {
	public void bind(int port)throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup,workGroup)
					.channel(NioServerSocketChannel.class)
						.option(ChannelOption.SO_BACKLOG, 1024)
							.childHandler(new ChannelInitializer<SocketChannel>() {
								@Override
								protected void initChannel(SocketChannel arg0)
										throws Exception {
									// TODO Auto-generated method stub
									arg0.pipeline().addLast(new FixedLengthFrameDecoder(15));
									arg0.pipeline().addLast(new StringDecoder());
									arg0.pipeline().addLast(new EchoServerHandler());
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
				System.exit(1);
			}
		}
		try {
			new EchoServer().bind(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("start server error !");
			e.printStackTrace();
		}
	}

}
