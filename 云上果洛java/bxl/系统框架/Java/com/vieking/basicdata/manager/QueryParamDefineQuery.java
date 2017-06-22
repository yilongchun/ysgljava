package com.vieking.basicdata.manager;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;

import com.vieking.basicdata.model.QueryParamDefine;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;

@Name("sys.queryParamDefineQuery")
@Scope(ScopeType.PAGE)
public class QueryParamDefineQuery extends BaseNqtQuery<QueryParamDefine>
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7143583542160447428L;

	@Override
	public void executeQueryCounts() {
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public FacesMessages getFacesMessages() {

		return facesMessages;
	}

	@Override
	public String getPageSizeCookiesName() {
		return "pageSize";
	}

	@Override
	protected boolean isPostback() {
		return false;
	}

	@Override
	public void resetFilter() {
		getQhb().reset();
		applyFilter();
	}

	public QueryParamDefineQuery() {
		super();
		setOrder("o.sort asc ");
	}

	@Override
	public String getQueryDesc() {
		return "查询过滤参数";
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.sys.QueryParamDefine";
	}

	@Override
	public String nameQueryName() {
		return "sys.QueryParamDefine";
	}

	@Override
	public void processRequestParams() {

	}
}
