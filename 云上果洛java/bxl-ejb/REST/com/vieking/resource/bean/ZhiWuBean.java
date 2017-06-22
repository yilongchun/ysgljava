package com.vieking.resource.bean;

import java.io.Serializable;

import com.vieking.sys.exception.ReConst;

/**
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */

public class ZhiWuBean implements ReConst, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -605712218449435706L;
	/** 职务 code */
	private String zwcode;
	/** 职务 */
	private String zhiwu;

	public String getZwcode() {
		return zwcode;
	}

	public void setZwcode(String zwcode) {
		this.zwcode = zwcode;
	}

	public String getZhiwu() {
		return zhiwu;
	}

	public void setZhiwu(String zhiwu) {
		this.zhiwu = zhiwu;
	}

}
