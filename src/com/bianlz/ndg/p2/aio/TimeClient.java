package com.bianlz.ndg.p2.aio;

public class TimeClient {
	public static void main(String args[]){
		int port = 8080;
		if(null!=args&&args.length>0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(NumberFormatException ex){
				System.err.println("port must be number !");
			}
		}
		AsyncTimeClientHandler client = new AsyncTimeClientHandler("127.0.0.1",port);
		new Thread(client).start();
	}
}
