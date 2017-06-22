package com.vieking.functions.manager;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.functions.model.Register;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;

@Name("fun.registerQuery")
@Scope(ScopeType.PAGE)
public class RegisterQuery extends BaseNqtQuery<Register> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6067135821876367940L;
	@In
	protected EntityManager entityManager;

	public void processRequestParams() {

	}

	@Override
	public void executeQueryCounts() {
	}

	public String getQueryDesc() {
		return "用户注册记录";
	}

	public RegisterQuery() {
		setOrder("o.ct desc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.fun.registerQuery";
	}

	@Override
	public String nameQueryName() {
		return "fun.registerQuery";
	}

}
