package com.vieking.functions.manager;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.functions.model.Contact;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;

@Name("fun.contactQuery")
@Scope(ScopeType.PAGE)
public class ContactQuery extends BaseNqtQuery<Contact> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1874091927872212610L;
	@In
	protected EntityManager entityManager;

	public void processRequestParams() {

	}

	@Override
	public void executeQueryCounts() {
	}

	public String getQueryDesc() {
		return "通讯录管理";
	}

	public ContactQuery() {
		setOrder("o.ct desc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.fun.contactQuery";
	}

	@Override
	public String nameQueryName() {
		return "fun.contactQuery";
	}

}
