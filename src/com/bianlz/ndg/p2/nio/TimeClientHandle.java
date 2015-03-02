package com.bianlz.ndg.p2.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TimeClientHandle implements Runnable {
	private String host ;
	private int port ;
	private SocketChannel socketChannel;
	private Selector selector;
	private volatile boolean stop;
	public TimeClientHandle(String ip,int port) {
		// TODO Auto-generated constructor stub
		this.host = ip==null?"127.0.0.1":ip;
		this.port = port;
		try{
			selector = Selector.open();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
		}catch(Exception ex){
			ex.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			doConnect();
		}catch(Exception ex){
			ex.printStackTrace();
			System.exit(1);
		}
		while(!stop){
			try {
				selector.select(1000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Set<SelectionKey> selectorKey = selector.selectedKeys();
			Iterator<SelectionKey> ite = selectorKey.iterator();
			SelectionKey key = null;
			while(ite.hasNext()){
				key = ite.next();
				ite.remove();
				try{
					handleInput(key);
				}catch(Exception ex){
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
			try {
				selector.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private void handleInput(SelectionKey key)throws IOException{
		if(key.isValid()){
			SocketChannel sc = (SocketChannel)key.channel();
			if(key.isConnectable()){
				if(sc.finishConnect()){
					sc.register(selector, SelectionKey.OP_READ);
					doWrite(sc);
				}else{
					System.exit(1);
				}
			}
			if(key.isReadable()){
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				if(readBytes>0){
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String body = new String(bytes,"UTF-8");
					System.out.println("NOW :"+body);
					this.stop = true;
				}else if(readBytes<0){
					key.cancel();
					sc.close();
				}else{
					return;
				}
			}
			
		}
	}
	
	private void doConnect()throws IOException{
		if(socketChannel.connect(new InetSocketAddress(host, port))){
			socketChannel.register(selector, SelectionKey.OP_READ);
			doWrite(socketChannel);
		}else{
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		}
	}
	
	private void doWrite(SocketChannel soc)throws IOException{
		byte[] req = "query time".getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
		writeBuffer.put(req);
		writeBuffer.flip();
		soc.write(writeBuffer);
		if(!writeBuffer.hasRemaining()){
			System.out.println("op:send is complete !");
		}
	}

}
