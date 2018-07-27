package com.vieking.resource.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


import com.vieking.sys.exception.ReConst;

@XmlRootElement(name = "voteItemBean")
public class VoteItemBean implements ReConst, Serializable{

	private String id;
	
	private String item;
	
	private int itemCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	
	
}
