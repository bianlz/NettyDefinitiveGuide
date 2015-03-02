package com.bianlz.ndg.p2.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeServerHandler implements Runnable {
    AsynchronousServerSocketChannel serverChannel ;
    CountDownLatch latch;
	public AsyncTimeServerHandler(int port){
		try {
			serverChannel = AsynchronousServerSocketChannel.open();
			serverChannel.bind(new InetSocketAddress(port));
			System.out.println("server start on port :"+port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("init server error !");
			System.exit(1);
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		latch = new CountDownLatch(1);
		doAccept();
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void doAccept(){
		serverChannel.accept(this,new AcceptCompletionHandler());
	}
}
