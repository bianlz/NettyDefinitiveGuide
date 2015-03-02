package com.bianlz.ndg.p3.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class TimeServerHandler implements Runnable {
	private Socket socket;
	public TimeServerHandler(Socket socket){
		this.socket = socket;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		BufferedReader in = null;
		PrintWriter out = null;
		try{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(),true);   //参数flush 保证msg的发出  防止阻塞
			String body = null;
			String current = null;
			while(true){
				body = in.readLine();
				if(body==null){
					break;
				}
				System.out.println("recieve a command from clent");
				current = body.equalsIgnoreCase("query time")?(new Date(System.currentTimeMillis())).toString():"BAD";
				out.println(current);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
