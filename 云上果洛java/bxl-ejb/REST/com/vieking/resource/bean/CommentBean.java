package com.vieking.resource.bean;

import java.io.Serializable;

import com.vieking.sys.exception.ReConst;

/**
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */

public class CommentBean implements ReConst, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1774833815044980365L;

	/** 评论ID */
	private String id;

	/** 评论用户ID */
	private String userid;

	/** 新闻ID */
	private String newsid;

	/** 评论用户 */
	private String name;

	/** 评论时间 */
	private String plsj;

	/** 评论内容 */
	private String pl;

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

	public String getPlsj() {
		return plsj;
	}

	public void setPlsj(String plsj) {
		this.plsj = plsj;
	}

	public String getPl() {
		return pl;
	}

	public void setPl(String pl) {
		this.pl = pl;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getNewsid() {
		return newsid;
	}

	public void setNewsid(String newsid) {
		this.newsid = newsid;
	}

}
