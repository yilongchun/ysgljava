package com.vieking.role.manager;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.role.model.Module;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;

@Name("sys.moduleQuery")
@Scope(ScopeType.PAGE)
public class ModuleQuery extends BaseNqtQuery<Module> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7143583542160447428L;

	public void processRequestParams() {

	}

	public String getQueryDesc() {
		return "功能组管理";
	}

	public ModuleQuery() {
		setOrder("o.name asc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.sys.module";
	}

	@Override
	public String nameQueryName() {
		return "sys.module";
	}

}
