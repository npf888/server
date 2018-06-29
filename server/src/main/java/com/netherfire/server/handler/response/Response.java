package com.netherfire.server.handler.response;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.AsciiString;

public class Response {

	    public static final AsciiString CONTENT_TYPE = new AsciiString("Content-Type");
	    public static final AsciiString CONTENT_LENGTH = new AsciiString("Content-Length");
	    public static final AsciiString CONNECTION = new AsciiString("Connection");
	    public static final AsciiString KEEP_ALIVE = new AsciiString("keep-alive");
	
	public static FullHttpResponse build(byte[] date,boolean iskeepAlive){
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(date));
        response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
        if (iskeepAlive) {
        	 response.headers().set(CONNECTION, KEEP_ALIVE);
         }
        return response;
	}
	public static FullHttpResponse buildHtml(byte[] date,boolean iskeepAlive){
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(date));
		response.headers().set(CONTENT_TYPE, "text/html");
		response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
		if (iskeepAlive) {
			response.headers().set(CONNECTION, KEEP_ALIVE);
		}
		return response;
	}
	
	
	
}
