package com.vieking.base;

import java.io.Serializable;

import com.vieking.sys.base.BaseQuery;
import com.vieking.sys.exception.DaoException;

/**
 * 对象选择基础封装模板类 页面导航模式
 * 
 */

public abstract class BaseRop<T> extends BaseQuery<T> implements
		Serializable {
	private static final long serialVersionUID = -3489486730536858686L;

	/** 重绘区域 */
	protected String reRender;

	protected String title;

	private boolean open = false;

	/** 查询列表 */
	public abstract void executeQuery();

	/**
	 * 抽象方法，返回选择页面的viewId
	 * 
	 * @return
	 */
	protected abstract String getViewId();

	public void find() {
		try {
			super.applyFilter();
			super.prepareResults();
		} catch (DaoException e) {

		}
	}

	public void findAll() {
		resetFilter();
		find();
	}

	public String getReRender() {
		return reRender;
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
