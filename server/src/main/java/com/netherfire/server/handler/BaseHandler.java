package com.netherfire.server.handler;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.netherfire.server.Global;
import com.netherfire.server.asytask.AsynchronousTask;
import com.netherfire.server.handler.request.UploadForm;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public abstract class BaseHandler implements Handler {
	
	protected AsynchronousTask asyTask = new AsynchronousTask(Global.getInstance().getExecutor());
	
	protected static final SerializerFeature[] features = {
			SerializerFeature.DisableCircularReferenceDetect,//打开循环引用检测，JSONField(serialize = false)不循环
			SerializerFeature.WriteDateUseDateFormat,//默认使用系统默认 格式日期格式化
			SerializerFeature.WriteMapNullValue, //输出空置字段
			SerializerFeature.WriteNullListAsEmpty,//list字段如果为null，输出为[]，而不是null
			SerializerFeature.WriteNullNumberAsZero,// 数值字段如果为null，输出为0，而不是null
			SerializerFeature.WriteNullBooleanAsFalse,//Boolean字段如果为null，输出为false，而不是null
			SerializerFeature.WriteNullStringAsEmpty//字符类型字段如果为null，输出为""，而不是null
			};



	@Override
	public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request,boolean iskeepAlive) {
		

	}

	@Override
	public void uploadPicture(ChannelHandlerContext ctx, UploadForm uploadForm, boolean iskeepAlive) {
		
		
	}
	@Override
	public void handleGet(ChannelHandlerContext ctx,Map<String, List<String>> parameter, FullHttpRequest request,
			boolean iskeepAlive) {
		// TODO Auto-generated method stub
		
	}

	
	
	

}
