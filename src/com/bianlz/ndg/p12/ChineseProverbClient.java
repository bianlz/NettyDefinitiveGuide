package com.bianlz.ndg.p12;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

public class ChineseProverbClient {
	public void run(int port)throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap client = new Bootstrap();
			client.group(group)
					.channel(NioDatagramChannel.class)
						.option(ChannelOption.SO_BROADCAST, true)
							.handler(new ChineseProverbClientHandler());
			Channel channel = client.bind(0).sync().channel();
			channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("query proverb",CharsetUtil.UTF_8), new InetSocketAddress("255.255.255.255", port))).sync();
			if(!channel.closeFuture().await(15000)){
				System.out.println("query out time ! ");
			}
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
			}catch(NumberFormatException ex){
				System.err.println("port must be number ! ");
				System.exit(1);
			}
		}
		try {
			new ChineseProverbClient().run(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
