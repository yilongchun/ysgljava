package com.vieking.resource.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.vieking.sys.exception.ReConst;

@XmlRootElement(name = "assistanceBean")
public class AssistanceBean implements ReConst, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5734108696549901559L;

	/** id */
	private String id;

	/** 发布人 */
	private String user;

	/** 发布时间 */
	private String fbsj;

	/** 标题 */
	private String biaoti;

	/** 内容 */
	private String nerong;

	/** 新闻类型 */
	private String bzlx;

	/** 附件类型 */
	private String fjlx;

	/** 附件路径 */
	private String fjlj;

	/** 浏览次数 */
	private int llcs;

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

	public String getBzlx() {
		return bzlx;
	}

	public void setBzlx(String bzlx) {
		this.bzlx = bzlx;
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

	public int getLlcs() {
		return llcs;
	}

	public void setLlcs(int llcs) {
		this.llcs = llcs;
	}

	public List<FileLinkBean> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileLinkBean> fileList) {
		this.fileList = fileList;
	}

}
