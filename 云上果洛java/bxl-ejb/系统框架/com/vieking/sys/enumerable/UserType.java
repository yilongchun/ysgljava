package com.vieking.sys.enumerable;

public enum UserType {
	管理用户(80, "80", "管理用户"), 普通用户(90, "90", "普通用户");
	private final int value;

	private final String desc;

	private final String code;

	private UserType(int value, String code, String desc) {
		this.value = value;
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public int getValue() {
		return value;
	}

}
