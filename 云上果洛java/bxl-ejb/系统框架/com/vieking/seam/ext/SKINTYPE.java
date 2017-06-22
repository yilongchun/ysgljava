package com.vieking.seam.ext;

public enum SKINTYPE {
	plain("plain", "100", "普通"), emeraldTown("emeraldTown", "101", "翡翠绿"), blueSky(
			"blueSky", "102", "蓝色天空"), wine("wine", "103", "紫红色"), japanCherry(
			"japanCherry", "104", "日本樱花"), ruby("ruby", "105", "红宝石"), classic(
			"classic", "106", "经典"), laguna("laguna", "107", "湖蓝色"), deepMarine(
			"deepMarine", "108", "深海洋");
	private final String value;

	private final String desc;

	private final String code;

	private SKINTYPE(String value, String code, String desc) {
		this.value = value;
		this.code = code;
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public String getCode() {
		return code;
	}

}
