package com.vieking.sys.ui;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.faces.Redirect;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.AuthorizationException;

import com.vieking.role.model.Application;
import com.vieking.sys.base.AppContext;
import com.vieking.sys.context.UserContextManager;
import com.vieking.sys.exception.DaoException;

@Name("vieking.ui.nav")
@Scope(ScopeType.EVENT)
@AutoCreate
public class Nav implements Serializable {

	private static final long serialVersionUID = -4239239062979853951L;

	@In(create = true)
	protected Redirect redirect;

	@In
	protected FacesMessages facesMessages;

	@In(value = "vieking.ui.ac", scope = ScopeType.CONVERSATION, required = false)
	private AppContext ac;

	@Logger
	protected Log log;

	@In(value = "userContextManager", scope = ScopeType.SESSION, required = true)
	protected UserContextManager userContextManager;

	@RequestParameter(value = "appName")
	private String appName;

	@RequestParameter(value = "returnValue")
	private String returnValue;

	public void open() throws DaoException {
		// log.debug("appName={0}", this.appName);
		Application app = _app(appName);
		Events.instance().raiseEvent("openApp", app);
		nav(app, null, null, false);
	}

	public void open(String _appName) throws DaoException {
		// log.debug("appName={0}", _appName);
		Application app = _app(_appName);

		Events.instance().raiseEvent("openApp", app);
		nav(app, null, null, true);
	}

	public void openObject(String _appName, String viewId)
			throws DaoException {
		log.debug("appName={0}", _appName);
		Application app = _app(_appName);
		Events.instance().raiseEvent("openApp", app);
		if (StringUtils.isEmpty(viewId)) {
			nav(app, app.getPath() + "/edit.xhtml", null, true);
		} else {
			nav(app, viewId, null, true);
		}
	}

	private Application _app(String appName) throws DaoException {
		Application app = userContextManager.app(appName);
		if (app == null) {
			throw new AuthorizationException("【" + appName + "】应用访问权限未分配！");
		}
		return userContextManager.app(appName);
	}

	public void toApp() throws DaoException {
		if (ac != null) {
			redirect.captureCurrentView();
			ac.push(redirect.getViewId(), returnValue);
		}
		Application app = _app(appName);
		Events.instance().raiseEvent("openApp", app);
		nav(app, null, null, true);
	}

	public void backPrior() {
		if (ac != null && ac.getAppStack().isEmpty()) {
			facesMessages.add("应用访问池为空，无法返回！");
			return;
		}
		AppContext appContext = ac.getAppStack().pop();
		Events.instance().raiseEvent("revertApp", appContext);
		nav(appContext.getApp(), appContext.getView(), null, true);
	}

	private void nav(Application app, String view, String 方法请求, boolean 是否继续会话) {
		if (app == null) {
			facesMessages.add("应用没有找到，请检查！");
			return;
		}
		javax.faces.context.FacesContext.getCurrentInstance()
				.getExternalContext().getRequestMap();
		if (view == null) {
			view = app.getPath() + "/main.xhtml";
		}
		redirect.captureCurrentView();
		if (redirect.getViewId().equals(view))
			return;
		redirect.getParameters().remove("appName");
		redirect.getParameters().remove("actionMethod");
		redirect.setViewId(view);
		redirect.setConversationPropagationEnabled(是否继续会话);
		redirect.execute();
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
}
