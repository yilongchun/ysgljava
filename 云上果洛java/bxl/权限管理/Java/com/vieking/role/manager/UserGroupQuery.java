package com.vieking.role.manager;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;

import com.vieking.role.model.UserGroup;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;

@Name("sys.userGroupQuery")
@Scope(ScopeType.PAGE)
public class UserGroupQuery extends BaseNqtQuery<UserGroup> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7143583542160447428L;

	@In
	protected EntityManager entityManager;

	@In
	protected FacesMessages facesMessages;

	public void processRequestParams() {

	}

	@Override
	public void executeQueryCounts() {
	}

	public String getQueryDesc() {
		return "用户组管理";
	}

	public UserGroupQuery() {
		setOrder("o.name asc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.sys.userGroup";
	}

	@Override
	public String nameQueryName() {
		return "sys.userGroup";
	}

}
