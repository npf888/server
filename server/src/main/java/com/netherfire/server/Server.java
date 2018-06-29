package com.netherfire.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.netherfire.server.db.createid.CreateUserInfoID;
import com.netherfire.server.db.factory.DataProvider;
import com.netherfire.server.handler.core.http.StartServerChannelInitializer;
import com.netherfire.server.redis.RedisProvider;
import com.netherfire.server.server.RechargeServer;

/**
 * 服务器启动类
 * @author 郭君伟
 *
 */
public class Server {

	private static final Logger logger = LogManager.getLogger(Server.class);
	
	public static void main(String[] args) {
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			logger.info("JVM 钩子函数");
		}));
		try {
			Global.getInstance().init();
			RedisProvider.getInstance().init();
			DataProvider.getInstance().init();
			//CreateDeliveryID.getInstance().init();
			CreateUserInfoID.getInstance().init();
			RechargeServer.getInstance().init();
			start();
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	
	
	/**
	 * 启动服务器
	 * @throws InterruptedException 
	 */
	private static void start() throws Exception{
		
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new StartServerChannelInitializer());//http 协议

           Channel ch =  b.bind( Global.getInstance().getConfig().serverIp, Global.getInstance().getConfig().getPort()).sync().channel();
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
	}

	
	

}
