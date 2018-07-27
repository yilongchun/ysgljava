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

import com.vieking.sys.model.BaseEntity;

/**
 * 投票结果
 * @author yeecolor
 *
 */
@Entity
@Table(name = "VoteResult")
@BatchSize(size = 50)
public class VoteResult extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;
	
	/**
	 * 投票主题
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assistance_id", nullable = true)
	@BatchSize(size = 50)
	private Assistance assistance;
	
	/**
	 * 选项
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "voteItem_id", nullable = true)
	@BatchSize(size = 50)
	private VoteItem voteItem;
	
	/**
	 * 投票人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = true)
	@BatchSize(size = 50)
	private User user;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "VoteResult";
	}

	public Assistance getAssistance() {
		return assistance;
	}

	public void setAssistance(Assistance assistance) {
		this.assistance = assistance;
	}

	public VoteItem getVoteItem() {
		return voteItem;
	}

	public void setVoteItem(VoteItem voteItem) {
		this.voteItem = voteItem;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
