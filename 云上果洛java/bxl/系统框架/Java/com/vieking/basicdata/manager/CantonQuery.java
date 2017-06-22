package com.vieking.basicdata.manager;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.basicdata.model.Canton;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;

@Name("sys.cantonQuery")
@Scope(ScopeType.PAGE)
public class CantonQuery extends BaseNqtQuery<Canton> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5731204016246976619L;

	@Override
	public void executeQueryCounts() {
	}

	public String getQueryDesc() {
		return "行政区划管理";
	}

	public CantonQuery() {
	    //	setOrder("o.code asc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.sys.canton";
	}

	@Override
	public String nameQueryName() {
		return "sys.canton";
	}

	@Override
	public void processRequestParams() {
		// TODO Auto-generated method stub

	}
}
