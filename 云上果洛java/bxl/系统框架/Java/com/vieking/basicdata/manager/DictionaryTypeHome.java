package com.vieking.basicdata.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.basicdata.model.Dictionary;
import com.vieking.basicdata.model.DictionaryType;

@Name("dic.dictionaryTypeHome")
@AutoCreate
public class DictionaryTypeHome extends BaseHome<DictionaryType> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1374342333948864219L;

	/***
	 * 页面初始化
	 */
	public void wire() {
		if (isIdDefined()) {
			getInstance();
		} else {
			clearInstance();
			getInstance();
		}
	}

	public Dictionary dictionary;

	public void setDictionary() {
		if (dictionary != null) {
			for (int i = 0; i < getInstance().getDictionarys().size(); i++) {
				Dictionary dict = getInstance().getDictionarys().get(i);
				if (dictionary.getTmpId().equals(dict.getTmpId())) {
					getInstance().getDictionarys().remove(dict);
				}
			}
		}
		getInstance().getDictionarys().add(dictionary);
	}

	public void addDictionary() {
		dictionary = new Dictionary(getInstance());
	}

	public void editDictionary(Dictionary o) {
		dictionary = o;
	}

	public void superDictionary(Dictionary o) {
		dictionary = new Dictionary(getInstance());
		dictionary.setSuperior(o);
	}

	public void reDictionarys(Dictionary o) {
		getInstance().getDictionarys().remove(o);
	}

	/** 保存定义信息 */
	public void saveDictionarys() {
		List<String> dicIds = new ArrayList<String>();
		for (Iterator<Dictionary> iterator = getInstance().getDictionarys()
				.iterator(); iterator.hasNext();) {
			Dictionary dic = iterator.next();
			entityManager.persist(dic);
			entityManager.flush();
			dicIds.add(dic.getId());
		}
		if (dicIds.isEmpty()) {
			dicIds.add("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		}
		entityManager
				.createQuery(
						"delete Dictionary o where o.dictionaryType.id =:dId and o.id not in (:dicds)")
				.setParameter("dId", getInstance().getId())
				.setParameter("dicds", dicIds).executeUpdate();
	}

	/***
	 * 添加
	 */
	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String persist() {
		entityManager.persist(getInstance());
		saveDictionarys();
		entityManager.flush();
		return "";
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public String update() {
		entityManager.persist(getInstance());
		saveDictionarys();
		entityManager.flush();
		return "";
	}

	@Override
	public String appName() {
		return "字典表类型描述录入";
	}

	@Override
	public String instanceInfo() {
		return "字典表类型描述录入";
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

}
