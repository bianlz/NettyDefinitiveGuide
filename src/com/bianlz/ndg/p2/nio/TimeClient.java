package com.bianlz.ndg.p2.nio;

public class TimeClient {

	public TimeClient() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String args[]){
		int port = 8080;
		if(null!=args&&args.length>0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(Exception ex){
				System.err.print("port must be number !");
				System.exit(1);
			}
		}
		new Thread(new TimeClientHandle("127.0.0.1", port),"timeClient-001").start();
	}
}
