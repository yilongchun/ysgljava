package com.vieking.role.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.role.model.User;
import com.vieking.role.model.UserGroup;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.util.FacesUtil;
import com.vieking.sys.util.Message;

/**
 * 用户管理 <br>
 * <br>
 * 
 * <p>
 * <a href="UserHomeEnh.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("sys.userHome")
@AutoCreate
public class UserHome extends BaseHome<User> {

	private static final long serialVersionUID = -4897668442982939963L;

	private Boolean isAdmin = false;

	private String pwdOne = null;

	private String pwdTwo = null;

	private String ortRoleId;

	@Override
	public void create() {
		super.create();

	}

	@Observer(value = "event.com.vieking.user.model.UserGroup", create = false)
	public void selUserGroup(String tagTmpId, List<UserGroup> selLs) {
		if (!getInstance().getTmpId().equals(tagTmpId))
			return;
		if (selLs.isEmpty()) {
			getInstance().setGroup(null);
		} else {
			getInstance().setGroup(selLs.get(0));
		}
	}

	public boolean isWired() {
		return true;
	}

	/***
	 * 页面初始化方法
	 */
	public void wire() {
		log.debug("wire:Id{0}", getId());
		if (isIdDefined()) {
			getInstance();

		} else {
			clearInstance();
			getInstance();
		}
	}

	@SuppressWarnings("unchecked")
	protected boolean isItemUnique(User item) throws DaoException {
		List<User> users = entityManager
				.createQuery(nqm.findQuery("sys.user.isitemunique"))
				.setParameter("loginName", item.getLoginName()).getResultList();
		if (users.isEmpty()) {
			return true;
		} else if (users.get(0).getId().equals(item.getId())) {
			return true;
		}
		return false;
	}

	public boolean validation() {
		boolean result = true;

		try {
			if (!isItemUnique(getInstance())) {
				result = false;
				facesMessages.add("登录名【 {0}】已被其他用户使用！", getInstance()
						.getLoginName());
			}
		} catch (DaoException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}

	private void setPwd(User u, String pwd) {
		pwd = StringUtils.trim(pwd);
		if (!StringUtils.isEmpty(pwd)) {
			u.setNoLockpwd(com.vieking.sys.util.StringUtil.makePassword(pwd));
		} else {
			u.setNoLockpwd(null);
		}
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public void resetPwd() {
		User o = getInstance();
		setPwd(o, "666666");
		entityManager.persist(o);
		entityManager.flush();
		facesMessages.add("密码已重置为666666");
		FacesUtil.getExternalContext().getRequestMap()
				.put("_pops", new Message("提示消息", "密码已重置为666666"));
	}

	@Override
	public void cancel() {

		super.cancel();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String persist() {
		if (!validation()) {
			return null;
		}
		User o = getInstance();
		setPwd(o, pwdTwo);
		entityManager.persist(o);
		entityManager.flush();
		lastStateChange = super.persist();
		return lastStateChange;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String update() {
		if (!validation()) {
			return null;
		}
		User o = getInstance();
		entityManager.persist(o);
		entityManager.flush();

		entityManager.refresh(getInstance());
		lastStateChange = super.update();
		return lastStateChange;
	}

	@Override
	public String appName() {
		return "用户管理";
	}

	@Override
	public String instanceInfo() {
		return "用户【 #{sys.userHome.getInstance().loginName} #{sys.userHome.getInstance().name}】";
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
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

	public String getOrtRoleId() {
		return ortRoleId;
	}

	public void setOrtRoleId(String ortRoleId) {
		this.ortRoleId = ortRoleId;
	}

}
