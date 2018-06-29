package com.netherfire.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.netherfire.server.config.Config;
import com.netherfire.server.handler.Handler;
import com.netherfire.server.handler.imple.DownloadFile;
import com.netherfire.server.utils.UtilsIO;
import com.netherfire.server.vo.IdentifyingCode;

/**
 * 
 * 全局单例
 * @author 郭君伟
 *
 */
public class Global {
	private static final Logger logger = LogManager.getLogger(Global.class);
	
	private static Global instance = new Global();
	
	/**url 处理类 **/
	private Map<String,Handler> handlerMap = new HashMap<String,Handler>();
	/**短信验证码的map**/
	private Map<String,IdentifyingCode> identifyingCodeMap = new HashMap<String,IdentifyingCode>();
	/**配置数据 **/
	private  Config config = new Config();
	
	private DownloadFile downloadFile;
	
	/**
	 * 线程池
	 */
	private  ScheduledExecutorService executor;
	
	private Global(){}
	
	public static Global getInstance(){
		return instance;
	}
	
	public void init(){
		try {
			initConfig();
			executor = Executors.newScheduledThreadPool(config.getThreadCount());
			initHandler();
			downloadFile = new DownloadFile();
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	/**
	 * 初始化配置数据
	 * @throws IOException
	 */
	private void initConfig() throws IOException{
		InputStream input = Server.class.getResourceAsStream("/config.json");
		
		String content = UtilsIO.getString(input);
		
		config = JSON.parseObject(content, Config.class);
	}
	
	/**
	 * 初始化Handler
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private void initHandler() throws Exception{
		ResourceBundle bundle = ResourceBundle.getBundle("uri");
		for(String key : bundle.keySet()){
			handlerMap.put(key, (Handler)(Class.forName(bundle.getString(key)).newInstance()));
			logger.info("----------------URL=key:"+key);
		}
	}
	
	
	
	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public boolean checkUri(String uri){
		return handlerMap.containsKey(uri);
	}
	
	public Handler getHandler(String uri){
		return handlerMap.get(uri);
	}

	public ScheduledExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ScheduledExecutorService executor) {
		this.executor = executor;
	}

	public DownloadFile getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(DownloadFile downloadFile) {
		this.downloadFile = downloadFile;
	}

	public Map<String, IdentifyingCode> getIdentifyingCodeMap() {
		return identifyingCodeMap;
	}

	public void setIdentifyingCodeMap(
			Map<String, IdentifyingCode> identifyingCodeMap) {
		this.identifyingCodeMap = identifyingCodeMap;
	}

	
	
	
	
}
