package com.vieking.resource.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.vieking.sys.exception.ReConst;

@XmlRootElement(name = "userNoticeBean")
public class UserNoticeBean implements ReConst, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5734108696549901559L;

	/** id */
	private String id;

	/** 通知人 */
	private String user;

	/** 通知时间 */
	private String fbsj;

	/** 标题 */
	private String biaoti;

	/** 内容 */
	private String nerong;

	/** 附件类型 */
	private String fjlx;

	/** 附件路径 */
	private String fjlj;

	/** 读取状态 */
	private int reads;

	private List<FileLinkBean> fileList = new ArrayList<FileLinkBean>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getFbsj() {
		return fbsj;
	}

	public void setFbsj(String fbsj) {
		this.fbsj = fbsj;
	}

	public String getBiaoti() {
		return biaoti;
	}

	public void setBiaoti(String biaoti) {
		this.biaoti = biaoti;
	}

	public String getNerong() {
		return nerong;
	}

	public void setNerong(String nerong) {
		this.nerong = nerong;
	}

	public String getFjlj() {
		return fjlj;
	}

	public void setFjlj(String fjlj) {
		this.fjlj = fjlj;
	}

	public String getFjlx() {
		return fjlx;
	}

	public void setFjlx(String fjlx) {
		this.fjlx = fjlx;
	}

	public List<FileLinkBean> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileLinkBean> fileList) {
		this.fileList = fileList;
	}

	public int getReads() {
		return reads;
	}

	public void setReads(int reads) {
		this.reads = reads;
	}

}
