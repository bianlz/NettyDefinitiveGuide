package com.bianlz.ndg.p2.aio;

public class TimeServer {
	public static void main(String args[]){
		int port = 8080;
		if(null!=args&&args.length>0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(Exception ex){
				System.err.println("port must be number !");
			}
		}
		AsyncTimeServerHandler server = new AsyncTimeServerHandler(port);
		new Thread(server,"asyServer-001").start();;
	}
}
