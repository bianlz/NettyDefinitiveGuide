package com.bianlz.ndg.p2.nio;

public class TimeServer {

	public TimeServer() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String args[]){
		int port = 8080;
		if(null!=args&&args.length>0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(Exception ex){
				System.err.println("port must be number!");
				System.exit(1);
			}
		}
		MultiplexerTimeServer server = new MultiplexerTimeServer(port);
		new Thread(server,"NIO-SERVER-001").start();
	}

}
