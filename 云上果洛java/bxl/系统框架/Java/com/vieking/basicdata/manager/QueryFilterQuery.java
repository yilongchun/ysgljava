package com.vieking.basicdata.manager;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;

import com.vieking.basicdata.model.QueryFilter;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;
import com.vieking.sys.exception.Const;

@Name("sys.queryFilterQuery")
@Scope(ScopeType.PAGE)
public class QueryFilterQuery extends BaseNqtQuery<QueryFilter> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7143583542160447428L;

	@Observer(create = false, value = { "queryFilter_persisted",
			"queryFilter_removed" })
	public void imRefresh() {
		super.applyFilter();
	}

	@Observer(create = false, value = "queryFilter_updated")
	public void updateRefresh() {
		super.updateRefresh();
	}

	@Transactional
	public void deleteItem(Object objId) {
		QueryFilter eo = entityManager.find(QueryFilter.class, objId);
		if (eo.readOnly()) {
			getFacesMessages().add("数据处于锁定状态，不能删除！");
			return;
		}
		try {
			entityManager.remove(eo);
			entityManager.flush();
			Events.instance().raiseEvent(Const.NAMEQUERYCHANGED,
					eo.getNameQuery().getName());
			resetQuery();
		} catch (Exception e) {
			e.printStackTrace();
			getFacesMessages().add("删除数据失败！存在其他数据关联！");
		}

	}

	public QueryFilterQuery() {
		super();
		setOrder("o.sort asc ");
	}

	@Override
	public String getQueryDesc() {
		return "查询过滤条件";
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.sys.QueryFilter";
	}

	@Override
	public String nameQueryName() {

		return "sys.QueryFilter";
	}

	@Override
	public void processRequestParams() {

	}
}
