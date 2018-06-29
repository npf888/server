package com.netherfire.server.handler.imple;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.netherfire.server.Global;
import com.netherfire.server.db.ImageDaoServer;
import com.netherfire.server.db.UserInfoDaoServer;
import com.netherfire.server.db.entity.UserInfo;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.request.UploadForm;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;
import com.netherfire.server.handler.result.UploadReturn;
import com.netherfire.server.manager.UserManager;
import com.netherfire.server.manager.pojo.CacheUserInfo;
import com.netherfire.server.utils.UtilsIO;
/**
 * 上传图片
 * @author 郭君伟
 *
 */
public class UploadPictureHandler extends BaseHandler {
	
	public static Logger log = LogManager.getLogger(UploadPictureHandler.class);
	
	public void uploadPicture(ChannelHandlerContext ctx, UploadForm uploadForm,boolean iskeepAlive){

			asyTask.addTask(() ->{
				try{

					//图片的名字
					long userId = uploadForm.getUserId();
					long name = userId;
					
					//修改玩家头像
					RestReturnVO vo = new RestReturnVO();
					vo.setErrorCode(ResultCode.SUCCESS);
					UploadReturn result = new UploadReturn();
					result.setImg(String.valueOf(name));
					result.setUserId(userId);
					vo.setResult(result);
					if(userId != 0){
						loadPicture(uploadForm.getContent(),name);
						UserInfoDaoServer.updateImg(userId,String.valueOf(name));
						CacheUserInfo cui = UserManager.getInstance().getUserInfo(userId);
						if(cui!=null)
						{
							UserInfo ui = cui.getUserInfo();
							if(ui!=null)
							{
								ui.setImg(result.getImg());
							}
						}
					}else{
						long nowPicture = new Date().getTime(); 
						//存到文件系统里
						loadActivityPicture(uploadForm.getContent(),nowPicture);
						//存到数据库里
						ImageDaoServer.save(String.valueOf(nowPicture));
					}
					
					FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
					ctx.write(response).addListener(ChannelFutureListener.CLOSE);
			        ctx.flush();
			        log.info(JSON.toJSONString(vo));
				}catch(Exception e){
					log.error(e.toString());
				}
			});
		
		
	}

	

	@Override
	public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, boolean iskeepAlive) {
		try{
			ByteBuf byteBuf = request.content();
			String data = UtilsIO.byteBufToStr(byteBuf);
			
			
			log.info(data);
			
			asyTask.addTask(() ->{
				try{
					
					UploadForm uploadForm = JSON.parseObject(data, UploadForm.class);
					
					//图片的名字
					long name = System.nanoTime();
					
					loadPicture(uploadForm.getContent(),name);
					
					long userId = uploadForm.getUserId();
					
					//修改玩家头像
					RestReturnVO vo = new RestReturnVO();
					vo.setErrorCode(ResultCode.SUCCESS);
					UploadReturn result = new UploadReturn();
					result.setImg(String.valueOf(name));
					result.setUserId(userId);
					vo.setResult(result);
					
					FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
					ctx.write(response).addListener(ChannelFutureListener.CLOSE);
			        ctx.flush();
					
				}catch(Exception e){
					log.error("", e);
				}
			});
		} catch (IOException e) {
			log.error("", e);
		}
		
	}
	
	

	/**
	 * 保存头像图片
	 * @param byteBuf
	 */
	private void loadPicture(byte[] byteBuf,long name){
		try {
			FileOutputStream out = new FileOutputStream(Global.getInstance().getConfig().getUpdatePath()+name);
			out.write(byteBuf, 0, byteBuf.length);
			out.flush();
			out.close();
		} catch (Exception e) {
			log.error("", e);
		}
	}
	
	/**
	 * 保存活动的图片
	 * @param byteBuf
	 */
	
	private void loadActivityPicture(byte[] byteBuf, long name) {
		try {
			FileOutputStream out = new FileOutputStream(Global.getInstance().getConfig().getLoadPath()+"activity/"+name);
			out.write(byteBuf, 0, byteBuf.length);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
