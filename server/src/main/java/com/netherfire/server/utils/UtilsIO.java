package com.netherfire.server.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.netty.buffer.ByteBuf;

public class UtilsIO {

	/**
	 * 解析
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String getString(InputStream in) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buff = new byte[1024];
		int len = 0;
		
		
		
		
		while((len = in.read(buff)) != -1){
			out.write(buff, 0, len);
		}
		String content = new String(out.toByteArray(),"UTF-8");
		out.close();
		in.close();
		return content;
	}
	
	/**
	 * 解析ByteBuf 中的数据
	 * @param byteBuf
	 * @return
	 * @throws IOException
	 */
	public static String byteBufToStr(ByteBuf byteBuf) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		while((len = byteBuf.readableBytes()) > 0){
			byte[] dst = new byte[len];
			byteBuf.readBytes(dst);
			out.write(dst, 0, len);
		}
		out.flush();
		out.close();
		String content = new String(out.toByteArray(),"UTF-8");
		return content;
	}
}
