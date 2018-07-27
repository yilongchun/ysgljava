package com.vieking.resource.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vieking.sys.exception.ReConst;

/**
 * 用户信息
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */

public class UserBean implements ReConst, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7713104180379091390L;

	/** 用户 ID */
	private String id;

	/** 登录名 */
	private String loginName;

	/** 用户名 */
	private String name;

	/** 性别 */
	private String sex;

	/** 职务 */
	private String zhiwustr;

	/** 手机号 */
	private String phone;

	/** 头像图片url */
	private String imgUrl;

	/** 邮箱地址 */
	private String email;

	/** 简介 */
	private String jianjie;

	/** 部门 */
	private List<BuMenBean> bmlist = new ArrayList<BuMenBean>();

	/** 职务 */
	private List<ZhiWuBean> zwlist = new ArrayList<ZhiWuBean>();
	/** 积分 */
	private int score;

	/** 注册用户类型 */
	private String userType;

	/** 第三方ID */
	private String thirdId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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

	public String getZhiwustr() {
		for (Iterator<ZhiWuBean> iterator = zwlist.iterator(); iterator
				.hasNext();) {
			ZhiWuBean zhiWuBean = iterator.next();
			zhiwustr = zhiWuBean.getZhiwu() + ",";
		}
		return zhiwustr;
	}

	public void setZhiwustr(String zhiwustr) {
		this.zhiwustr = zhiwustr;
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getThirdId() {
		return thirdId;
	}

	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}

}
