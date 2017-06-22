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
import com.vieking.role.model.Application;
import com.vieking.role.model.Role;
import com.vieking.role.model.RoleApp;

/**
 * 角色管理 <br>
 * <br>
 * 
 * <p>
 * <a href="RoleHomeEnh.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("sys.roleHome")
@AutoCreate
public class RoleHome extends BaseHome<Role> {

	private static final long serialVersionUID = -4897668442982939963L;

	@Observer(value = "event.com.vieking.role.model.Application", create = false)
	public void addApplications(String tagTmpId,
			List<Application> seleApplications) {
		if (!getInstance().getTmpId().equals(tagTmpId))
			return;
		for (Iterator<Application> iterator = seleApplications.iterator(); iterator
				.hasNext();) {
			Application app = iterator.next();
			getInstance().addNewRoleApp(new RoleApp(app, ""));
		}
	}

	public void saveRoleApps() {
		List<String> ids = new ArrayList<String>();
		for (Iterator<RoleApp> iterator = getInstance().getRoleApps()
				.iterator(); iterator.hasNext();) {
			RoleApp ma = iterator.next();
			ma.setRole(getInstance());
			entityManager.persist(ma);
			ids.add(ma.getId());
		}
		if (ids.isEmpty()) {
			ids.add("xxxxxxxxxxxxxxxxxxxxxxxxxx");
		}
		log.debug("ids--->{0}", ids);
		entityManager
				.createQuery(
						"delete RoleApp o where o.id not in( :ids) and o.role.id=:roleId")
				.setParameter("ids", ids)
				.setParameter("roleId", getInstance().getId()).executeUpdate();
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
		Role vo = entityManager.find(Role.class, getInstance().getId());
		if (vo != null && !isManaged()) {
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
		Role o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		saveRoleApps();
		lastStateChange = super.persist();
		return lastStateChange;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String update() {
		if (!validation()) {
			return null;
		}
		Role o = getInstance();

		entityManager.persist(o);
		entityManager.flush();
		saveRoleApps();
		lastStateChange = super.update();
		return lastStateChange;
	}

	@Override
	public String appName() {
		return "角色管理";
	}

	@Override
	public String instanceInfo() {
		return "角色【 #{sys.roleHome.getInstance().name} #{sys.roleHome.getInstance().des}】";
	}

}
