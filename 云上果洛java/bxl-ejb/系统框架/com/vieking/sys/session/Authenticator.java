package com.vieking.sys.session;

import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.bpm.Actor;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.vieking.role.dao.UserDao;
import com.vieking.role.model.User;
import com.vieking.role.model.UserGroupRole;
import com.vieking.seam.ext.UsbKeyCredentials;
import com.vieking.seam.security.IdentityEx;
import com.vieking.sys.enumerable.DataState;
import com.vieking.sys.enumerable.UserType;
import com.vieking.sys.exception.Const;
import com.vieking.sys.util.StringUtil;

@Name("authenticator")
public class Authenticator {
	@Logger
	private Log log;

	@In
	IdentityEx identity;
	@In
	UsbKeyCredentials credentials;
	@In
	protected FacesMessages facesMessages;

	@Out(scope = ScopeType.SESSION, required = false, value = Const.CURRUSER)
	private User currentUser;

	@Out(scope = ScopeType.SESSION, required = false, value = "Login")
	private int login;

	@In(value = "userDao")
	private UserDao userDao;

	@In(required = false)
	private Actor actor;

	@In(required = false)
	private WikiCaptcha captcha;

	private void initIdentity() {
		// 安全角色初始化
		List<UserGroupRole> roles = currentUser.getGroup().getRoles();

		// 将用户组名称 加入角色
		identity.addRole("SYS_UG_"
				+ currentUser.getGroup().getName().toUpperCase());

		currentUser.toString();// 装载用户信息
		currentUser.setDesktop(currentUser.getGroup().getDesktop());

		if (actor != null) {
			actor.setId(StringUtils.upperCase(currentUser.getActorId()));
		}
		for (Iterator<UserGroupRole> iterator = roles.iterator(); iterator
				.hasNext();) {
			UserGroupRole ugr = iterator.next();
			identity.addRole(ugr.getRole().getName().toUpperCase());
			identity.addGlobalRole(ugr.getRole().getName().toUpperCase());
			currentUser.getRolesMap().put(
					ugr.getRole().getName().toUpperCase(),
					ugr.getRole().getDes());

			// 添加工作流角色
			if (ugr.getRole().isActor()) {
				if (actor != null) {
					actor.getGroupActorIds().add(
							ugr.getRole().getName().toUpperCase());
				}
			}

		}

		log.info("认证通过 {0}", currentUser.getLoginName());
	}

	private boolean 管理用户验证() {
		log.debug("管理用户验证密码{0}", currentUser.getLoginName());

		if (验证密码(credentials.getPassword(), currentUser.getNoLockpwd())) {
			initIdentity();
			facesMessages.add("{0}登录成功！", currentUser.getLoginName());
			return true;
		} else {
			setSession();
			currentUser = null;
			return false;
		}
	}

	private boolean 系统初始化验证() {
		credentials.getUsername();
		if ("sysman".equals(credentials.getUsername())
				&& "kingfree".equals(credentials.getPassword())) {
			currentUser = new User();
			currentUser.setName("管理员");
			currentUser.setId("System");
			identity.addRole("SUPERVISOR");
			identity.addRole("GLOBALSEARCH");
			return true;
		} else {
			return false;
		}
	}

	private boolean 验证密码(String ipwd, String spwd) {
		String tpwd = StringUtil.makePassword(ipwd);
		if (((tpwd == null) | "".equals(credentials.getPassword()))
				&& (spwd == null)) {

			return true;
		} else if ((tpwd != null) && (tpwd.equals(spwd))) {
			return true;
		}
		return false;

	}

	private boolean 普通用户验证() {
		if (验证密码(credentials.getPassword(), currentUser.getNoLockpwd())) {
			initIdentity();
			return true;
		} else {
			currentUser = null;
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public boolean authenticate() {
		log.info("开始认证 {0}", credentials.getUsername());
		if (userDao.noUser()) {
			return 系统初始化验证();
		}
		currentUser = userDao.getLoginUser(credentials.getUsername());
		if (captcha != null && captcha.getResponse() != null
				&& captcha.validateResponse("校验码错误，请重新输入！")) {
			setSession();
			return false;
		}

		if (currentUser != null) {
			if (!DataState.正常.equals(currentUser.getState())) {
				facesMessages.add(FacesMessage.SEVERITY_ERROR, "用户已{0}",
						currentUser.getState().getDesc());
				currentUser = null;
				return false;
			}
			if (UserType.管理用户.equals(credentials.getUserType())) {
				return 管理用户验证();
			}
			if (UserType.普通用户.equals(credentials.getUserType())) {
				return 普通用户验证();
			}

		}
		setSession();

		currentUser = null;
		return false;
	}

	public void setSession() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext extContext = facesContext.getExternalContext();
		HttpSession session = (HttpSession) extContext.getSession(true);
		if (session.getAttribute("Login") != null) {
			login = Integer.valueOf(session.getAttribute("Login").toString()) + 1;
		} else {
			login = 1;
		}
	}

}
