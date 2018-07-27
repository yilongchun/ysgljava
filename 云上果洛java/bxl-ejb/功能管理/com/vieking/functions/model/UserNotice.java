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

import com.vieking.role.model.Assistance;
import com.vieking.role.model.User;
import com.vieking.sys.model.BaseEntity;

/**
 * 用户通知
 * 
 * @author wj
 * 
 */
@Entity
@Table(name = "UserNotice")
@BatchSize(size = 50)
public class UserNotice extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 用户 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = true)
	@BatchSize(size = 50)
	private User user;

	/** 通知 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assistance_id", nullable = true)
	@BatchSize(size = 50)
	private Assistance assistance;

	/** 通知时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tzsj", nullable = true)
	private Calendar tzsj;

	/** 读取 0未读 1已读 */
	@Column(name = "isreads")
	private int isreads = 0;

	public UserNotice() {
		super();
	}

	public UserNotice(User user, Assistance assistance) {
		super();
		this.user = user;
		this.assistance = assistance;
	}

	@Override
	public String toString() {
		return "UserNotice [user=" + user + "]";
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

	public Calendar getTzsj() {
		return tzsj;
	}

	public void setTzsj(Calendar tzsj) {
		this.tzsj = tzsj;
	}

	public int getIsreads() {
		return isreads;
	}

	public void setIsreads(int isreads) {
		this.isreads = isreads;
	}
 
	 

}
