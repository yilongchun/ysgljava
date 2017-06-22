package com.vieking.resource.bean;

import java.io.Serializable;

import com.vieking.sys.exception.ReConst;

/**
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */

public class ItemValueBean implements ReConst, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4522188285236862108L;

	/** ID */
	private String id;

	/** 值 */
	private String vaule;

	/** 数值 */
	private int number;

	/** 图片地址 */
	private String imgUrl;

	/** 其他 */
	private String msg;

	public ItemValueBean() {
	}

	public ItemValueBean(String msg, int number) {
		this.msg = msg;
		this.number = number;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVaule() {
		return vaule;
	}

	public void setVaule(String vaule) {
		this.vaule = vaule;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
