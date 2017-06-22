package com.vieking.basicdata.manager;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.basicdata.model.Canton;

@Name("sys.cantonHome")
@AutoCreate
public class CantonHome extends BaseHome<Canton> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4266938364227006435L;

	private String soId = "";

	private String notSuper;

	private boolean isSuper;

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
			if (soId != "")
				getSuper();
		}
	}

	public boolean validation() {
		boolean result = true;
		Canton vo = entityManager.find(Canton.class, getInstance().getId());
		if (vo != null && !isManaged()) {
			facesMessages.add(instanceInfoStr() + "已存在！");
			result = false;
		}
		return result;
	}

	@Override
	public String appName() {
		return "行政区划管理";
	}

	@Override
	public String instanceInfo() {

		return "行政区划【 #{sys.cantonHome.getInstance().name}】";
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String persist() {
		if (!validation()) {
			return null;
		}
		Canton o = getInstance();
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
		Canton o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		lastStateChange = super.update();
		return lastStateChange;
	}

	public void getSuper() {

		Canton ct = entityManager.find(Canton.class, soId);
		if (notSuper.equals("true")) {
			getInstance().setSuperior(ct);
		} else {
			getInstance().setSuperior(ct.getSuperior());
		}

	}

	public String getSoId() {
		return soId;
	}

	public void setSoId(String soId) {
		this.soId = soId;
	}

	public boolean isSuper() {
		return isSuper;
	}

	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}

	public String getNotSuper() {
		return notSuper;
	}

	public void setNotSuper(String notSuper) {
		this.notSuper = notSuper;
	}

}
