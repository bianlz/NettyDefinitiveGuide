package com.bianlz.ndg.p10.httpFile;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServer {
	private static final String DEFAULT_URL = "/src/com/bianlz/ndg/";
	public void run(final String url,final int port)throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup,workGroup)
					.channel(NioServerSocketChannel.class)
						.childHandler(new ChannelInitializer<SocketChannel>() {
							@Override
							protected void initChannel(SocketChannel arg0)
									throws Exception {
								// TODO Auto-generated method stub
								arg0.pipeline().addLast("http-decoder",new HttpRequestDecoder());
								arg0.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
								arg0.pipeline().addLast("htto-encoder",new HttpResponseEncoder());
								arg0.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
								arg0.pipeline().addLast("fileServerHandler",new HttpFileServerHandler(url));
							}
						});
			ChannelFuture  F = server.bind(port).sync();
			System.out.println("server has been started , url : [http:xxx.xxx.xxx.xxx:"+port+url+"]");
			F.channel().closeFuture().sync();
		}catch(Exception ex){
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
			}catch(Exception ex){
				System.err.println("port must be number !");
				ex.printStackTrace();
			}
		}
		String url = DEFAULT_URL;
		if(args.length>1){
			url = args[1];
		}
		try {
			new HttpFileServer().run(url, port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
