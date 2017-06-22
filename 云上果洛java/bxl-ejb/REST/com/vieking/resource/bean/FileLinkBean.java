package com.vieking.resource.bean;

import java.io.Serializable;

import com.vieking.sys.exception.ReConst;

public class FileLinkBean implements ReConst, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8012768688954277699L;

	/** id */
	private String id;

	/** 附件类型 */
	private String fjlx;

	/** 附件url */
	private String fjurl;

	/** 时长 */
	private String sc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFjlx() {
		return fjlx;
	}

	public void setFjlx(String fjlx) {
		this.fjlx = fjlx;
	}

	public String getFjurl() {
		return fjurl;
	}

	public void setFjurl(String fjurl) {
		this.fjurl = fjurl;
	}

	public String getSc() {
		return sc;
	}

	public void setSc(String sc) {
		this.sc = sc;
	}

}
