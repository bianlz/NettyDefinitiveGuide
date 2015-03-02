package com.bianlz.ndg.p3.bio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimeServerHandlerExecutePool {
	private Executor executor ;
	public TimeServerHandlerExecutePool(int maxPoolSize,int queueSize) {
		// TODO Auto-generated constructor stub
		executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<java.lang.Runnable>(queueSize));
	}
	public void execute(java.lang.Runnable task){
		executor.execute(task);
	}

}
