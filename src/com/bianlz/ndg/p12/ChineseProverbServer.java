package com.bianlz.ndg.p12;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class ChineseProverbServer {
	public void run(int port)throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap server = new Bootstrap();
			server.group(group)
					.channel(NioDatagramChannel.class)
						.option(ChannelOption.SO_BROADCAST, true)
							.handler(new ChineseProverbServerHandler());
			server.bind(port).sync().channel().closeFuture().await();
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
			new ChineseProverbServer().run(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
