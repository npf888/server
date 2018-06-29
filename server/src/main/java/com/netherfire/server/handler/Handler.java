package com.netherfire.server.handler;

import java.util.List;
import java.util.Map;

import com.netherfire.server.handler.request.UploadForm;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface Handler {
//	void handleGet(ChannelHandlerContext ctx,Map<String, List<String>> parameter,boolean iskeepAlive);
	void handleGet(ChannelHandlerContext ctx,Map<String, List<String>> parameter, FullHttpRequest request,
			boolean iskeepAlive);
//	void handlePost(ChannelHandlerContext ctx,ByteBuf byteBuf,boolean iskeepAlive);
	void handlePost(ChannelHandlerContext ctx,FullHttpRequest request,boolean iskeepAlive);
	
	void uploadPicture(ChannelHandlerContext ctx, UploadForm uploadForm,boolean iskeepAlive);
	
}
