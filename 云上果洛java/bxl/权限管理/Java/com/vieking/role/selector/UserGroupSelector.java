package com.vieking.role.selector;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.base.NqtSelector;
import com.vieking.role.model.UserGroup;

/**
 * 用户组(新)
 * 
 * @author wul
 * 
 */
@Name("user.userGroupSelector")
@Scope(ScopeType.PAGE)
public class UserGroupSelector extends NqtSelector<UserGroup> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5842529076844230385L;

	@Override
	public String defEvent() {
		return "event.com.vieking.user.model.UserGroup";
	}

	@Override
	protected Object getObjectId(UserGroup obj) {
		return obj.getId();
	}

	@Override
	protected String getObjectName(UserGroup obj) {
		return obj.getName();
	}

	@Override
	protected String getViewId() {
		return "/common/selector/userGroupSelector.xhtml";
	}

	@Override
	public void processRequestParams() {
	}

	@Override
	public String getQueryDesc() {
		return "用户组选择器";
	}

	@Override
	public String nameQueryName() {
		return "sys.userGroup";
	}

	@Override
	public String queryHelpName() {
		return "sys.userGroup.selector";
	}

}
