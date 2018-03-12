package com.ksign.access.mobile.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ksign.access.mobile.configure.Configure;
import com.ksign.access.mobile.handler.TokenHandler;
import com.ksign.access.mobile.repository.TokenEntityRepository;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Component
public class NettyServer {

	//@Autowired	TokenHandler tokenHandler;
	@Autowired TokenEntityRepository tokenRepo;
	
	@Autowired Configure conf;
	
	
	public void start() {
		EventLoopGroup parentGroup = new NioEventLoopGroup(1);
		EventLoopGroup childGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap sb = new ServerBootstrap();
			
			sb.group(parentGroup, childGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel sc) throws Exception {
					TokenHandler handler = new TokenHandler();
					handler.setTokenEntityRepository(tokenRepo);
					
					ChannelPipeline cp = sc.pipeline();
					cp.addLast(handler);
				}
			});

			ChannelFuture cf = sb.bind(conf.getMobileServerPort()).sync();
			cf.channel().closeFuture().sync();
			
		} catch(Exception e) {
			e.printStackTrace();
			
		} finally{
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
	}
}
