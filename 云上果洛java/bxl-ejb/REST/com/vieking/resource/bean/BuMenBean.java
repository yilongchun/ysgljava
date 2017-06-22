package com.vieking.resource.bean;

import java.io.Serializable;

import com.vieking.sys.exception.ReConst;

/**
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */

public class BuMenBean implements ReConst, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1774833815044980365L;

	/** 部门 code */
	private String bmcode;

	/** 部门名称 */
	private String bumen;

	/** 全称 */
	private String wn;

	/** 全称code */
	private String wncode;

	/** 上级编号 */
	private String superCode;

	/** 注册人数 */
	private int zcrs;

	/** 总人数 */
	private int zrs;

	public String getBmcode() {
		return bmcode;
	}

	public void setBmcode(String bmcode) {
		this.bmcode = bmcode;
	}

	public String getBumen() {
		return bumen;
	}

	public void setBumen(String bumen) {
		this.bumen = bumen;
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

	public int getZcrs() {
		return zcrs;
	}

	public void setZcrs(int zcrs) {
		this.zcrs = zcrs;
	}

	public int getZrs() {
		return zrs;
	}

	public void setZrs(int zrs) {
		this.zrs = zrs;
	}

	public String getWncode() {
		return wncode;
	}

	public void setWncode(String wncode) {
		this.wncode = wncode;
	}

}
