package com.bianlz.ndg.p2.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

public class ReadCompletionHandler implements
		CompletionHandler<Integer, ByteBuffer> {
	private AsynchronousSocketChannel channel;
	public ReadCompletionHandler(AsynchronousSocketChannel channel){
		if(this.channel == null){
			this.channel = channel;
		}
	}
	@Override
	public void completed(Integer result, ByteBuffer attachment) {
		// TODO Auto-generated method stub
		attachment.flip();
		byte[] body = new byte[attachment.remaining()];
		attachment.get(body);
		try{
			String req = new String(body,"UTF-8");
			System.out.println("server recieve :"+req);
			String resp = "query time".equalsIgnoreCase(req)?new Date(System.currentTimeMillis()).toString():"bad command";
			this.doWrite(resp);
		}catch(UnsupportedEncodingException ex){
			ex.printStackTrace();
		}
		
	}
	
	
	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		// TODO Auto-generated method stub
		try {
			this.channel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void doWrite(String resp){
		if(resp!=null&&resp.length()>0){
			byte[] bytes = resp.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			this.channel.write(writeBuffer,writeBuffer,new CompletionHandler<Integer, ByteBuffer>() {

				@Override
				public void completed(Integer result, ByteBuffer attachment) {
					// TODO Auto-generated method stub
					if(attachment.hasRemaining()){
						channel.write(attachment,attachment,this);
					}
				}

				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
					// TODO Auto-generated method stub
					try{
						channel.close();
					}catch(IOException ex){
						ex.printStackTrace();
					}
				}
			});
		}
	}

}
