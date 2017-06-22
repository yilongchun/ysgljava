package com.vieking.basicdata.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;

import com.vieking.basicdata.model.Department;
import com.vieking.basicdata.model.Dictionary;
import com.vieking.sys.base.BaseDaoHibernate;

@Name("dictionaryDao")
@AutoCreate
public class DictionaryDao extends BaseDaoHibernate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5061027408225379507L;

	/***/
	@SuppressWarnings("unchecked")
	public List<Dictionary> dictions(String code) {
		return entityManager
				.createQuery(
						" from Dictionary o where  o.dictionaryType.bh =:code and (o.superior.code is null or o.superior.code ='') order by o.code asc")
				.setParameter("code", code).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Dictionary> dictionnaryType(String code) {
		return entityManager
				.createQuery(
						" from Dictionary o where  o.dictionaryType.bh =:code  order by o.code asc")
				.setParameter("code", code).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Dictionary> dictionarys(String superCode) {
		return entityManager
				.createQuery(
						" from Dictionary o where  o.superior.code =:superCode) order by o.code asc")
				.setParameter("superCode", superCode).getResultList();
	}

	/***/
	public Dictionary dictionsByName(String code, String name) {
		return (Dictionary) entityManager
				.createQuery(
						" from Dictionary o where   o.dictionaryType.bh =:code and  o.name =:name and (o.superior.code is null or o.superior.code ='') order by o.code asc")
				.setParameter("code", code).setParameter("name", name)
				.getResultList().get(0);
	}

	public Dictionary dictionsByName(String code) {
		return (Dictionary) entityManager
				.createQuery(" from Dictionary o where   o.code =:code ")
				.setParameter("code", code).getResultList().get(0);
	}

	@SuppressWarnings("unchecked")
	public List<Dictionary> dictions(String code, String superior) {
		List<Dictionary> ls = entityManager
				.createQuery(
						"from Dictionary o where  o.dictionaryType.bh =:code and o.superior.code =:superior order by o.code ")
				.setParameter("code", code).setParameter("superior", superior)
				.getResultList();
		if (ls.isEmpty()) {
			return null;
		} else {
			return ls;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Dictionary> dictions(String code, List<String> strs) {
		return entityManager
				.createQuery(
						" from Dictionary o where o.dictionaryType.bh=:code and o.superior.code in (:strs)")
				.setParameter("code", code).setParameter("strs", strs)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Dictionary> dictions(List<String> strs) {
		return entityManager
				.createQuery(" from Dictionary o where  o.code in (:strs)")
				.setParameter("strs", strs).getResultList();
	}

	public static Dictionary indexOfList(List<Dictionary> list,
			Dictionary superDic) {
		for (int i = 0; i < list.size(); i++) {
			Dictionary tree = list.get(i);
			if (superDic.equals(tree.getCode())) {
				return tree;
			}
		}
		return null;
	}
	
	
	public Boolean getDepartmentById(String code) {
		List list = entityManager
				.createQuery(" from Department o where   o.superior.code =:code ")
				.setParameter("code", code).getResultList();
		if (list.size() > 0) {
			return false;
		}
		return true;
	}

}
