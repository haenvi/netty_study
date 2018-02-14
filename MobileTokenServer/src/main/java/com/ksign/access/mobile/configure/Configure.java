package com.ksign.access.mobile.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("ksign")
@Getter
@Setter
public class Configure {
	
	@Value("${mobile.server.port}")
	private int mobileServerPort;
}
