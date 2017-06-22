package com.vieking.functions.manager;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.basicdata.model.Department;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;

@Name("fun.departmentQuery")
@Scope(ScopeType.PAGE)
public class DepartmentQuery extends BaseNqtQuery<Department> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2642373387565307864L;

	@Override
	public void executeQueryCounts() {
	}

	public String getQueryDesc() {
		return "单位部门职务管理";
	}

	public DepartmentQuery() {
		// setOrder("o.code asc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.fun.departmentQuery";
	}

	@Override
	public String nameQueryName() {
		return "fun.departmentQuery";
	}

	@Override
	public void processRequestParams() {
		// TODO Auto-generated method stub

	}
}
