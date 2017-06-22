package com.vieking.sys.enumerable;

/**
 * 验证类型 <br>
 * *
 * 
 * <p>
 * <a href="DataState.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 * 
 */
public enum VerificationType {
	警告("100", "警告"),

	阻止("200", "阻止");

	private final String desc;

	private final String code;

	private VerificationType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public String getCode() {
		return code;
	}

}
