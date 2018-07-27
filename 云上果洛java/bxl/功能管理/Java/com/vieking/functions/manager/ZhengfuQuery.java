package com.vieking.functions.manager;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.role.model.Assistance;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;

@Name("fun.zhengfuQuery")
@Scope(ScopeType.PAGE)
public class ZhengfuQuery extends BaseNqtQuery<Assistance> implements
Serializable{

	@In
	protected EntityManager entityManager;

	public void processRequestParams() {
		getQhb().parm("cxlx", "DT003").setSv("cxlx");
	}

	@Override
	public void executeQueryCounts() {
	}

	public String getQueryDesc() {
		return "服务管理";
	}

	public ZhengfuQuery() {
		setOrder("o.ct desc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.fun.zhengfuQuery";
	}

	@Override
	public String nameQueryName() {
		return "fun.zhengfuQuery";
	}
}
