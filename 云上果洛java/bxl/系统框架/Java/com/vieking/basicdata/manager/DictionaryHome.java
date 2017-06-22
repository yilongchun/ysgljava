package com.vieking.basicdata.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.basicdata.model.Dictionary;
import com.vieking.basicdata.model.DictionaryType;

@Name("dic.dictionaryHome")
@AutoCreate
public class DictionaryHome extends BaseHome<Dictionary> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3493489031475426517L;

	private String soId = "";

	private String notSuper;

	private boolean isSuper;

	private List<DictionaryType> dicLists = new ArrayList<DictionaryType>();

	/***
	 * 页面初始化
	 */
	public void wire() {
		if (isIdDefined()) {
			getInstance();
		} else {
			clearInstance();
			getInstance();
			if (soId != "")
				getSuper();
		}
		initDicLists();
	}

	public boolean validation() {
		boolean result = true;
		Dictionary vo = entityManager.find(Dictionary.class, getInstance()
				.getId());
		if (vo != null && !isManaged()) {
			facesMessages.add(instanceInfoStr() + "已存在！");
			result = false;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public void initDicLists() {
		dicLists = entityManager.createQuery(
				" from DictionaryType o where o.invalid = 0").getResultList();
	}

	/***
	 * 添加
	 */
	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String persist() {
		if (!validation()) {
			return null;
		}
		entityManager.persist(getInstance());
		entityManager.flush();
		lastStateChange = super.persist();
		return lastStateChange;
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public String update() {
		if (!validation()) {
			return null;
		}
		entityManager.persist(getInstance());
		entityManager.flush();
		lastStateChange = super.persist();
		return lastStateChange;
	}

	@Override
	public String appName() {
		return "字典表录入";
	}

	@Override
	public String instanceInfo() {
		return "字典表管理";
	}

	public String getSoId() {
		return soId;
	}

	public void setSoId(String soId) {
		this.soId = soId;
	}

	public String getNotSuper() {
		return notSuper;
	}

	public void setNotSuper(String notSuper) {
		this.notSuper = notSuper;
	}

	public boolean isSuper() {
		return isSuper;
	}

	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}

	public void getSuper() {
		Dictionary ct = entityManager.find(Dictionary.class, soId);
		if (notSuper.equals("true")) {
			getInstance().setSuperior(ct);
		} else {
			getInstance().setSuperior(ct.getSuperior());
		}
	}

	public List<DictionaryType> getDicLists() {
		return dicLists;
	}

	public void setDicLists(List<DictionaryType> dicLists) {
		this.dicLists = dicLists;
	}

}
