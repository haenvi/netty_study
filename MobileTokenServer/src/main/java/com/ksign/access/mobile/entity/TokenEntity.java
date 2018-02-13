package com.ksign.access.mobile.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = { "token" })
@Entity
public class TokenEntity {
	@Id
	private String tokenIdx;
	private String token;
	
	private Date genTime;
}



