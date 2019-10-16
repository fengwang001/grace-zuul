package com.qiyi.zuul.constant;

import java.util.Calendar;

public enum TokenConstant {

	/** token 过期时间: 2 小时 */
	CALENDAR_FIELD("field",Calendar.HOUR_OF_DAY),
	
	CALENDAR_INTERVAL("interval",2),
	/** token 过期 */
	TOKEN_OUT("out",501);
	
	private String name;
	
	private Integer value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
	private TokenConstant(String name, Integer value){
		this.name = name;
		this.value = value;
	}
	
}
