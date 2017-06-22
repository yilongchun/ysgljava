package com.vieking.resource.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vieking.sys.exception.ReConst;

/**
 * 新闻栏目
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */

public class XinWenLmBean implements ReConst, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -391586533432744500L;

	/** 编号 */
	private String code;

	/** 名称 */
	private String name;

	/** 数值 */
	private int number;

	/** 其他 */
	private String msg;

	/** 图片 */
	private List<ItemValueBean> urlList = new ArrayList<ItemValueBean>();

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

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<ItemValueBean> getUrlList() {
		return urlList;
	}

	public void setUrlList(List<ItemValueBean> urlList) {
		this.urlList = urlList;
	}

}
