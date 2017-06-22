package com.vieking.resource.bean;

import java.io.Serializable;

import com.vieking.sys.exception.ReConst;

public class RegisterBean implements ReConst, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6445278896340247486L;

	private String id;

	/** 注册电话 */
	private String phone;

	/** 注册密码 */
	private String pwd;

	/** 验证码 */
	private String yzm;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getYzm() {
		return yzm;
	}

	public void setYzm(String yzm) {
		this.yzm = yzm;
	}

}
