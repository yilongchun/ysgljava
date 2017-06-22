package com.vieking.sys.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.bpm.Actor;
import org.jboss.seam.core.Init;
import org.jboss.seam.framework.PersistenceController;

import com.vieking.role.dao.UserDao;
import com.vieking.role.model.Application;
import com.vieking.role.model.ModuleApp;
import com.vieking.role.model.User;
import com.vieking.sys.bean.ModuleAppBean;
import com.vieking.sys.bean.ModuleBean;
import com.vieking.sys.bean.QueryParamBean;
import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.exception.KeException;
import com.vieking.sys.service.ApplicationManger;
import com.vieking.sys.service.NameQueryManger;
import com.vieking.sys.util.Config;
import com.vieking.sys.util.ExpressUtil;
import com.vieking.sys.util.StringUtil;

/**
 * 用户上下文管理 <br>
 * <br>
 * 
 * <p>
 * <a href="UserContextManager.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("userContextManager")
@AutoCreate
@Scope(ScopeType.SESSION)
public class UserContextManager extends PersistenceController<EntityManager>
		implements Serializable {

	private static final long serialVersionUID = -5648442172257423717L;

	@In("app.applicationManger")
	private ApplicationManger am;

	@In(value = "userDao")
	private UserDao userDao;

	@In("app.nameQueryManger")
	private NameQueryManger nqm;
 
	
	@In(scope = ScopeType.SESSION, required = false, value = Const.CURRUSER)
	private User currentUser;

	@In(value = "org.jboss.seam.core.init", scope = ScopeType.APPLICATION)
	private Init init;

	@In(value = "app.config")
	private Config config;

	private List<ModuleBean> mbs = new ArrayList<ModuleBean>();

	private Map<String, ModuleBean> mbMap = new HashMap<String, ModuleBean>();

	/** 登录用户 Session 失效时间 秒 */
	private int loginUserSessionMaxInactiveInterval = 1800;

	@Out(scope = ScopeType.SESSION, required = false)
	public Application currApp;

	public String userRole = "";

	public ArrayList<String> groupIds = new ArrayList<String>();

	private String module;

	private String app;

	public String getAppName() {
		if (currApp == null) {
			return null;
		} else {
			return currApp.getName();
		}
	}

	public void setAppName(String name) {
		if (!StringUtils.isEmpty(name)) {
			try {
				currApp = am.application(name);
			} catch (Exception e) {
				currApp = null;
			}
		}
	}

	public ModuleBean mb(String name) {
		name = name.toLowerCase().trim();
		return mbMap.get(name);
	}

	public Application app(String name) throws DaoException {
		return am.application(name);
	}

	public void putMa(ModuleApp ma) throws DaoException {
		ModuleBean mb = null;
		if (mbMap.containsKey(ma.getModule().getName())) {
			mb = mbMap.get(ma.getModule().getName());
		} else {
			mb = new ModuleBean(ma.getModule().getName(), ma.getModule()
					.getDes(), ma.getModule().getMenuPos(), ma.getModule()
					.getIco());
			mb.setShowInDesktop(ma.getModule().isShowInDesktop());
			mb.setShowInMenu(ma.getModule().isShowInMenu());
			mbMap.put(mb.getName(), mb);
		}
		ModuleAppBean mab = new ModuleAppBean(am.application(ma.getApp()
				.getName()), ma.getAppAlias(), ma.getSort());
		// 判断应用路径是否为直接访问的页面，如果是El表达式对表达式求职
		if (mab.getApp() != null
				&& StringUtils.isNotEmpty(mab.getApp().getPath())) {
			String _path = mab.getApp().getPath();
			if (StringUtil.hasExp(_path)) {
				_path = ExpressUtil.evs(_path, null);
			}
			mab.setPageUrl(StringUtil.isPageUrl(_path));
			if (StringUtils.isEmpty(_path)) {
				mab.setUrl("/home.xhtml");
				mab.setAppAlias(mab.getAppAlias() + "路径错误【"
						+ mab.getApp().getPath() + "】!");
			} else {
				// 判读页面是否为全路径Url
				if (StringUtil.isFullUrl(_path)) {
					mab.setUrl(_path);
					mab.setFullUrl(true);
				} else {
					// 如果不是页面Url 补默认页面访问 main
					if (!mab.isPageUrl()) {
						// 判读是否在最后加"/"
						if ("/".charAt(0) == _path.charAt(_path.length() - 1)) {
							_path = _path + "main.htm";
						} else {
							_path = _path + "/main.htm";
						}
					}
					// 判读是否在最前加"/"
					if ("/".charAt(0) == _path.charAt(0)) {
						mab.setUrl(_path);
					} else {
						mab.setUrl("/" + _path);
					}
				}

			}
		}
		mb.getMas().add(mab);
	}

	@Create
	public void create() throws KeException {
		try {
			loginUserSessionMaxInactiveInterval = Integer.valueOf(config
					.get(Const.KEY_LOGINUSERSESSIONMAXINACTIVEINTERVAL));
			groupIds.clear();
		} catch (Exception e) {

		}

	}

	 

 

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@Observer(create = true, value = org.jboss.seam.security.Identity.EVENT_LOGIN_SUCCESSFUL)
	public void userApps() throws DaoException {

		mbMap.clear();
		if (currentUser == null) {
			return;
		}
		List<ModuleApp> maLs = userDao
				.query("sys.user.moduleApps", "", new QueryParamBean("groupId",
						currentUser.getGroup().getId()), new QueryParamBean(
						"isDebug", init.isDebug() ? "true" : "flase")
				// new QueryParamBean("eqRoles", eqRoleIds()),
				// new QueryParamBean("userId", currentUser.getGroup().getId())
				);
		for (Iterator<ModuleApp> iterator = maLs.iterator(); iterator.hasNext();) {
			ModuleApp ma = iterator.next();
			putMa(ma);
		}
		for (Iterator<ModuleBean> iterator = mbMap.values().iterator(); iterator
				.hasNext();) {
			ModuleBean mb = iterator.next();
			Collections.sort(mb.getMas());
		}
		mbs.clear();
		mbs.addAll(mbMap.values());
		Collections.sort(mbs);

		getPersistenceContext().clear();
		HttpSession session = (HttpSession) getFacesContext()
				.getExternalContext().getSession(false);
		session.setMaxInactiveInterval(loginUserSessionMaxInactiveInterval);
		if (Init.instance().isJbpmInstalled()) {
			groupIds.clear();
			groupIds.addAll(Actor.instance().getGroupActorIds());
			groupIds.add(Actor.instance().getId());
		}
		getLog().debug("session MaxInactiveInterval {0}",
				session.getMaxInactiveInterval());
	}

	public UserContextManager() {
		super();
	}

	public Map<String, ModuleBean> getMbMap() {
		return mbMap;
	}

	public void setMbMap(Map<String, ModuleBean> mbMap) {
		this.mbMap = mbMap;
	}

	public List<ModuleBean> getMbs() {
		return mbs;
	}

	public void setMbs(List<ModuleBean> mbs) {
		this.mbs = mbs;
	}

	@Override
	protected String getPersistenceContextName() {
		return "entityManager";
	}

	public Application getCurrApp() {
		return currApp;
	}

	public void setCurrApp(Application currApp) {
		this.currApp = currApp;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public ArrayList<String> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(ArrayList<String> groupIds) {
		this.groupIds = groupIds;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

}
