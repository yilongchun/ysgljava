package com.vieking.resource.bean;

import java.io.Serializable;

import com.vieking.sys.exception.ReConst;

/**
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
public class NumberMessage implements ReConst, Serializable {

	private static final long serialVersionUID = 2186254500122122720L;

	private String coding;

	private String times;

	public String getCoding() {
		return coding;
	}

	public void setCoding(String coding) {
		this.coding = coding;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public NumberMessage() {
		super();
	}

	public NumberMessage(String times) {
		super();
		this.times = times;
	}

	public NumberMessage(String coding, String times) {
		super();
		this.coding = coding;
		this.times = times;
	}
}
