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

@Name("fun.assistanceQuery")
@Scope(ScopeType.PAGE)
public class AssistanceQuery extends BaseNqtQuery<Assistance> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8259795018899602446L;
	
	@In
	protected EntityManager entityManager;

	public void processRequestParams() {

	}

	@Override
	public void executeQueryCounts() {
	}

	public String getQueryDesc() {
		return "新闻管理";
	}

	public AssistanceQuery() {
		setOrder("o.ct desc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.fun.assistanceQuery";
	}

	@Override
	public String nameQueryName() {
		return "fun.assistanceQuery";
	}

}
