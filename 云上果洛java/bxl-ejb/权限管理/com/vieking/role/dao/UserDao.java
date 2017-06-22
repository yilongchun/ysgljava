package com.vieking.role.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Init;

import com.vieking.role.model.Application;
import com.vieking.role.model.Role;
import com.vieking.role.model.RoleApp;
import com.vieking.role.model.User;
import com.vieking.role.model.UserGroup;
import com.vieking.role.model.UserGroupRole;
import com.vieking.sys.base.BaseDaoHibernate;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.util.StringUtil;

@Name("userDao")
@AutoCreate
public class UserDao extends BaseDaoHibernate {

	private static final long serialVersionUID = -189228877334733044L;

	@In(value = "org.jboss.seam.core.init", scope = ScopeType.APPLICATION)
	private Init init;

	public User findUser(String userId) {
		return entityManager.find(User.class, userId);
	}

	@SuppressWarnings("unchecked")
	public User getLoginUser(String loginName) {
		User result = null;
		List<User> users = entityManager
				.createQuery("from User o  where o.loginName=:loginName")
				.setParameter("loginName",
						StringUtils.lowerCase(StringUtils.trim(loginName)))
				.getResultList();
		if (users.isEmpty())
			return null;
		result = (User) users.get(0);	
		return result;
	}

	@SuppressWarnings("unchecked")
	public User getLoginUserBylockedId(String lockedId) {
		User result = null;
		List<User> users = entityManager
				.createQuery(
						"from User o   where o.lockedId=:lockedId")
				.setParameter("lockedId", lockedId).getResultList();
		if (users.isEmpty())
			return null;
		result = (User) users.get(0);
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<User> list(String organId, String verifcode) {
		return entityManager
				.createQuery(
						"from User o where o.organ.id=:organId and bitand(o.syncSign,:verifcode)=0   and o.state='正常'")
				.setParameter("organId", organId)
				.setParameter("verifcode", verifcode).getResultList();
	}

	public void save(User u) {
		entityManager.persist(u);
	}

 

	public void changePassword(String loginName, String oldpwd, String newpwd)
			throws DaoException {
		User u = getLoginUser(loginName);

		if (u != null) {
			String nPwd = StringUtil.makePassword(newpwd);
			String uPwd = StringUtil.makePassword(u.getNoLockpwd());
			if (((oldpwd == null) | "".equals(oldpwd)) && (uPwd == null)) {
				u.setNoLockpwd(nPwd);
				entityManager.persist(u);
			} else if ((oldpwd != null) && (oldpwd.equals(uPwd))) {
				u.setNoLockpwd(nPwd);
				entityManager.persist(u);
			} else {
				throw new DaoException("用户原始密码错误！", "UserManager", "002");
			}
		} else {
			throw new DaoException("用户没有找到！", "UserManager", "001");
		}

	}

	@SuppressWarnings("rawtypes")
	public Boolean validateRole(String uid, String roleCode) {
		List l = entityManager
				.createQuery(
						"select distinct o.id from User o "
								+ " join o.roles r "
								+ " where r.coding like :roleCoding"
								+ " and o.id=:uid").setParameter("uid", uid)
				.setParameter("roleCoding", roleCode + "%").getResultList();
		return !l.isEmpty();
	}

	@SuppressWarnings("unchecked")
	public boolean noUser() {
		List<String> uids = entityManager
				.createQuery("select o.id from User o").setMaxResults(1)
				.getResultList();
		return uids.isEmpty();
	}

	public Role getRole(String name) {
		return entityManager.find(Role.class,
				StringUtils.upperCase(StringUtils.trim(name)));
	}

	public UserGroup getUserGp(String name) {
		return entityManager.find(UserGroup.class,
				StringUtils.lowerCase(StringUtils.trim(name)));
	}

	@SuppressWarnings("unchecked")
	public User getUser(String name, String pwd) {
		List<User> ulist = entityManager
				.createQuery(
						"from User o  where o.loginName =:name and o.noLockpwd =:pwd and o.group.name ='siji' ")
				.setParameter("name", name).setParameter("pwd", pwd)
				.getResultList();
		if (!ulist.isEmpty())
			return ulist.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public User getEnterpriseUser(String name, String pwd) {
		List<User> ulist = entityManager
				.createQuery(
						"from User o  where o.loginName =:name and o.noLockpwd =:pwd and o.driver.id is null ")
				.setParameter("name", name).setParameter("pwd", pwd)
				.getResultList();
		if (!ulist.isEmpty())
			return ulist.get(0);
		return null;
	}

	public List<Application> getApps(User user) {
		List<UserGroupRole> userGroups = user.getGroup().getRoles();
		List<Application> apps = new ArrayList<Application>();
		for (Iterator<UserGroupRole> iterator = userGroups.iterator(); iterator
				.hasNext();) {
			UserGroupRole ugr = iterator.next();
			for (Iterator<RoleApp> iterator2 = ugr.getRole().getRoleApps()
					.iterator(); iterator2.hasNext();) {
				RoleApp roleApp = iterator2.next();
				apps.add(roleApp.getApp());
			}
		}
		return apps;
	}

}
