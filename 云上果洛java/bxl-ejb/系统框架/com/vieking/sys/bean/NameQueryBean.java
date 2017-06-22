package com.vieking.sys.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 命名查询Bean 用于改善查询性能 <a href="NameQueryBean.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
public class NameQueryBean implements Serializable {

	private static final long serialVersionUID = 4885945993524889304L;

	/** 查询 语句 */
	private String ql;

	/** 查询数量 语句 */
	private String qlCount;

	/** 查询统计数据语句 */
	private String qlCounts;

	/** 是否使用数据库 sql查询 */
	private boolean userNativeQuery = false;

	/** 默认排序 */
	private String orderBy;

	/** 查询参数 */
	private Map<String, QueryParamDefineBean> qpds = new HashMap<String, QueryParamDefineBean>();

	public void reset() {
		ql = null;
		userNativeQuery = false;
		orderBy = null;
		qlCount = null;
		qlCounts = null;
		qpds.clear();
	}

	public boolean isNeedInit() {
		return StringUtils.isEmpty(ql);
	}

	public String getQl() {
		return ql;
	}

	public void setQl(String ql) {
		this.ql = ql;
	}

	public String getQlCount() {
		return qlCount;
	}

	public void setQlCount(String qlCount) {
		this.qlCount = qlCount;
	}

	public String getQlCounts() {
		return qlCounts;
	}

	public void setQlCounts(String qlCounts) {
		this.qlCounts = qlCounts;
	}

	public boolean isUserNativeQuery() {
		return userNativeQuery;
	}

	public void setUserNativeQuery(boolean userNativeQuery) {
		this.userNativeQuery = userNativeQuery;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public void setQpds(Map<String, QueryParamDefineBean> qpds) {
		this.qpds = qpds;
	}

	public Map<String, QueryParamDefineBean> getQpds() {
		return qpds;
	}

	public void putQpd(String name, QueryParamDefineBean bean) {
		if (getQpds().get(name) != null) {
			getQpds().remove(name);
		}
		getQpds().put(name, bean);
	}

}
