package com.netherfire.server.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




public class HttpClientUtil {
	
	private static Logger logger = LogManager.getLogger(HttpClientUtil.class);
	/** 
	   * post鏂瑰紡鎻愪氦琛ㄥ崟锛堟ā鎷熺敤鎴风櫥褰曡姹傦級 
	   */
	  public static String postUrl(String url,HashMap<String,String> params) { 
	    // 鍒涘缓榛樿鐨刪ttpClient瀹炰緥.  
	    CloseableHttpClient httpclient = HttpClients.createDefault(); 
	    // 鍒涘缓httppost  
	    HttpPost httppost = new HttpPost(url); 
	    
	    RequestConfig requestConfig = RequestConfig.custom()    
	            .setConnectTimeout(4000).setConnectionRequestTimeout(1000)    
	            .setSocketTimeout(4000).build();    
	    httppost.setConfig(requestConfig); 
	    // 鍒涘缓鍙傛暟闃熷垪  
	    List<NameValuePair> formparams = new ArrayList<NameValuePair>(); 
	    Set<Entry<String, String>> entrySet= params.entrySet();
	    for(Entry<String, String> entry:entrySet){
	    	String key = entry.getKey();
	    	String value = entry.getValue();
	    	formparams.add(new BasicNameValuePair(key, value));
	    }
	    HttpEntity httpEntity; 
	    try { 
	    	httpEntity = new UrlEncodedFormEntity(formparams, "UTF-8"); 
	      httppost.setEntity(httpEntity);
	      logger.error("executing request " + httppost.getURI()); 
	      CloseableHttpResponse response = httpclient.execute(httppost); 
	      try { 
	        HttpEntity entity = response.getEntity(); 
	        if (entity != null) { 
	        	 return EntityUtils.toString(entity, "UTF-8"); 
	        } 
	      } finally { 
	        response.close(); 
	      } 
	    } catch (ClientProtocolException e) { 
	      e.printStackTrace(); 
	    } catch (UnsupportedEncodingException e1) { 
	      e1.printStackTrace(); 
	    } catch (IOException e) { 
	      e.printStackTrace(); 
	    } finally { 
	      // 鍏抽棴杩炴帴,閲婃斁璧勬簮  
	      try { 
	        httpclient.close(); 
	      } catch (IOException e) { 
	        e.printStackTrace(); 
	      } 
	    } 
	    
	    return null;
	} 
	  /** 
	   * post  json 鎻愪氦 鏂瑰紡鎻愪氦琛ㄥ崟锛堟ā鎷熺敤鎴风櫥褰曡姹傦級 
	   */
	  public static String postJSONUrl(String url,String jsonParamStr) { 
		  // 鍒涘缓榛樿鐨刪ttpClient瀹炰緥.  
		  CloseableHttpClient httpclient = HttpClients.createDefault(); 
		  // 鍒涘缓httppost  
		  HttpPost httppost = new HttpPost(url); 
		  RequestConfig requestConfig = RequestConfig.custom()    
		            .setConnectTimeout(4000).setConnectionRequestTimeout(1000)    
		            .setSocketTimeout(4000).build();    
		  httppost.setConfig(requestConfig); 
		  // 鍒涘缓鍙傛暟闃熷垪  
		  try { 
			  
			  // 寤虹珛涓�涓狽ameValuePair鏁扮粍锛岀敤浜庡瓨鍌ㄦ浼犻�佺殑鍙傛暟  
			  httppost.addHeader("Content-type","application/json; charset=utf-8");  
              httppost.setHeader("Accept", "application/json");  
              httppost.setEntity(new StringEntity(jsonParamStr, Charset.forName("UTF-8"))); 
			  logger.error("executing request " + httppost.getURI()); 
			  CloseableHttpResponse response = httpclient.execute(httppost); 
			  try { 
				  HttpEntity entity = response.getEntity(); 
				  if (entity != null) { 
					  return EntityUtils.toString(entity, "UTF-8"); 
				  } 
			  } finally { 
				  response.close(); 
			  } 
		  } catch (ClientProtocolException e) { 
			  e.printStackTrace(); 
		  } catch (UnsupportedEncodingException e1) { 
			  e1.printStackTrace(); 
		  } catch (IOException e) { 
			  e.printStackTrace(); 
		  } finally { 
			  // 鍏抽棴杩炴帴,閲婃斁璧勬簮  
			  try { 
				  httpclient.close(); 
			  } catch (IOException e) { 
				  e.printStackTrace(); 
			  } 
		  } 
		  
		  return null;
	  } 
	  
	  
	  /** 
	     * 鍙戦�� get璇锋眰 
	     */  
	  public static String get(String url) {  
	        CloseableHttpClient httpclient = HttpClients.createDefault();  
	        try {  
	            // 鍒涘缓httpget.    
	            HttpGet httpget = new HttpGet(url);  
	            RequestConfig requestConfig = RequestConfig.custom()    
	    	            .setConnectTimeout(4000).setConnectionRequestTimeout(1000)    
	    	            .setSocketTimeout(4000).build();    
	            httpget.setConfig(requestConfig); 
	             logger.error("executing request " + httpget.getURI());  
	            // 鎵цget璇锋眰.    
	            CloseableHttpResponse response = httpclient.execute(httpget);  
	            try {  
	                // 鑾峰彇鍝嶅簲瀹炰綋    
	                HttpEntity entity = response.getEntity();  
	                // 鎵撳嵃鍝嶅簲鐘舵��    
	                 logger.error(response.getStatusLine());  
	                if (entity != null) {  
	                    // 鎵撳嵃鍝嶅簲鍐呭    
	                     return EntityUtils.toString(entity, "UTF-8");  
	                }  
	            } finally {  
	                response.close();  
	            }  
	        } catch (ClientProtocolException e) {  
	            e.printStackTrace();  
	        } catch (ParseException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } finally {  
	            // 鍏抽棴杩炴帴,閲婃斁璧勬簮    
	            try {  
	                httpclient.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	        
	        return null;
	    }  
	  
}