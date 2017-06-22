package com.vieking.sys.base;

import java.io.Serializable;
import java.util.Stack;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.log.Log;

import com.vieking.role.model.Application;

@Name("vieking.ui.ac")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class AppContext implements Serializable {
	private static final long serialVersionUID = 3968187047090511168L;

	@Logger
	Log log;

	private String popUrl;

	private String closeReRender;

	private Object returnObj;

	private String raiseEventName;

	private Application app;

	private String entId;

	private boolean inited;

	private String view;

	private String actionMethod;

	private String returnValue;

	private Stack<AppContext> appStack = new Stack<AppContext>();

	@SuppressWarnings("rawtypes")
	public void openPopUrl(String url, String returnValue) {
		setPopUrl(url);
		setReturnValue(returnValue);
		ValueExpression el = Expressions.instance().createValueExpression(
				returnValue);
		returnObj = el.getValue();
		log.debug("returnObj{0}", returnObj);
	}

	public String getPopUrl() {
		return popUrl;
	}

	public void setPopUrl(String popUrl) {
		log.debug("setPopUrl{0}", popUrl);
		this.popUrl = popUrl;
	}

	public void resetContext() {
		popUrl = "";
	}

	@Observer("openApp")
	public void initContext(Application _app) {
		log.debug("应用信息{0}", _app.toString());
		popUrl = null;
		this.app = _app;
	}

	@Observer("revertApp")
	public void revertApp(AppContext _ac) {
		log.debug("应用信息{0}", _ac.toString());
		initContext();
		this.popUrl = _ac.getPopUrl();
		this.app = _ac.getApp();
	}

	@Observer("resetAppContext")
	public void initContext() {
		log.debug("应用{0}复位", this.app.getDes());
		popUrl = null;
		returnValue = null;
		actionMethod = null;
		view = null;
		inited = false;
	}

	public void push(String view, String returnValue) {
		AppContext _ac = new AppContext(this.app, view, this.popUrl,
				this.entId, this.inited, this.actionMethod, returnValue);
		appStack.push(_ac);
	}

	public Application getApp() {
		return app;
	}

	public Stack<AppContext> getAppStack() {
		return appStack;
	}

	public boolean isInited() {
		return inited;
	}

	public void setInited(boolean inited) {
		this.inited = inited;
	}

	public String getEntId() {
		return entId;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public AppContext(Application app, String view, String popUrl,
			String entId, boolean inited, String actionMethod,
			String returnValue) {
		super();
		this.popUrl = popUrl;
		this.app = app;
		this.entId = entId;
		this.inited = inited;
		this.view = view;
		this.actionMethod = actionMethod;
		this.returnValue = returnValue;
	}

	public AppContext() {
		super();
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	public Object getReturnObj() {
		return returnObj;
	}

	public void setReturnObj(Object returnObj) {
		this.returnObj = returnObj;
	}

	public String getCloseReRender() {
		return closeReRender;
	}

	public void setCloseReRender(String closeReRender) {
		this.closeReRender = closeReRender;
	}

	public String getRaiseEventName() {
		return raiseEventName;
	}

	public void setRaiseEventName(String raiseEventName) {
		this.raiseEventName = raiseEventName;
	}

}
