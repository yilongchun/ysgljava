package com.vieking.role.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.role.model.Role;
import com.vieking.role.model.UserGroup;
import com.vieking.role.model.UserGroupRole;

/**
 * 用户组管理 <br>
 * <br>
 * 
 * <p>
 * <a href="UserGroupHome.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("sys.userGroupHome")
@AutoCreate
public class UserGroupHome extends BaseHome<UserGroup> {

	private static final long serialVersionUID = -4897668442982939963L;

	@Observer(value = "event.com.vieking.role.model.Role", create = false)
	public void addRoles(String tagTmpId, List<Role> seleRoles) {
		if (!getInstance().getTmpId().equals(tagTmpId))
			return;
		for (Iterator<Role> iterator = seleRoles.iterator(); iterator.hasNext();) {
			Role role = iterator.next();
			getInstance().addNewRole(new UserGroupRole(role, ""));
		}
	}

	public void saveUserGroupRoles() {
		List<String> ids = new ArrayList<String>();
		for (Iterator<UserGroupRole> iterator = getInstance().getRoles()
				.iterator(); iterator.hasNext();) {
			UserGroupRole ugr = iterator.next();
			ugr.setGroup(getInstance());
			entityManager.persist(ugr);
			ids.add(ugr.getId());
		}
		if (ids.isEmpty()) {
			ids.add("xxxxxxxxxxxxxxxxxxxxxxxxxx");
		}
		log.debug("ids--->{0}", ids);
		entityManager
				.createQuery(
						"delete UserGroupRole o where o.id not in( :ids) and o.group.id=:groupId")
				.setParameter("ids", ids)
				.setParameter("groupId", getInstance().getId()).executeUpdate();
		entityManager.flush();
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

	public boolean validation() {
		boolean result = true;
		UserGroup ug = entityManager.find(UserGroup.class, getInstance()
				.getId());
		if (ug != null && !isManaged()) {
			facesMessages.add(instanceInfoStr() + "已存在！");
			result = false;
		}
		return result;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String persist() {
		if (!validation()) {
			return null;
		}
		UserGroup o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		saveUserGroupRoles();
		lastStateChange = super.persist();
		return lastStateChange;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String update() {
		if (!validation()) {
			return null;
		}
		UserGroup o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		saveUserGroupRoles();
		lastStateChange = super.update();
		return lastStateChange;
	}

	@Override
	public String appName() {
		return "用户组管理";
	}

	@Override
	public String instanceInfo() {
		return "用户组【 #{sys.userGroupHome.getInstance().name} #{sys.userGroupHome.getInstance().des}】";
	}

}
