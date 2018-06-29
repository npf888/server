package com.netherfire.server.handler.core.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class StartServerChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
			
		ChannelPipeline pipeline = ch.pipeline();
		
        pipeline.addLast(new HttpServerCodec());
       // pipeline.addLast(new TestHandler());
        pipeline.addLast(new HttpUploadServerHandler());
        pipeline.addLast(new HttpObjectAggregator(655369));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new RouteHandler());
        //pipeline.addLast(new HttpUploadServerHandler());
		// pipeline.addLast(new SwitchCaseHandler());
       
	}
	
	

}
