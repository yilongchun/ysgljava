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
 * 新闻收藏 <br>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Collection")
@BatchSize(size = 50)
public class Collection extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1012307891430305325L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 收藏用户 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = true)
	@BatchSize(size = 50)
	private User user;

	/** 收藏新闻 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assistance", nullable = true)
	@BatchSize(size = 50)
	private Assistance assistance;

	/** 收藏时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "scsj", nullable = true, unique = false)
	private Calendar scsj;

	@Override
	public String toString() {
		return "Collection [user=" + user + "]";
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

	public Calendar getScsj() {
		return scsj;
	}

	public void setScsj(Calendar scsj) {
		this.scsj = scsj;
	}

}
