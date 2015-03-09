package com.bianlz.ndg.p10.httpXml.client;

import com.bianlz.ndg.p10.httpXml.codec.HttpXmlRequestEncoder;
import com.bianlz.ndg.p10.httpXml.codec.HttpXmlResponseDecoder;
import com.bianlz.ndg.p10.httpXml.pojo.Order;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

public class HttpXmlClient {
	public void connect(String host,int port)throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap client = new Bootstrap();
			client.group(group).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel arg0) throws Exception {
							// TODO Auto-generated method stub
							arg0.pipeline().addLast("http-decoder",new HttpResponseDecoder());
							arg0.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
							arg0.pipeline().addLast("xml-decoder",new HttpXmlResponseDecoder(Order.class, true));
							arg0.pipeline().addLast("http-encoder",new HttpRequestEncoder());
							arg0.pipeline().addLast("xml-decoder",new HttpXmlRequestEncoder());
							arg0.pipeline().addLast("xmlClientHandler",new HttpXmlClientHandler());
						}
					});
			ChannelFuture future = client.connect(host, port).sync();
			future.channel().close().sync();
		}finally{
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
				System.exit(1);
			}
		}
		try {
			new HttpXmlClient().connect("127.0.0.1", port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
}
