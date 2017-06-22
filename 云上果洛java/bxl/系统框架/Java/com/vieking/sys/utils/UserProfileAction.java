package com.vieking.sys.utils;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.faces.Redirect;
import org.jboss.seam.international.Messages;
import org.jboss.seam.log.Log;

import com.vieking.basicdata.dao.NqtDao;
import com.vieking.role.dao.UserDao;
import com.vieking.role.model.User;
import com.vieking.sys.base.ExceptionHandler;
import com.vieking.sys.bean.QueryParamBean;
import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.util.StringUtil;

@Name("sys.act.userProfile")
@AutoCreate
@Scope(ScopeType.EVENT)
public class UserProfileAction implements Serializable {

	private static final long serialVersionUID = 2691026414028100641L;

	@Logger
	protected Log log;

	@In
	protected EntityManager entityManager;

	@In
	protected FacesMessages facesMessages;

	@In(create = true)
	protected Redirect redirect;

	@In("sys.nqtDao")
	protected NqtDao nqtDao;

	@Out(value = "userProFile", required = true, scope = ScopeType.CONVERSATION)
	@In(value = "userProFile", required = false, scope = ScopeType.CONVERSATION)
	private User userProFile;

	@In(value = Const.CURRUSER, required = false)
	protected User currUser;

	@In("exceptionHandler")
	protected ExceptionHandler handler;

	private String pwdOne;

	private String pwdTwo;

	private String pwdOld;

	private String phone;

	@In()
	UserDao userDao;

	public void showProFile() {
		log.debug("showProFile{0}", currUser.getName());
		userProFile = entityManager.find(User.class, currUser.getId());
	}

	public void changePwd() {
		userProFile = new User();
	}

	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.REQUIRED)
	public void sendPwd() {
		if (phone == null) {
			facesMessages.add("请填写手机号码");
			return;
		}
		List<User> list = entityManager
				.createQuery(" from User o where o.phone =:phone ")
				.setParameter("phone", phone).getResultList();
		if (list.isEmpty()) {
			facesMessages.add("用户手机号码不存在");
			return;
		}
		User u = list.get(0);

		u.setNoLockpwd(StringUtil.makePassword("666666"));
		entityManager.persist(u);

	}

	protected boolean isItemUnique(User item) {
		try {
			boolean isUnique = nqtDao.isItemUnique(userProFile,
					"sys.user.isitemunique", new QueryParamBean("loginName",
							item.getLoginName()));
			if (!isUnique) {
				facesMessages.add("登录名【{0}】已被其他用户使用", item.getLoginName());
				return false;
			} else {

				return true;
			}
		}

		catch (DaoException e) {
			facesMessages.add(e.getMessage());
			return false;
		}
	}

	private boolean canChangePwd(User u, String oldPwd, String newPwd) {

		String uPwd = u.getNoLockpwd();
		log.debug("uPwd={0}", uPwd);
		// 密码为空处理
		if (((oldPwd == null) | "".equals(oldPwd)) && (uPwd == null)) {
			u.setNoLockpwd(StringUtil.makePassword(newPwd));
			return true;
		}
		String oPwd = StringUtil.makePassword(oldPwd);
		log.debug("oPwd={0}", oPwd);
		if ((!StringUtils.isEmpty(oPwd)) && (oPwd.equals(uPwd))) {
			u.setNoLockpwd(StringUtil.makePassword(newPwd));
			return true;
		} else {
			facesMessages.addFromResourceBundleOrDefault("", "用户原始密码错误！");
		}
		return false;
	}

	@Restrict(value = "#{currentUser.id==userProFile.id}")
	@Transactional(TransactionPropagationType.REQUIRED)
	@End
	public String save() {
		try {
			if ((canChangePwd(userProFile, pwdOld, pwdOne) && (isItemUnique(userProFile)))) {
				entityManager.persist(userProFile);
				entityManager.flush();
				facesMessages.addFromResourceBundleOrDefault(
						"app.save.succeed", "用户信息修改成功！", userProFile.getName());
				return Const.SUCCEED;
			} else {
				return Const.FAIL;
			}
		} catch (Exception e) {
			handler.handleException(e, facesMessages);
		}
		return Const.FAIL;
	}

	public String getAppDesc() {
		return Messages.instance().get("app.sys.userProfile");
	}

	public String getPwdOne() {
		return pwdOne;
	}

	public void setPwdOne(String pwdOne) {
		this.pwdOne = pwdOne;
	}

	public String getPwdTwo() {
		return pwdTwo;
	}

	public void setPwdTwo(String pwdTwo) {
		this.pwdTwo = pwdTwo;
	}

	public String getPwdOld() {
		return pwdOld;
	}

	public void setPwdOld(String pwdOld) {
		this.pwdOld = pwdOld;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
