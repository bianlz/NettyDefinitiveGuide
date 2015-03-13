package com.bianlz.ndg.p14.client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.bianlz.ndg.p14.codec.NettyMessageDecoder;
import com.bianlz.ndg.p14.codec.NettyMessageEncoder;
import com.bianlz.ndg.p14.content.Constant;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyClient {
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private EventLoopGroup group = new NioEventLoopGroup();
	public void connect(String host,int port)throws Exception{
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
										arg0.pipeline().addLast(new NettyMessageDecoder(1024*1024, 4, 4));
										arg0.pipeline().addLast("messageEncoder",new NettyMessageEncoder());
										arg0.pipeline().addLast("timeOutHandler",new ReadTimeoutHandler(5000));
										arg0.pipeline().addLast("loginAuthReq",new LoginAuthReqHandler());
										arg0.pipeline().addLast("heartBeat",new HeartBeatReqHandler());
									}
								});
			ChannelFuture future = client.connect(new InetSocketAddress(host, port)
									,new InetSocketAddress(Constant.LOCALIP, Constant.LOCAL_PORT)).sync();
			future.channel().closeFuture().sync();
		}finally{
			executor.execute(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try{
						TimeUnit.SECONDS.sleep(5000);
						connect(Constant.REMOTEIP, Constant.PORT);
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			});
		}
	}
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		new NettyClient().connect(Constant.REMOTEIP, Constant.PORT);
	}

}
