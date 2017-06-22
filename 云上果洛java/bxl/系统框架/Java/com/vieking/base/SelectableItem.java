package com.vieking.base;

import java.io.Serializable;

/**
 * 可选择对象封装模板
 * 
 * @author happydev
 * 
 * @param <T>
 */
public class SelectableItem<T> implements Serializable {

	private static final long serialVersionUID = -8948678631515215371L;

	public SelectableItem(boolean selected, Object id, String itemInfo, T item) {
		this.selected = selected;
		this.id = id;
		this.itemInfo = itemInfo;
		this.item = item;
	}

	/**
	 * 是否已被选择
	 */
	private boolean selected = false;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	private Object id;

	private String itemInfo;

	private T item;

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public String getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(String itemInfo) {
		this.itemInfo = itemInfo;
	}

	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
	}

}
