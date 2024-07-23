package com.mkc.mq.domain;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Email implements Serializable {
	
	private static final long serialVersionUID = 348799747125435718L;
	
	public String subject;
	public String text;
	public String to;
	public String from;
	
}
