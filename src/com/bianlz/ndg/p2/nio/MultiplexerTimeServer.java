package com.bianlz.ndg.p2.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeServer implements Runnable {
	private Selector selector;
	private volatile boolean stop;
	private ServerSocketChannel servchannel;
	public MultiplexerTimeServer(int port) {
		// TODO Auto-generated constructor stub
		try{
			selector = Selector.open();
			servchannel = ServerSocketChannel.open();
			servchannel.configureBlocking(false);
			servchannel.socket().bind(new InetSocketAddress(port), 1024);
			servchannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("server start on port "+port);
		}catch(Exception ex){
			ex.printStackTrace();
			System.err.println("channel initial fail !");
			System.exit(1);
		}
	}
	public void stop(){
		this.stop = true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!stop){
			try {
				selector.select(1000);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Set<SelectionKey> set = selector.selectedKeys();
			Iterator<SelectionKey> ite = set.iterator();
			SelectionKey key = null;
			while(ite.hasNext()){
				key = ite.next();
				ite.remove();
				try{
					handleInput(key);
				}catch(IOException ex){
					if(key!=null){
						key.cancel();
						if(key.channel()!=null){
							try {
								key.channel().close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		if(selector!=null){
			try{
				selector.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	public void handleInput(SelectionKey key)throws IOException{
		if(key.isValid()){
			if(key.isAcceptable()){
				ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ);
			}
			if(key.isReadable()){
				SocketChannel sc = (SocketChannel)key.channel();
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				if(readBytes>0){
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String body = new String(bytes,"UTF-8");
					System.out.println("server recieve msg:"+body);
					String currentTime = "query time".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"bad command";
					doWrite(sc,currentTime);
				}else if(readBytes<0){
					key.cancel();
					sc.close();
				}else{
					
				}
			}
		}
	}
	public void doWrite(SocketChannel channel,String response)throws IOException{
		if(response!=null&&response.trim().length()>0){
			byte[] bytes = response.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			channel.write(writeBuffer);
		}
	}
}
