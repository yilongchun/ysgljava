package com.vieking.functions.manager;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.functions.model.DepartmentPost;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;

@Name("fun.departmentPostQuery")
@Scope(ScopeType.PAGE)
public class DepartmentPostQuery extends BaseNqtQuery<DepartmentPost> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1229533967689744679L;
	@In
	protected EntityManager entityManager;

	public void processRequestParams() {

	}

	@Override
	public void executeQueryCounts() {
	}

	public String getQueryDesc() {
		return "职务管理";
	}

	public DepartmentPostQuery() {
		setOrder("o.ct desc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.fun.departmentPostQuery";
	}

	@Override
	public String nameQueryName() {
		return "fun.departmentPostQuery";
	}

}
