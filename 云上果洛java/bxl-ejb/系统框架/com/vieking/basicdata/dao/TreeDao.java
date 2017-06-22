package com.vieking.basicdata.dao;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.sys.base.BaseDaoHibernate;

@Name("sys.dao.tree")
@AutoCreate
@Scope(ScopeType.EVENT)
public class TreeDao extends BaseDaoHibernate {
	private static final long serialVersionUID = 7314212203225893533L;

	@SuppressWarnings("rawtypes")
	public List loadNodes(String sql, String supCode) {
		return entityManager.createQuery(sql).setParameter("supCode", supCode)
				.getResultList();
	}

}
