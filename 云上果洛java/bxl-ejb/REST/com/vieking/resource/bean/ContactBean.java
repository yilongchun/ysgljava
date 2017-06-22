package com.vieking.resource.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vieking.sys.exception.ReConst;

/**
 * 通讯录
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */

public class ContactBean implements ReConst, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3097625093487791247L;

	/** 通讯录联系人ID */
	private String id;

	/** 通讯录联系人 */
	private String name;

	/** 手机号 */
	private String phone;

	/** 职务 */
	private String zhiwustr;

	/** 头像图片url */
	private String imgUrl;

	/** 邮箱地址 */
	private String email;

	/** 是否注册 0未注册,1已注册 */
	private int zhuce;

	/** 简介 */
	private String jianjie;
	
	/** 权限级别 **/
	private String level;

	/** 部门 */
	private List<BuMenBean> bmlist = new ArrayList<BuMenBean>();

	/** 职务 */
	private List<ZhiWuBean> zwlist = new ArrayList<ZhiWuBean>();
	
	/*职务*/
	private String post;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getZhuce() {
		return zhuce;
	}

	public void setZhuce(int zhuce) {
		this.zhuce = zhuce;
	}

	public List<BuMenBean> getBmlist() {
		return bmlist;
	}

	public void setBmlist(List<BuMenBean> bmlist) {
		this.bmlist = bmlist;
	}

	public List<ZhiWuBean> getZwlist() {
		return zwlist;
	}

	public void setZwlist(List<ZhiWuBean> zwlist) {
		this.zwlist = zwlist;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJianjie() {
		return jianjie;
	}

	public void setJianjie(String jianjie) {
		this.jianjie = jianjie;
	}

	public String getZhiwustr() {
		return zhiwustr;
	}

	public void setZhiwustr(String zhiwustr) {
		this.zhiwustr = zhiwustr;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}
	
	
}
