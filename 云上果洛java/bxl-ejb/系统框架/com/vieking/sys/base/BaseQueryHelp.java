package com.vieking.sys.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.core.Init;

import com.vieking.sys.bean.QueryParamBean;
import com.vieking.sys.enumerable.DataType;

/**
 * <br>
 * <br>
 * BaseQuery 查询Bean 助手工具bean 用于保存 分页参数及 查询条件
 * <p>
 * <a href="BaseQueryHelp.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
public class BaseQueryHelp implements Serializable {

	private static final long serialVersionUID = 7941561227611883684L;

	/** 分页参数 */
	public static final String DIR_ASC = "asc";
	public static final String DIR_DESC = "desc";

	private int pageSize = 5;
	private int page = 0;
	private long recordCount = 0;
	private long totalRecordCount = 0;

	private String orderColumn;
	private String orderDirection;

	private String clearQpn;

	private Map<String, QueryParamBean> qpbs = new HashMap<String, QueryParamBean>();

	public List<QueryParamBean> qpbLs = new ArrayList<QueryParamBean>();

	/*** 传输参数列表 trans param List 用于查询Bean向其他处理传递参数 如多选或单选的数据 Id */
	@SuppressWarnings("rawtypes")
	private List tps = new ArrayList();

	public void clearTps() {
		tps.clear();
	}

	@SuppressWarnings("unchecked")
	public void addTp(Object obj) {
		if (tps.contains(obj)) {
			tps.remove(obj);
		}
		tps.add(obj);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addTps(Collection params) {
		tps.clear();
		tps.addAll(params);
	}

	public QueryParamBean parm(String name) {
		name = StringUtils.trim(name);
		QueryParamBean p = qpbs.get(name);
		if (p == null) {
			p = new QueryParamBean();
			p.setPn(name);
			qpbs.put(name, p);
			return p;
		} else {
			return p;
		}
	}

	public void clearParm() {
		if (!StringUtils.isEmpty(clearQpn)) {
			qpbs.remove(clearQpn);
			qpbLs.clear();
			clearQpn = null;
		}
	}

	public void clearinitParm(String... params) {
		Set<String> key = new TreeSet<String>();
		for (Iterator<String> iter = qpbs.keySet().iterator(); iter.hasNext();) {
			String x = iter.next();
			key.add(x);
		}
		for (int i = 0; i < params.length; i++) {
			for (Iterator<String> it = key.iterator(); it.hasNext();) {
				String s = (String) it.next();
				if (params[i].trim().equals(s)) {
					key.remove(params[i].trim());
					break;
				}
			}
		}

		for (Iterator<String> iter = key.iterator(); iter.hasNext();) {
			String t = iter.next();

			for (Iterator<QueryParamBean> iterator = qpbs.values().iterator(); iterator
					.hasNext();) {
				QueryParamBean qpb = iterator.next();
				if (t.equals(qpb.getPn())) {
					qpb.setValue(null);
				}
			}
		}

	}

	public void clearParm(String _pn) {
		if (!StringUtils.isEmpty(_pn)) {
			qpbs.remove(_pn);
			qpbLs.clear();
		}
	}

	public QueryParamBean tmpParm(String name, String des) {
		QueryParamBean qpb = parm(name);
		qpb.setDes(des);
		qpb.setPl(-100);
		return qpb;
	}

	public QueryParamBean tmpParm(String name) {
		QueryParamBean qpb = parm(name);
		qpb.setPl(-100);
		return qpb;
	}

	public QueryParamBean plParm(String name, int pl) {
		QueryParamBean qpb = parm(name);
		qpb.setPl(pl);
		return qpb;
	}

	public QueryParamBean parm(String name, String dtName) {
		QueryParamBean p = parm(name);
		p.setDt(DataType.valueOf(dtName));
		return p;
	}

	public QueryParamBean parm(String name, String dtName, int pl) {
		QueryParamBean qpb = parm(name, dtName);
		qpb.setPl(pl);
		return qpb;
	}

	public Object pv(String name) {
		name = StringUtils.trim(name);
		QueryParamBean p = qpbs.get(name);
		if (p == null) {
			return null;
		} else {
			return p.getValue();
		}
	}

	public void reset() {
		qpbs.clear();
	}

	public int getOffset() {
		return page * pageSize;
	}

	public void last() {
		if (totalRecordCount <= 0) {
			page = 0;
		} else {
			page = (int) (totalRecordCount % pageSize == 0 ? totalRecordCount
					/ pageSize - 1 : totalRecordCount / pageSize);
		}
	}

	public void next() {
		page++;
	}

	public boolean pageChanged(int _page) {
		if (page != _page) {
			if ((!isPageLastAvailable(_page)) && (_page > 0)) {
				last();
				return true;
			}
			if (!isPageFirstAvailable(_page)) {
				first();
				return true;
			}
			setPage(_page);
			return true;
		} else {
			return false;
		}
	}

	public boolean previous() {
		if (page > 0) {
			page--;
			return true;
		}
		return false;
	}

	public boolean first() {
		if (page > 0) {
			page = 0;
			return true;
		}
		return false;

	}

	public boolean isPageLastAvailable(int _page) {
		return ((_page + 1) * pageSize) < totalRecordCount;
	}

	public boolean isPageFirstAvailable(int _page) {
		return ((_page + 1) * pageSize) < totalRecordCount;
	}

	public boolean isNextAvailable() {
		return ((page + 1) * pageSize) < totalRecordCount;
	}

	public boolean isPreviousAvailable() {
		return totalRecordCount > 0 && page > 0;
	}

	public long getStart() {
		return totalRecordCount != 0 ? page * pageSize + 1 : 0;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	public long getEnd() {
		return page * pageSize + recordCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPage() {

		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}

	public long getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(long totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
		if (!isPageLastAvailable(page)) {
			last();
		}
	}

	public Map<String, QueryParamBean> getQpbs() {
		return qpbs;
	}

	public void setQpbs(Map<String, QueryParamBean> qpbs) {
		this.qpbs = qpbs;
	}

	public void reset(int pl) {
		List<String> pns = new ArrayList<String>();
		for (Iterator<QueryParamBean> iterator = qpbs.values().iterator(); iterator
				.hasNext();) {
			QueryParamBean qpb = iterator.next();
			if (qpb.getPl() < pl) {
				pns.add(qpb.getPn());
			}
		}
		for (Iterator<String> iterator = pns.iterator(); iterator.hasNext();) {
			qpbs.remove(iterator.next());
		}
	}

	public List<QueryParamBean> getQpbLs() {
		qpbLs.clear();
		for (Iterator<QueryParamBean> iterator = qpbs.values().iterator(); iterator
				.hasNext();) {
			QueryParamBean qpb = iterator.next();
			if ((qpb.getPl() < 0) || (qpb.getPl() > 0)
					&& Init.instance().isDebug()) {
				qpbLs.add(qpb);
			}
		}
		return qpbLs;
	}

	public void setQpbLs(List<QueryParamBean> qpbLs) {
		this.qpbLs = qpbLs;
	}

	public String getClearQpn() {
		return clearQpn;
	}

	public void setClearQpn(String clearQpn) {
		this.clearQpn = clearQpn;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getTps() {
		return tps;
	}

}
