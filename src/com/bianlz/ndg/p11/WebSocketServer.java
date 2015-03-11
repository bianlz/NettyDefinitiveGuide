package com.bianlz.ndg.p11;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {
	public void run(int port)throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup, workGroup)
					.channel(NioServerSocketChannel.class)
						.childHandler(new ChannelInitializer<SocketChannel>() {
							@Override
							protected void initChannel(SocketChannel arg0)
									throws Exception {
								// TODO Auto-generated method stub
								arg0.pipeline().addLast("http-codec",new HttpServerCodec());
								arg0.pipeline().addLast("http-aggreator",new HttpObjectAggregator(65536));
								arg0.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
								arg0.pipeline().addLast("webSocketHandler",new WebSocketServerHandler());
							}
						});
			ChannelFuture future = server.bind(port).sync();
			System.out.println("server has started on port ["+port+"]");
			future.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
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
		try {
			new WebSocketServer().run(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
