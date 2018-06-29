package com.netherfire.server.handler.result;

public class ResultCode {
	
	/** 成功  **/
	public static final int SUCCESS = 0;
	/** 失败  **/
	public static final int FAIL = 1;
	/** h5 注册时：用户名已存在  **/
	public static final int EXIST = 2;
	/** h5 登录时：用户名不存在  **/
	public static final int NOTEXIST = 3;
	/** 登录注册一起的接口使用  HtmlRegisterLoginHandler 用户名存在 ，但是密码错误**/
	public static final int PASSWORD_WRONG = 4;
	/** 还没有通过验证码 登录过 **/
	public static final int IDENTIFYING_CODE_NOT_LOGIN = 5;

}
