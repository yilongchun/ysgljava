package com.vieking.base;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;

/**
 * 对象选择基础封装模板类 页面导航模式
 * 
 */

public abstract class BaseTreeSelector<T> implements Serializable {
	private static final long serialVersionUID = -3489486730536858686L;

	@Logger
	protected Log log;

	/** 导航模式 */
	/** 返回页面 */
	protected String ret;
	/** 是否已提交 */

	protected boolean submited = false;

	/** 已选择ID */
	protected String selIds;

	/** 单选模式 */
	protected boolean selectOne = false;

	/** 对话框模式 */
	protected boolean popMode = true;

	/** 重绘区域 */
	protected String reRender;

	/** 事件抛出 */
	protected String event;

	protected String title;

	private boolean open = false;

	/**
	 * 选择复选框ajax事件，处理选中或取消操作
	 * 
	 * @param item
	 */
	public void onChange(SelectableItem<T> item) {
		if (selectOne && item.isSelected()) {
			// 如果是单选，则要将已选中的其它项取消
			for (SelectableItem<T> t : selectableItems) {
				if (t.isSelected() && t != item) {
					t.setSelected(false);
				}
			}
		}
	}

	public void select(SelectableItem<T> item) {
		for (SelectableItem<T> t : selectableItems) {
			if (t == item) {
				t.setSelected(true);
			} else {
				t.setSelected(false);
			}
		}
	}

	/**
	 * 开始单个对象选择
	 * 
	 * @param ret
	 *            ，选择结束后需要返回的viewId
	 * @param selIds
	 *            ，已被的对象id字符串，用','分隔
	 * @return，返回选择页面的viewId
	 */
	public String startSelOne(String ret, String selIds) {
		selectOne = true;
		return startSel(ret, selIds);
	}

	/**
	 * 开始多个对象选择
	 * 
	 * @param ret
	 *            ，选择结束后需要返回的viewId
	 * @param selIds
	 *            ，已被的对象id字符串，用','分隔
	 * @return，返回选择页面的viewId
	 */
	public String startSelMany(String ret, String selIds) {
		selectOne = false;
		return startSel(ret, selIds);
	}

	public String startSel(String ret, String selIds) {
		this.ret = ret;
		this.selIds = selIds;
		submited = false;
		return getViewId();
	}

	/**
	 * 抽象方法，返回选择页面的viewId
	 * 
	 * @return
	 */
	protected abstract String getViewId();

	/**
	 * 选择结束
	 * 
	 * @return，选择结束后需要返回的viewId
	 */
	public String ok() {
		if (!popMode) {
			if (ret == null || ret.length() < 1) {
				return "/home.xhtml";
			}
			submited = true;
			return ret;
		} else {
			log.debug("event  {0}", getEvent());
			// Events.instance().raiseEvent(getEvent(), getSelectedList());
			open = false;
			return null;
		}
	}

	/**
	 * 选择取消
	 * 
	 * @return，选择结束后需要返回的viewId
	 */
	public String cancel() {
		if (ret == null || ret.length() < 1) {
			return "/home.xhtml";
		}
		selectableItems = null;// 清空
		return ret;
	}

	/**
	 * 抽象方法，取得对象的标签（名称）
	 * 
	 * @param obj
	 * @return
	 */
	protected abstract String getObjectName(T obj);

	private List<SelectableItem<T>> selectableItems = null;

	public List<SelectableItem<T>> getSelectableItems() {
		return selectableItems;
	}

	public boolean isSubmited() {
		return submited;
	}

	public void setSubmited(boolean submited) {
		this.submited = submited;
	}

	public boolean isSelectOne() {
		return selectOne;
	}

	public boolean isPopMode() {
		return popMode;
	}

	public String getReRender() {
		return reRender;
	}

	public String getEvent() {
		return event;
	}

	public String getTitle() {
		return title;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public void close() {
		this.open = false;
	}

}
