package com.bianlz.ndg.p8.protoBuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;


public class SubRespClient {
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
									arg0.pipeline().addLast(new ProtobufVarint32FrameDecoder());
									arg0.pipeline().addLast(new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance()));
									arg0.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
									arg0.pipeline().addLast(new ProtobufEncoder());
									arg0.pipeline().addLast(new SubRespClientHandler());
								}
							});
			ChannelFuture f = client.connect("127.0.0.1", port).sync();
			f.channel().closeFuture().sync();
		}catch(Exception ex){
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
			}
		}
		try{
			new SubRespClient().connect("127.0.0.1", port);
		}catch(Exception ex){
			System.err.println("client has occured some exceptions");
			ex.printStackTrace();
		}
	}
}
