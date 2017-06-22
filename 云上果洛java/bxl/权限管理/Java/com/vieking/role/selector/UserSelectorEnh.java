package com.vieking.role.selector;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.base.NqtSelector;
import com.vieking.role.model.User;

@Name("sys.userSelector")
@Scope(ScopeType.PAGE)
public class UserSelectorEnh extends NqtSelector<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4780934312775103604L;

	@Override
	public String getQueryDesc() {
		return "用户选择器";
	}

	@Override
	public String nameQueryName() {
		return "sys.user";
	}

	@Override
	public String queryHelpName() {
		return "sys.user.selector";
	}

	@Override
	public String defEvent() {
		return "event.sel.com.vieking.sys.model.User";
	}

	@Override
	protected Object getObjectId(User obj) {
		return obj.getId();
	}

	@Override
	protected String getObjectName(User obj) {
		return obj.getName();
	}

	@Override
	protected String getViewId() {
		return "/common/selector/userSelector.xhtml";
	}

	@Override
	public void processRequestParams() {
	}

}
