package com.bianlz.ndg.p2.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel,AsyncTimeServerHandler > {

	@Override
	public void completed(AsynchronousSocketChannel result,
			AsyncTimeServerHandler attachment) {
		// TODO Auto-generated method stub
		attachment.serverChannel.accept(attachment,this);
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		result.read(buffer, buffer, new ReadCompletionHandler(result));
	}

	@Override
	public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
		// TODO Auto-generated method stub
		exc.printStackTrace();
		attachment.latch.countDown();
	}

}
