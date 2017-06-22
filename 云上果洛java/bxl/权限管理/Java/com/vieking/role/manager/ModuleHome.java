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
import com.vieking.role.model.Module;
import com.vieking.role.model.ModuleApp;

/**
 * 模块管理 <br>
 * <br>
 * 
 * <p>
 * <a href="ModuleHomeEnh.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("sys.moduleHome")
@AutoCreate
public class ModuleHome extends BaseHome<Module> {

	private static final long serialVersionUID = -4897668442982939963L;

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
		Module vo = entityManager.find(Module.class, getInstance().getId());
		if (vo != null && !isManaged()) {
			facesMessages.add(instanceInfoStr() + "已存在！");
			result = false;
		}
		return result;

	}

	@Observer(value = "event.com.vieking.role.model.Application", create = false)
	public void addApplications(String tagTmpId,
			List<Application> seleApplications) {
		if (!getInstance().getTmpId().equals(tagTmpId))
			return;
		for (Iterator<Application> iterator = seleApplications.iterator(); iterator
				.hasNext();) {
			Application app = iterator.next();
			getInstance().addNewModuleApp(new ModuleApp(app, null, 10));
		}
	}

	public void saveModuleApps() {
		List<String> ids = new ArrayList<String>();
		for (Iterator<ModuleApp> iterator = getInstance().getModuleApps()
				.iterator(); iterator.hasNext();) {
			ModuleApp ma = iterator.next();
			ma.setModule(getInstance());
			entityManager.persist(ma);
			ids.add(ma.getId());
		}
		if (ids.isEmpty()) {
			ids.add("xxxxxxxxxxxxxxxxxxxxxxxxxx");
		}
		log.debug("ids--->{0}", ids);
		entityManager
				.createQuery(
						"delete ModuleApp o where o.id not in( :ids) and o.module.id=:moduleId")
				.setParameter("ids", ids)
				.setParameter("moduleId", getInstance().getId())
				.executeUpdate();
		entityManager.flush();
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String persist() {
		if (!validation()) {
			return null;
		}
		Module o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		saveModuleApps();
		lastStateChange = super.persist();
		return lastStateChange;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String update() {
		if (!validation()) {
			return null;
		}
		Module o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		saveModuleApps();
		lastStateChange = super.update();
		return lastStateChange;
	}

	@Override
	public String appName() {
		return "模块管理";
	}

	@Override
	public String instanceInfo() {
		return "模块【 #{sys.moduleHome.getInstance().name} #{sys.moduleHome.getInstance().des}】";
	}

}
