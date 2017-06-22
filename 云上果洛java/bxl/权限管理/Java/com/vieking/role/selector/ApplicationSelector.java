package com.vieking.role.selector;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.base.NqtSelector;
import com.vieking.role.model.Application;

/**
 * 应用选择器
 * 
 * @author 万俊
 * 
 */
@Name("role.applicationSelector")
@Scope(ScopeType.PAGE)
public class ApplicationSelector extends NqtSelector<Application> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 401690412712453632L;

	@Override
	public String defEvent() {
		return "event.com.vieking.role.model.Application";
	}

	@Override
	protected Object getObjectId(Application obj) {
		return obj.getId();
	}

	@Override
	protected String getObjectName(Application obj) {
		return obj.getName();
	}

	@Override
	protected String getViewId() {
		return "/common/selector/applicationSelector.xhtml";
	}

	@Override
	public void processRequestParams() {
	}

	@Override
	public String getQueryDesc() {
		return "应用选择器";
	}

	@Override
	public String nameQueryName() {
		return "sys.application";
	}

	@Override
	public String queryHelpName() {
		return "sys.applicationSelect";
	}

}
