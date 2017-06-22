package com.vieking.resource.bean;

import java.io.Serializable;

import com.vieking.sys.exception.ReConst;

public class CollectionBean implements ReConst, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2486878742070430884L;

	/** id */
	private String id;

	/** 收藏人ID */
	private String userId;

	/** 收藏时间 */
	private String scsj;

	/** 新闻ID */
	private String newsId;

	/** 新闻标题 */
	private String newsBt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getScsj() {
		return scsj;
	}

	public void setScsj(String scsj) {
		this.scsj = scsj;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public String getNewsBt() {
		return newsBt;
	}

	public void setNewsBt(String newsBt) {
		this.newsBt = newsBt;
	}

}
