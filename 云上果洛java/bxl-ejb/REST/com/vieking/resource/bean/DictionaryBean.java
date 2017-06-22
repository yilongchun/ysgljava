package com.vieking.resource.bean;

import java.io.Serializable;

import com.vieking.sys.exception.ReConst;

/**
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
public class DictionaryBean implements ReConst, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -391586533432744500L;

	/** ID */
	private String id;

	/** 编号 */
	private String code;

	/** 名称 */
	private String name;

	/** 全称 */
	private String wn;

	/** 上级编号 */
	private String superCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWn() {
		return wn;
	}

	public void setWn(String wn) {
		this.wn = wn;
	}

	public String getSuperCode() {
		return superCode;
	}

	public void setSuperCode(String superCode) {
		this.superCode = superCode;
	}

}
