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

@Name("fun.voteQuery")
@Scope(ScopeType.PAGE)
public class VoteQuery extends BaseNqtQuery<Assistance> implements
Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@In
	protected EntityManager entityManager;

	public void processRequestParams() {
		getQhb().parm("cxlx", "DT003").setSv("cxlx");
	}

	@Override
	public void executeQueryCounts() {
	}

	public String getQueryDesc() {
		return "投票";
	}

	public VoteQuery() {
		setOrder("o.ct desc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.fun.voteQuery";
	}

	@Override
	public String nameQueryName() {
		return "fun.voteQuery";
	}
}
