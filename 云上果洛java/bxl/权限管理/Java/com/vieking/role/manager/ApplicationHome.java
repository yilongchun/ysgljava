package com.vieking.role.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;

import com.vieking.base.BaseHome;
import com.vieking.file.enumerable.DocLinkCount;
import com.vieking.file.model.AppDocType;
import com.vieking.file.model.DocType;
import com.vieking.role.model.Application;

/**
 * 应用管理 <br>
 * <br>
 * 
 * <p>
 * <a href="ApplicationHomeEnh.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("sys.applicationHome")
@AutoCreate
public class ApplicationHome extends BaseHome<Application> {

	private static final long serialVersionUID = -4897668442982939963L;

	@Observer(value = "event.sel.com.vieking.sys.model.DocType", create = false)
	public void addDocTypes(String tagTmpId, List<DocType> seleDocTypes) {
		if (!getInstance().getTmpId().equals(tagTmpId))
			return;
		for (Iterator<DocType> iterator = seleDocTypes.iterator(); iterator
				.hasNext();) {
			DocType docType = iterator.next();
			getInstance().addNewDocType(
					new AppDocType(docType, DocLinkCount.单个));
		}
	}

	public void saveAppDocTypes() {
		List<String> ids = new ArrayList<String>();
		for (Iterator<AppDocType> iterator = getInstance().getDocTypes()
				.iterator(); iterator.hasNext();) {
			AppDocType adt = iterator.next();
			adt.setApp(getInstance());
			entityManager.persist(adt);
			ids.add(adt.getId());
		}
		if (ids.isEmpty()) {
			ids.add("xxxxxxxxxxxxxxxxxxxxxxxxxx");
		}
		log.debug("ids--->{0}", ids);
		entityManager
				.createQuery(
						"delete AppDocType o where o.id not in( :ids) and o.app.id=:appId")
				.setParameter("ids", ids)
				.setParameter("appId", getInstance().getId()).executeUpdate();
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
		Application vo = entityManager.find(Application.class, getInstance()
				.getId());
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
		Application o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		saveAppDocTypes();
		lastStateChange = super.persist();
		Events.instance().raiseEvent("event.chg.com.vieking.sys.model.Application",
				getInstance().getName());
		return lastStateChange;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String update() {
		if (!validation()) {
			return null;
		}
		Application o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		saveAppDocTypes();
		Events.instance().raiseEvent("event.chg.com.vieking.sys.model.Application",
				getInstance().getName());
		lastStateChange = super.update();
		return lastStateChange;
	}

	@Override
	public String appName() {
		return "应用管理";
	}

	@Override
	public String instanceInfo() {
		return "应用【 #{sys.applicationHome.getInstance().name} #{sys.applicationHome.getInstance().des}】";
	}

}
