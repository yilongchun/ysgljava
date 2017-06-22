package com.vieking.basicdata.model;

/**
 * 查询定义类型 <br>
 * *
 * 
 * <p>
 * <a href="DataType.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 * 
 */
public enum QueryDefineType {
	QDT001("命名查询"), QDT002("查询过滤");

	private final String des;

	private QueryDefineType(String des) {
		this.des = des;
	}

	public String getDes() {
		return des;
	}
}
