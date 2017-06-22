package com.vieking.role.manager;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;

import com.vieking.role.model.Role;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;

@Name("sys.roleQuery")
@Scope(ScopeType.PAGE)
public class RoleQuery extends BaseNqtQuery<Role> implements Serializable {

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
		return "角色管理";
	}

	public RoleQuery() {
		setOrder("o.name asc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.sys.role";
	}

	@Override
	public String nameQueryName() {
		return "sys.role";
	}

}
