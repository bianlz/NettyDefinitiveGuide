package com.bianlz.ndg.p13;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class FileServer {
	public void run(int port)throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup, workGroup)
					.channel(NioServerSocketChannel.class)
						.option(ChannelOption.SO_BACKLOG, 100)
							.childHandler(new ChannelInitializer<SocketChannel>() {

								@Override
								protected void initChannel(SocketChannel arg0)
										throws Exception {
									// TODO Auto-generated method stub
									arg0.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
									arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));
									arg0.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
									arg0.pipeline().addLast(new FileServerHandler());
								}
							});
			ChannelFuture future = server.bind(port).sync();
			System.out.println("server has started on port : ["+port+" ]");
			future.channel().closeFuture().sync();
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
				System.err.println("port must be number ! ");
				System.exit(1);
			}
		}
		try {
			new FileServer().run(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
