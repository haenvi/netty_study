package haenvi.study.netty.server;


import org.apache.log4j.BasicConfigurator;

import haenvi.study.netty.handler.TestHandler;
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

public class NettyServer {
	
	private static final int _PORT = 9090;
	
	public static void main(String args[]) {

		BasicConfigurator.configure();
		
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
					ChannelPipeline cp = sc.pipeline();
					//TODO:
					cp.addLast(new TestHandler());
				}
			});

			ChannelFuture cf = sb.bind(_PORT).sync();
			cf.channel().closeFuture().sync();
			
		} catch(Exception e) {
			e.printStackTrace();
			
		} finally{
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
	}
}
