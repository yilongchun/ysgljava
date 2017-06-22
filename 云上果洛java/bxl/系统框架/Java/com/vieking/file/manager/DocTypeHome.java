package com.vieking.file.manager;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;

import com.vieking.base.BaseHome;
import com.vieking.file.model.DocType;

/**
 * 文档类型管理 <br>
 * <br>
 * 
 * <p>
 * <a href="DocTypeHomeEnh.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("sys.docTypeHome")
@AutoCreate
public class DocTypeHome extends BaseHome<DocType> {

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
		DocType vo = entityManager.find(DocType.class, getInstance().getId());
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
		DocType o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		Events.instance().raiseEvent("event.chg.com.vieking.sys.model.DocType",
				getInstance().getName());
		lastStateChange = super.persist();
		return lastStateChange;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String update() {
		if (!validation()) {
			return null;
		}
		DocType o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		Events.instance().raiseEvent("event.chg.com.vieking.sys.model.DocType",
				getInstance().getName());
		lastStateChange = super.update();
		return lastStateChange;
	}

	@Override
	public String appName() {
		return "文档类型管理";
	}

	@Override
	public String instanceInfo() {
		return "文档类型【 #{sys.docTypeHome.getInstance().name} #{sys.docTypeHome.getInstance().des}】";
	}

}
