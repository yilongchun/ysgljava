package com.vieking.functions.manager;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.basicdata.model.Department;
import com.vieking.role.model.Assistance;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;

@Name("fun.userNoticeQuery")
@Scope(ScopeType.PAGE)
public class UserNoticeQuery extends BaseNqtQuery<Assistance> implements
		Serializable {

  
	/**
	 * 
	 */
	private static final long serialVersionUID = 7676137913559838169L;
	@In
	protected EntityManager entityManager;

	
	
	public void processRequestParams() {
		 
	}

	@Override
	public void executeQueryCounts() {
	}

	public String getQueryDesc() {
		return "通知管理";
	}

	public UserNoticeQuery() {
		setOrder("o.ct desc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.fun.userNoticeQuery";
	}

	@Override
	public String nameQueryName() {
		return "fun.userNoticeQuery";
	}

}
