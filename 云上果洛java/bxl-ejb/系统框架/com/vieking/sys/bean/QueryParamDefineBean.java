package com.vieking.sys.bean;

import java.io.Serializable;

import com.vieking.sys.enumerable.DataType;

/**
 * <p>
 * 查询参数定义Bean <a href="QueryParamDefineBean.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
public class QueryParamDefineBean implements Serializable {

	private static final long serialVersionUID = 8192654945263372954L;

	/** 参数名称 */
	private String name;

	/** 参数说明 */
	private String des;

	/** 参数类型 */
	private DataType type;

	/** 参数值表达式 */
	private String valueEL;

	/** 默认值 */
	private String defineValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public String getValueEL() {
		return valueEL;
	}

	public void setValueEL(String valueEL) {
		this.valueEL = valueEL;
	}

	public String getDefineValue() {
		return defineValue;
	}

	public void setDefineValue(String defineValue) {
		this.defineValue = defineValue;
	}

	@Override
	public String toString() {
		return "QueryParamDefineBean [name=" + name + ", valueEL=" + valueEL
				+ "]";
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public QueryParamDefineBean(String name, String des, DataType type,
			String valueEL, String defineValue) {
		super();
		this.name = name;
		this.des = des;
		this.type = type;
		this.valueEL = valueEL;
		this.defineValue = defineValue;
	}

	public QueryParamDefineBean() {
		super();
	}

}
