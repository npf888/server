package com.netherfire.server.asytask;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * 保证顺序
 * @author 郭君伟
 *
 */
public class AsynchronousTask {
	public static Logger log = LogManager.getLogger(AsynchronousTask.class);
	
	private final ConcurrentLinkedQueue<Runnable> taskList = new ConcurrentLinkedQueue<Runnable>();
	
	private volatile boolean processingCompleted=true;
	
	private ExecutorService executor;
	
	public AsynchronousTask(ExecutorService executor){
		this.executor = executor;
	}
	
	/**
	 * 按顺序执行的任务
	 * @param task
	 */
	public void addSynTask(Runnable task){
		taskList.offer(task);
		if(processingCompleted){
			processingCompleted = false;
			executor.execute(() -> {
				executeTaskQueue();
			});
		}
	}
	
	private void executeTaskQueue(){
		for(;;){
			Runnable task = taskList.poll();
			if(task == null){
				processingCompleted = true;
				break;
			}
			try{
				task.run();
			}catch(Exception e){
				log.error("", e);
			}
		}
	}
	
	/**
	 * 不用按顺序执行的任务
	 * @param task
	 */
	public void addTask(Runnable task){
		executor.execute(task);
	}
	
	

}
