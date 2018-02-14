package com.ksign.access.mobile.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("ksign")
@Getter
@Setter
public class Configure {
	private int mobileServerPort;
}
