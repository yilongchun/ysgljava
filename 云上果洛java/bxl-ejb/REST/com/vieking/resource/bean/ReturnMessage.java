package com.vieking.resource.bean;

import java.io.Serializable;

import com.vieking.sys.exception.ReConst;

/**
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
public class ReturnMessage implements ReConst, Serializable {

	private static final long serialVersionUID = 2186254500122122720L;

	private String coding;

	private String msg;

	public String getCoding() {
		return coding;
	}

	public void setCoding(String coding) {
		this.coding = coding;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ReturnMessage() {
		super();
	}

	public ReturnMessage(String msg) {
		super();
		this.msg = msg;
	}

	public ReturnMessage(String coding, String msg) {
		super();
		this.coding = coding;
		this.msg = msg;
	}
}
