package com.vieking.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.core.Events;

import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.exception.DaoException;

/**
 * 对象选择基础封装模板类 页面导航模式
 * 
 */
public abstract class BaseSelector<T> extends BaseNqtQuery<T> implements
		Serializable {

	private static final long serialVersionUID = -2222478641164435361L;
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

	/** 目标接收对象 Id */
	protected String tagTmpId;

	private boolean open = false;

	protected boolean showtitl = true;

	public StringBuilder sb = new StringBuilder();

	/** 储存所有选择了的对象，替代selecteditem返回页面 */
	public Set<T> selectedSet = new HashSet<T>();

	/**
	 * 选择复选框ajax事件，处理选中或取消操作
	 * 
	 * @param item
	 */
	public void onChange(SelectableItem<T> item) {
		Object o = null;
		if (selectOne && item.isSelected()) {
			// 如果是单选，则要将已选中的其它项取消
			for (SelectableItem<T> t : selectableItems) {
				if (t.isSelected() && t != item) {
					t.setSelected(false);
				}
			}
		}
		if (!selectOne && !item.isSelected()) {
			for (T p : selectedSet) {
				if (p == item.getItem()) {
					// selectedSet.remove(p);
					o = item.getItem();
				}
			}
			if (o != null) {
				selectedSet.remove(o);
			}
		} else if (!selectOne && item.isSelected()) {
			selectedSet.add(item.getItem());
		}
	}

	public void clealPageAll() {
		for (T item : results) {
			for (SelectableItem<T> t : selectableItems) {
				if (t.getId() == item) {
					selectableItems.remove(t);
					break;
				}
			}
		}
	}

	public void selectPageAll() {
		selectedSet.addAll(results);
		initSelectableItems();
	}

	public void clearPageAll() {
		selectedSet.removeAll(results);
		initSelectableItems();
	}

	public void selectAll() throws DaoException {
		exp = true;
		executeQuery();
		exp = false;
		selectedSet.clear();
		selectedSet.addAll(getExpResults());
		initSelectableItems();
	}

	public void clearAll() {
		emptySelected();
		initSelectableItems();
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
			Events.instance().raiseEvent(getEvent(), getSelectedList());
			open = false;
			emptySelected();
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

	/**
	 * 抽象方法，取得对象的Id
	 * 
	 * @param obj
	 * @return
	 */
	protected abstract Object getObjectId(T obj);

	public void find() {
		try {
			super.applyFilter();
			super.prepareResults();
			initSelectableItems();
		} catch (DaoException e) {
			e.printStackTrace();
			facesMessages.add("错误信息：", e.getMessage());
		}
	}

	@Override
	public void next() {
		super.next();
		initSelectableItems();
	}

	@Override
	public void last() {
		super.last();
		initSelectableItems();
	}

	@Override
	public void previous() {
		super.previous();
		initSelectableItems();
	}

	@Override
	public void pageSizeChanged(ValueChangeEvent event) {
		super.pageSizeChanged(event);
		log.debug("pageSizeChanged");
		initSelectableItems();

	}

	@Override
	public void pageChanged(ValueChangeEvent event) {
		super.pageChanged(event);
		log.debug("pageChanged");
		initSelectableItems();
	}

	public void first() {
		super.first();
		initSelectableItems();
	}

	public void findAll() {
		resetFilter();
		find();
	}

	public void findAll(int fl) {
		resetFilter(fl);
		find();
	}

	/**
	 * 取得已被选择的Id字符串，用','分隔
	 * 
	 * @throws DaoException
	 */
	public String getSelectedStr() throws DaoException {
		List<T> lll = getSelectedList();
		sb.setLength(0);
		for (int i = 0; i < lll.size(); i++) {
			sb.append(getObjectName(lll.get(i)));
			if (i != lll.size() - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/** 打开选择器初始化；直接使用取出来数据的对象，可以回显，调用这个方法 ,zm */
	public void initParm(List<T> list) {
		emptySelected();
		if (list != null && list.size() > 0) {
			for (Iterator<T> iter = list.iterator(); iter.hasNext();) {
				T o = iter.next();
				selectedSet.add(o);
			}
		}
	}

	/**
	 * 取得已被选择的对象列表
	 * 
	 * @return
	 * @throws DaoException
	 */
	public List<T> getSelectedList() {
		if (getSelectableItems() == null)
			return null;
		for (SelectableItem<T> item : getSelectableItems()) {
			if (item.isSelected()) {
				selectedSet.add(item.getItem());
			}
		}
		return new ArrayList<T>(selectedSet);
	}

	private List<SelectableItem<T>> selectableItems = null;

	public void initSelectableItems() {
		if (selectableItems == null) {
			selectableItems = new ArrayList<SelectableItem<T>>();
		} else {
			selectableItems.clear();
		}
		List<String> l_selIds = new ArrayList<String>();
		if (selIds != null && selIds.length() > 0) {
			String[] arr = selIds.split(",");
			if (arr != null) {
				for (String id : arr) {
					l_selIds.add(id);
				}
			}
		}
		int i;
		for (T p : results) {
			i = 0;
			for (Iterator<T> iter = selectedSet.iterator(); iter.hasNext();) {
				Object o = iter.next();
				if (o.equals(p)) {
					i = 1;
				}
			}
			if (i == 1) {
				selectableItems.add(new SelectableItem<T>(true, getObjectId(p),
						getObjectName(p), p));
			} else {
				selectableItems.add(new SelectableItem<T>(false,
						getObjectId(p), getObjectName(p), p));
			}
		}
	}

	/** 清空保存选择数据的临时字段 */
	public void emptySelected() {
		sb.setLength(0);
		selectedSet.clear();
	}

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

	public StringBuilder getSb() {
		return sb;
	}

	public void setSb(StringBuilder sb) {
		this.sb = sb;
	}

	public Set<T> getSelectedSet() {
		return selectedSet;
	}

	public void setSelectedSet(Set<T> selectedSet) {
		this.selectedSet = selectedSet;
	}

	public String getTagTmpId() {
		return tagTmpId;
	}

	public void setTagTmpId(String tagTmpId) {
		this.tagTmpId = tagTmpId;
	}

	public boolean isShowtitl() {
		return showtitl;
	}

	public void setShowtitl(boolean showtitl) {
		this.showtitl = showtitl;
	}

}
