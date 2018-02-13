package com.ksign.access.mobile.handler;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ksign.access.mobile.dto.RequestDto;
import com.ksign.access.mobile.entity.TokenEntity;
import com.ksign.access.mobile.repository.TokenEntityRepository;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable 
public class TokenHandler extends ChannelInboundHandlerAdapter {
	private Logger log = Logger.getLogger(getClass());
	private String lhead = "[TokenHandler." + this.hashCode() + "] ";
	
	TokenEntityRepository tokenRepo;
	public void setTokenEntityRepository(TokenEntityRepository tokenRepo) {
		this.tokenRepo = tokenRepo;
	}

	// 채널 읽는 것을 완료했을 때 동작할 코드를 정의 합니다.
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	};
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace(); // 쌓여있는 트레이스를 출력합니다.
		ctx.close(); // 컨텍스트를 종료시킵니다.
	}
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf b = (ByteBuf) msg;
		String reqJson = b.toString(Charset.defaultCharset());
		Channel c = ctx.channel();

		Gson gson = new GsonBuilder().disableHtmlEscaping().create();

		HashMap<String, String> respMap = new HashMap<String, String>();
		
		log.info(lhead + ((InetSocketAddress) c.remoteAddress()).getAddress().getHostAddress() + ", readJson: " + reqJson);
		RequestDto req = gson.fromJson(reqJson, RequestDto.class);

		if(RequestDto.REQ_TYPE.PUT.name().equals(req.getCmd())) {
			String tokenIdx = UUID.randomUUID().toString();
			TokenEntity entity = new TokenEntity();
			
			entity.setTokenIdx(tokenIdx);
			entity.setToken(req.getData());
			entity.setGenTime(new Date(System.currentTimeMillis()));
			
			tokenRepo.save(entity);
			respMap.put("tokenId", tokenIdx);
			
		} else if(RequestDto.REQ_TYPE.GET.name().equals(req.getCmd())) {
			TokenEntity entity = tokenRepo.findOne(req.getData());
			
			if(entity != null) {
				respMap.put("basicToken", entity.getToken());
				tokenRepo.delete(req.getData());
				
			} else {
				respMap.put("basicToken", "");
			}
			
			
		} else {
			//error
		}

		String respData =  gson.toJson(respMap).toString();
		
		log.info(lhead + "respData:" + respData);

		byte[] result = respData.getBytes();
		ByteBuf f = ctx.alloc().buffer(result.length);
		f.writeBytes(result);

		c.writeAndFlush(f);
		c.close();		
	}
}
