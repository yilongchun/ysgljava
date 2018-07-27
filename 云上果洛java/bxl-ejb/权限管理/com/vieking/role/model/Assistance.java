package com.vieking.role.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.basicdata.model.Dictionary;
import com.vieking.functions.model.UserNotice;
import com.vieking.sys.model.BaseEntity;

/**
 * news <br>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Assistance")
@BatchSize(size = 50)
public class Assistance extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5322429944860782376L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 发布人 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = true)
	@BatchSize(size = 50)
	private User user;

	/** 发布人姓名 */
	@Column(length = 30)
	private String namea;

	/** 发布时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fbsj", nullable = true, unique = false)
	private Calendar fbsj;

	/** 内容标题 */
	@Column(length = 200, unique = true)
	private String biaoti;

	/** 副标题 **/
	@Column(length = 200)
	private String subTitle;

	/** 内容 */
	@Column(length = 2000)
	private String nerong;

	/** 浏览次数 */
	private int llcs = 0;

	/** 点赞数 **/
	private int zanCount = 0;

	/** 新闻类型 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bzlx", nullable = true)
	@BatchSize(size = 50)
	private Dictionary bzlx;

	/** 附件类型 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fjlx", nullable = true)
	@BatchSize(size = 50)
	private Dictionary fjlx;

	/** 内容 */
	@Column(length = 500)
	private String videoUrl;

	/** 区县 */
	@Column(length = 40)
	private String distrs;

	/** 用户消息通知 */
	@OneToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "assistance")
	@BatchSize(size = 100)
	@OrderBy("isreads asc")
	private List<UserNotice> userNotices = new ArrayList<UserNotice>();
	
	/** 投票列表 */
	@OneToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "assistance")
	@BatchSize(size = 100)
//	@OrderBy("isreads asc")
	private List<VoteItem> voteItems = new ArrayList<VoteItem>();

	@Override
	public String toString() {
		return "Assistance [user=" + user + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getNamea() {
		return namea;
	}

	public void setNamea(String namea) {
		this.namea = namea;
	}

	public Calendar getFbsj() {
		return fbsj;
	}

	public void setFbsj(Calendar fbsj) {
		this.fbsj = fbsj;
	}

	public String getBiaoti() {
		return biaoti;
	}

	public void setBiaoti(String biaoti) {
		this.biaoti = biaoti;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public Dictionary getBzlx() {
		return bzlx;
	}

	public void setBzlx(Dictionary bzlx) {
		this.bzlx = bzlx;
	}

	public String getNerong() {
		return nerong;
	}

	public void setNerong(String nerong) {
		this.nerong = nerong;
	}

	public Dictionary getFjlx() {
		return fjlx;
	}

	public void setFjlx(Dictionary fjlx) {
		this.fjlx = fjlx;
	}

	public int getLlcs() {
		return llcs;
	}

	public void setLlcs(int llcs) {
		this.llcs = llcs;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public int getZanCount() {
		return zanCount;
	}

	public void setZanCount(int zanCount) {
		this.zanCount = zanCount;
	}

	public List<UserNotice> getUserNotices() {
		return userNotices;
	}

	public void setUserNotices(List<UserNotice> userNotices) {
		this.userNotices = userNotices;
	}

	public String getDistrs() {
		return distrs;
	}

	public void setDistrs(String distrs) {
		this.distrs = distrs;
	}

	public List<VoteItem> getVoteItems() {
		return voteItems;
	}

	public void setVoteItems(List<VoteItem> voteItems) {
		this.voteItems = voteItems;
	}
	
	

}
