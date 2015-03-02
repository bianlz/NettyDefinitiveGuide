package com.bianlz.ndg.p3.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class TimeClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port = 8080;
		if(args!=null&&args.length>0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(Exception ex){
				System.err.println("port must be number");
				System.exit(1);
			}
		}
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try{
			 socket = new Socket("127.0.0.1", port);
			 in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 out = new PrintWriter(socket.getOutputStream(),true);
			 out.println("query time");
			 System.out.println("send command to server");
			 System.out.println("Now:" + in.readLine());
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			out.close();
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
