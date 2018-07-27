package com.vieking.functions.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.basicdata.model.Dictionary;
import com.vieking.role.model.Assistance;
import com.vieking.role.model.User;
import com.vieking.sys.model.BaseEntity;

/**
 * 新闻评论
 * 
 * @author yeecolor
 * 
 */
@Entity
@Table(name = "Comment")
@BatchSize(size = 50)
public class Comment extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 评论用户 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = true)
	@BatchSize(size = 50)
	private User user;

	/** 评论新闻 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assistance", nullable = true)
	@BatchSize(size = 50)
	private Assistance assistance;

	/** 评论时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "pjsj", nullable = true, unique = false)
	private Calendar plsj;

	/** 评论类型 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pllxId", nullable = true)
	@BatchSize(size = 50)
	private Dictionary pllx;

	/** 评论内容 */
	@Column(length = 500, nullable = true)
	private String plnr;

	/** 点赞 */
	@Column
	private boolean zan;

	@Override
	public String toString() {
		return "Comment [user=" + user + "]";
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

	public Assistance getAssistance() {
		return assistance;
	}

	public void setAssistance(Assistance assistance) {
		this.assistance = assistance;
	}

	public Calendar getPlsj() {
		return plsj;
	}

	public void setPlsj(Calendar plsj) {
		this.plsj = plsj;
	}

	public boolean isZan() {
		return zan;
	}

	public void setZan(boolean zan) {
		this.zan = zan;
	}

	public Dictionary getPllx() {
		return pllx;
	}

	public void setPllx(Dictionary pllx) {
		this.pllx = pllx;
	}

	public String getPlnr() {
		return plnr;
	}

	public void setPlnr(String plnr) {
		this.plnr = plnr;
	}

}
