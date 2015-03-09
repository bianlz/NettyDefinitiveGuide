package com.bianlz.ndg.p10.httpXml.server;

import com.bianlz.ndg.p10.httpXml.codec.HttpXmlRequestDecoder;
import com.bianlz.ndg.p10.httpXml.codec.HttpXmlResponseEncoder;
import com.bianlz.ndg.p10.httpXml.pojo.Order;

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

public class HttpXmlServer {
	public void run(int port)throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel arg0)
								throws Exception {
							// TODO Auto-generated method stub
							arg0.pipeline().addLast("http-decoder",new HttpRequestDecoder());
							arg0.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
							arg0.pipeline().addLast("xml-decoder",new HttpXmlRequestDecoder(Order.class,true));
							arg0.pipeline().addLast("http-encoder", new HttpResponseEncoder());
							arg0.pipeline().addLast("xml-encoder",new HttpXmlResponseEncoder());
							arg0.pipeline().addLast("xmlServerHandler",new HttpXmlServerHandler());
						}
					});
			ChannelFuture future = server.bind(port).sync();
			System.out.println("server has been started !");
			future.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port = 8080;
		if(args!=null&&args.length>0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(NumberFormatException ex){
				System.err.println("port must be number !");
				System.exit(1);
			}
		}
		try{
			new HttpXmlServer().run(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

}
