package com.ksign.access.mobile.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDto {
	
	private String cmd;
	private String data;
	
	public enum REQ_TYPE { PUT, GET }
}
