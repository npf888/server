package com.netherfire.server.handler.core.http;

import static io.netty.buffer.Unpooled.copiedBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.StringUtils;
import com.netherfire.server.Global;
import com.netherfire.server.handler.Handler;
import com.netherfire.server.handler.request.UploadForm;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpData;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;

public class HttpUploadServerHandler extends SimpleChannelInboundHandler<HttpObject> {
	
	 private static final Logger logger = LogManager.getLogger(HttpUploadServerHandler.class);
	 
	    private HttpRequest request;

	    private boolean readingChunks;

	    private HttpData partialContent;

	    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); // Disk if size exceed

	    private HttpPostRequestDecoder decoder;
	    
	    private ArrayList<Byte> byteList = new ArrayList<Byte>();
	   
	    private String userId;
	    private String userCode;
	    
	    private String patchuri;
	    
	    private boolean iskeepAlive;
	    
	    private volatile boolean fly = true;

	    static {
	        DiskFileUpload.deleteOnExitTemporaryFile = true; // should delete file
	                                                         // on exit (in normal
	                                                         // exit)
	        DiskFileUpload.baseDirectory = null; // system temp directory
	        DiskAttribute.deleteOnExitTemporaryFile = true; // should delete file on
	                                                        // exit (in normal exit)
	        DiskAttribute.baseDirectory = null; // system temp directory
	    }

	    @Override
	    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
	        if (decoder != null) {
	            decoder.cleanFiles();
	        }
	    }
	    
	    @Override
		public void channelReadComplete(ChannelHandlerContext ctx) {
	    	
			ctx.flush();
		}
	    
	  

	    @Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
	    	/*logger.info(ctx);
	    	ctx.fireChannelActive();
	    	ChannelPipeline channelPipeline =  ctx.pipeline();
            channelPipeline.remove(this);*/
            fly = false;
		}



		@Override
	    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
	    //	logger.info(msg);
	    	//logger.info(ctx);
	    	
	    	//logger.info("=======================================================");
	    	 if(fly){
	    		 ctx.fireChannelRead(msg);
	    	 }else{ 
	    		 if (msg instanceof HttpRequest) { 
	 	        	
	 	             HttpRequest request = this.request = (HttpRequest) msg;
	    			
	 	            String uri = request.uri();
	 	            if(!uri.equals("/api/upload.json")){
	 	            	 fly = true;
	 	            	 ctx.fireChannelRead(msg);
	 	            	 ctx.pipeline().remove(this);
	 	            }else{
		 	            try {
		 	            	patchuri = "/api/upload.json";
		 	            	//是否保存长连接
		 	       		    //iskeepAlive = HttpUtil.isKeepAlive(request);
		 	                decoder = new HttpPostRequestDecoder(factory, request);
		 	            } catch (ErrorDataDecoderException e1) {
		 	                logger.error("", e1);
		 	               // writeResponse(ctx.channel());
		 	                ctx.channel().close();
		 	                return;
		 	            }
		 	            readingChunks = HttpUtil.isTransferEncodingChunked(request);
		 	            if (readingChunks) {
		 	                readingChunks = true;
		 	            }
	 	            }
	 	            
	 	           
	 	        }
                if(!fly){
                	// check if the decoder was constructed before
    	 	        // if not it handles the form get
    	 	        if (decoder != null) {
    	 	            if (msg instanceof HttpContent) {
    	 	                // New chunk is received
    	 	                HttpContent chunk = (HttpContent) msg;
    	 	                try {
    	 	                    decoder.offer(chunk);
    	 	                } catch (ErrorDataDecoderException e1) {
    	 	                	logger.error("", e1);
    	 	                   // writeResponse(ctx.channel());
    	 	                    ctx.channel().close();
    	 	                    return;
    	 	                }
    	 	               
    	 	                // example of reading chunk by chunk (minimize memory usage due to
    	 	                // Factory)
    	 	                readHttpDataChunkByChunk();
    	 	                // example of reading only if at the end
    	 	                if (chunk instanceof LastHttpContent) {
    	 	                    
    	 	                UploadForm form = new UploadForm();
    	 	                logger.info("---上传图片-1--userId："+userId+"!StringUtils.isNullOrEmpty(userId)::"+!StringUtils.isNullOrEmpty(userId));
    	 	                if(!StringUtils.isNullOrEmpty(userId)){
    	 	                	logger.info("---上传图片-2--userId："+userId);
    	 	                	try{
    	 	                		form.setUserId(Long.valueOf(userId));
    	 	                	}catch(Exception e){
    	 	                		logger.error("上传图片格式化错误：", e);
    	 	                	}
    	 	                }
    	 	      	    	form.setUserCode(userCode);
    	 	      	    	byte[] bytes = new byte[byteList.size()];
    	 	      	    	for(int i = 0;i < byteList.size();i++){
    	 	      	    		bytes[i] = byteList.get(i);
    	 	      	    	}
    	 	      	    	form.setContent(bytes);
    	 	      	    	
    	 	      	    	Handler handler = Global.getInstance().getHandler(patchuri);
    	 	      	    	
    	 	      	    	handler.uploadPicture(ctx, form, iskeepAlive);
    	 	      	    	//writeResponse(ctx.channel());
	 	                    readingChunks = false;
    	 	                    reset();
    	 	                }
    	 	            }
    	 	        } else {
    	 	           // writeResponse(ctx.channel());
    	 	        }
                }
	 	        
	    	 }
	        
	    }

	    private void reset() {
	        request = null;
	        decoder.destroy();
	        decoder = null;
	    }

	    /**
	     * Example of reading request by chunk and getting values from chunk to chunk
	     * @throws IOException 
	     */
	    private void readHttpDataChunkByChunk() throws IOException {
	        try {
	            while (decoder.hasNext()) {
	                InterfaceHttpData data = decoder.next();
	                if (data != null) {
	                    // check if current HttpData is a FileUpload and previously set as partial
	                    if (partialContent == data) {
	                    	long len = partialContent.length();
	                        logger.info(" 100% (FinalSize: " + len + ")");
	                        byte[] cope = partialContent.get();
	 	                    
	 	                    for(byte b : cope){
	 	                    	byteList.add(b);
	 	                    }
	                        partialContent = null;
	                    }
	                    try {
	                        // new value
	                        writeHttpData(data);
	                    } finally {
	                        data.release();
	                    }
	                }
	            }
	            // Check partial decoding for a FileUpload
	            InterfaceHttpData data = decoder.currentPartialHttpData();
	            
	            if (data != null) {
	            	boolean fly = true;
	                StringBuilder builder = new StringBuilder();
	                if (partialContent == null) {
	                    partialContent = (HttpData) data;
	                    if (partialContent instanceof FileUpload) {
	                        builder.append("Start FileUpload: ").append(((FileUpload) partialContent).getFilename()).append(" ");
	                    } else {
	                        builder.append("Start Attribute: ").append(partialContent.getName()).append(" ");
	                    }
	                    builder.append("(DefinedSize: ").append(partialContent.definedLength()).append(")");
	                    fly = false;
	                }
	                if (partialContent.definedLength() > 0) {
	                    builder.append(" ").append(partialContent.length() * 100 / partialContent.definedLength()).append("% ");
	                    logger.info(builder.toString());
	                } else {
	                    builder.append(" ").append(partialContent.length()).append(" ");
	                    if(fly){
	                    	
	                    }
	                    logger.info(byteList.size());
	                    logger.info(builder.toString());
	                }
	            }
	        } catch (EndOfDataDecoderException e1) {
	            // end
	          
	        }
	    }

	    private void writeHttpData(InterfaceHttpData data) {
	        if (data.getHttpDataType() == HttpDataType.Attribute) {
	            Attribute attribute = (Attribute) data;
	            String value;
	            try {
	                value = attribute.getValue();
	                String name = attribute.getName();
	                if(name.equals("userId")){
	                	userId = value;
	                }
	                if(name.equals("userCode")){
	                	userCode = value;
	                }
	            } catch (IOException e1) {
	                // Error while reading data from File, only print name and error
	                logger.error("", e1);
	                return;
	            }
	            if (value.length() > 100) {
	              
	            } else {
	            }
	        }else {
	          
	            if (data.getHttpDataType() == HttpDataType.FileUpload) {
	                FileUpload fileUpload = (FileUpload) data;
	                if (fileUpload.isCompleted()) {
	                    if (fileUpload.length() < 10000) {
	                       
	                        try {
	                           
	                        } catch (Exception e1) {
	                            // do nothing for the example
	                            logger.error("", e1);
	                        }
	                       
	                    } else {
	                        
	                    }
	                    // fileUpload.isInMemory();// tells if the file is in Memory
	                    // or on File
	                    // fileUpload.renameTo(dest); // enable to move into another
	                    // File dest
	                    // decoder.removeFileUploadFromClean(fileUpload); //remove
	                    // the File of to delete file
	                } else {
	                   
	                }
	            }
	        }
	    }

	    @SuppressWarnings("unused")
		private void writeResponse(Channel channel) {
	        // Convert the response content to a ChannelBuffer.
	        ByteBuf buf = copiedBuffer("1111111", CharsetUtil.UTF_8);
	      

	        // Decide whether to close the connection or not.
	        boolean close = request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true)
	                || request.protocolVersion().equals(HttpVersion.HTTP_1_0)
	                && !request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE, true);

	        // Build the response object.
	        FullHttpResponse response = new DefaultFullHttpResponse(
	                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
	        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

	        if (!close) {
	            // There's no need to add 'Content-Length' header
	            // if this is the last response.
	            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
	        }

	        Set<Cookie> cookies;
	        String value = request.headers().get(HttpHeaderNames.COOKIE);
	        if (value == null) {
	            cookies = Collections.emptySet();
	        } else {
	            cookies = ServerCookieDecoder.STRICT.decode(value);
	        }
	        if (!cookies.isEmpty()) {
	            // Reset the cookies if necessary.
	            for (Cookie cookie : cookies) {
	                response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
	            }
	        }
	        // Write the response.
	        ChannelFuture future = channel.writeAndFlush(response);
	        // Close the connection after the write operation is done if necessary.
	        if (close) {
	            future.addListener(ChannelFutureListener.CLOSE);
	        }
	    }

	    @SuppressWarnings("unused")
		private void writeMenu(ChannelHandlerContext ctx) {
	       
	        ByteBuf buf = copiedBuffer("3333333333333333333", CharsetUtil.UTF_8);
	        // Build the response object.
	        FullHttpResponse response = new DefaultFullHttpResponse(
	                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

	        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
	        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());

	        // Write the response.
	        ctx.channel().writeAndFlush(response);
	    }

	    @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	       // logger.log(cause);
	    	logger.error("", cause);
			ctx.close();
	    }

}
