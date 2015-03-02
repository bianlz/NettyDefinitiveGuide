package com.bianlz.ndg.p3.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {
	public static void main(String args[]){
		int port = 8080;
		if(args!=null&&args.length>0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(Exception ex){
				System.err.println("port must be number");
				System.exit(1);
			}
		}
		ServerSocket server = null;
		try{
			server = new ServerSocket(port);
			Socket socket = null;
			while(true){
				socket = server.accept();
				TimeServerHandlerExecutePool executor = new TimeServerHandlerExecutePool(50, 1000);
				executor.execute(new TimeServerHandler(socket));
			}
		}catch(IOException io){
			io.printStackTrace();
			System.exit(1);
		}finally{
			if(server!=null){
				try {
					server.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				server = null;
			}
		}
	}
}
