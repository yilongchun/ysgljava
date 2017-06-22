package com.vieking.role.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.basicdata.model.Dictionary;
import com.vieking.sys.model.BaseEntity;

/**
 * 会员积分 <br>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "UserScore")
@BatchSize(size = 50)
public class UserScore extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4131999217579614914L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = true)
	@BatchSize(size = 50)
	private User user;

	/** 积分类型 （增、减） */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jflx", nullable = true)
	@BatchSize(size = 50)
	private Dictionary jflx;

	/** 积分来源（系统赠送、交易） */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jfly", nullable = true)
	@BatchSize(size = 50)
	private Dictionary jfly;

	/** 积分 */
	@Column
	private int jf;

	@Override
	public String toString() {
		return "UserScore [user=" + user + "]";
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

	public Dictionary getJflx() {
		return jflx;
	}

	public void setJflx(Dictionary jflx) {
		this.jflx = jflx;
	}

	public Dictionary getJfly() {
		return jfly;
	}

	public void setJfly(Dictionary jfly) {
		this.jfly = jfly;
	}

	public int getJf() {
		return jf;
	}

	public void setJf(int jf) {
		this.jf = jf;
	}

}
