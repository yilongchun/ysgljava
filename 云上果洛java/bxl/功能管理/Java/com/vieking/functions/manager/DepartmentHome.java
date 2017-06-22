package com.vieking.functions.manager;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.basicdata.model.Department;

@Name("fun.departmentHome")
@AutoCreate
public class DepartmentHome extends BaseHome<Department> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3375119420068385197L;

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
		Department vo = entityManager.find(Department.class, getInstance()
				.getId());
		if (vo != null && !isManaged()) {
			facesMessages.add(instanceInfoStr() + "已存在！");
			result = false;
		}
		return result;
	}

	@Override
	public String appName() {
		return "单位部门管理";
	}

	@Override
	public String instanceInfo() {

		return "单位部门【 #{fun.departmentHome.getInstance().name}】";
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String persist() {
		if (!validation()) {
			return null;
		}
		Department o = getInstance();
		if (o.getSuperior() != null) {
			o.setLeaf(false);
		}
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
		Department o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		lastStateChange = super.update();
		return lastStateChange;
	}

	public void getSuper() {

		Department ct = entityManager.find(Department.class, soId);
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
