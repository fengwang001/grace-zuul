package com.qiyi.zuul.constant;

public enum HeaderConstant {

	HEADER_TOKEN("Authorization");
	
	private String value;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private HeaderConstant(String val){
		this.value = val;
	}
	
}
