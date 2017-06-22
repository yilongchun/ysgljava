package com.vieking.role.selector;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.base.NqtSelector;
import com.vieking.role.model.Role;

/**
 * 角色选择器(新)
 * 
 * @author wul
 * 
 */
@Name("role.roleSelector")
@Scope(ScopeType.PAGE)
public class RoleSelector extends NqtSelector<Role> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8606828412161250777L;

	@Override
	public String defEvent() {
		return "event.com.vieking.role.model.Role";
	}

	@Override
	protected Object getObjectId(Role obj) {
		return obj.getId();
	}

	@Override
	protected String getObjectName(Role obj) {
		return obj.getName();
	}

	@Override
	protected String getViewId() {
		return "/common/selector/roleSelector.xhtml";
	}

	@Override
	public void processRequestParams() {
	}

	@Override
	public String getQueryDesc() {
		return "角色选择器";
	}

	@Override
	public String nameQueryName() {
		return "sys.role";
	}

	@Override
	public String queryHelpName() {
		return "sys.roleSelect";
	}

}
