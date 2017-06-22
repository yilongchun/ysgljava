package com.vieking.resource.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vieking.sys.exception.ReConst;

/**
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */

public class BmLxrBean implements ReConst, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8860868026282557639L;

	/** 上级部门 code */
	private String bmcode;

	/** 上级部门 */
	private String bumen;

	/** 注册人数 */
	private int zcrs;

	/** 总人数 */
	private int zrs;

	/** 联系人 */
	private List<ContactBean> lxrlist = new ArrayList<ContactBean>();

	/** 同级部门 */
	private List<BuMenBean> bmlist = new ArrayList<BuMenBean>();

	/** 上级部门 */
	private List<BuMenBean> sjbmlist = new ArrayList<BuMenBean>();

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

	public List<BuMenBean> getBmlist() {
		return bmlist;
	}

	public void setBmlist(List<BuMenBean> bmlist) {
		this.bmlist = bmlist;
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

	public List<BuMenBean> getSjbmlist() {
		return sjbmlist;
	}

	public void setSjbmlist(List<BuMenBean> sjbmlist) {
		this.sjbmlist = sjbmlist;
	}

	public List<ContactBean> getLxrlist() {
		return lxrlist;
	}

	public void setLxrlist(List<ContactBean> lxrlist) {
		this.lxrlist = lxrlist;
	}

}
