package com.bianlz.ndg.p14.client;

import java.util.concurrent.TimeUnit;

import com.bianlz.ndg.p14.content.MessageType;
import com.bianlz.ndg.p14.message.Header;
import com.bianlz.ndg.p14.message.NettyMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;

public class HeartBeatReqHandler extends ChannelHandlerAdapter {
	private volatile ScheduledFuture<?> heartBeat;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub
		NettyMessage message = (NettyMessage)msg;
		if(message!=null&&message.getHeader().getType()==MessageType.LOGIN_RESP.value()){
			heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
		}else if(message.getHeader()!=null&&message.getHeader().getType()==MessageType.HEARTBEAT_RESP.value()){
			System.out.println("client has recieved message"+message);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		if(heartBeat!=null){
			heartBeat.cancel(true);
			heartBeat = null;
		}
		ctx.fireExceptionCaught(cause);
	}
	
	private class HeartBeatTask implements Runnable{
		private final ChannelHandlerContext ctx;
		public HeartBeatTask(ChannelHandlerContext ctx){
			this.ctx = ctx;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			NettyMessage message = buildMessage();
			System.out.println("client has sended heart beat req :"+message);
			ctx.writeAndFlush(message);
		}
		
		private NettyMessage buildMessage(){
			NettyMessage message = new NettyMessage();
			Header header = new Header();
			header.setType(MessageType.HEARTBEAT_REQ.value());
			message.setHeader(header);
			return message;
		}
	}
	
}
