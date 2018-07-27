package com.vieking.role.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.base.NqtSelector;
import com.vieking.basicdata.model.Department;
import com.vieking.role.model.User;
import com.vieking.sys.base.IResultConvert;
import com.vieking.sys.tree.ContactTree;

@Name("sys.userSelector")
@Scope(ScopeType.PAGE)
public class UserSelectorEnh extends NqtSelector<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4780934312775103604L;

	public List<Department> bmList;
	
	public List<Department> getBmList() {
		List<Department> list = entityManager.createQuery("from Department o").getResultList();
		return list;
	}

	public void setBmList(List<Department> bmList) {
		this.bmList = bmList;
	}
	
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
