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
 * 投票选项
 * @author yeecolor
 *
 */
@Entity
@Table(name = "VoteItem")
@BatchSize(size = 50)
public class VoteItem extends BaseEntity {
	
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
	 * 投票选项
	 */
	@Column(length = 200)
	private String item;
	
	/**
	 * 票数
	 */
	private int itemCount;

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
		return "VoteItem";		
	}

	public Assistance getAssistance() {
		return assistance;
	}

	public void setAssistance(Assistance assistance) {
		this.assistance = assistance;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	
	public VoteItem(){
		
	}
	
	public VoteItem(Assistance assistance){
		this.assistance = assistance;
	}

}
