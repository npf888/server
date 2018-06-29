package com.netherfire.server.handler.result;

/**
 * 统一返回格式
 * @author 郭君伟
 *
 */
public class RestReturnVO {
	
	public int errorCode;
	
	public Object result;
	
	public RestReturnVO(){}
	
	public  RestReturnVO(int errorCode,Object result){
		this.errorCode = errorCode;
		this.result = result;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	
	

}
