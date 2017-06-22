/*
 * EDAS2 - TetraTech, Inc.
 *
 * Distributable under GPL license.
 * See terms of license at gnu.org.
 */
package com.vieking.sys.utils;

import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

@Name("sys.ff")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class FilterFactory {
	@Logger
	private Log log;
	@In
	private EntityManager entityManager;

	@In
	protected FacesMessages facesMessages;

	@SuppressWarnings("unchecked")
	public List<Object[]> filterModule(Object event) {
		List<Object[]> matches = null;
		try {
			String partial = event.toString().replaceFirst(" - .*$", "");
			matches = entityManager
					.createQuery(
							"select o.des,o.name  from Module  o where lower(o.des) like '%"
									+ partial.trim().toLowerCase()
									+ "%' or  lower(o.zjf) like '%"
									+ partial.trim().toLowerCase()
									+ "%' order by o.des asc")
					.setMaxResults(10).getResultList();
		} catch (Exception e) {
			facesMessages.add("模块过滤查询错误，请重试！");
			e.printStackTrace();
		}
		return matches;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> filterOrganStruType(Object event) {
		List<Object[]> matches = null;
		try {
			String partial = event.toString().replaceFirst(" - .*$", "");
			matches = entityManager
					.createQuery(
							"select o.name,o.coding  from OrganStruType  o where lower(o.name) like '%"
									+ partial.trim().toLowerCase()
									+ "%' or  lower(o.zjf) like '%"
									+ partial.trim().toLowerCase()
									+ "%' order by o.des asc")
					.setMaxResults(10).getResultList();
		} catch (Exception e) {
			facesMessages.add("组织结构类型过滤查询错误，请重试！");
			e.printStackTrace();
		}
		return matches;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> filterOrganType(Object event) {
		List<Object[]> matches = null;
		try {
			String partial = event.toString().replaceFirst(" - .*$", "");
			matches = entityManager
					.createQuery(
							"select o.name,o.coding  from OrganType  o where lower(o.name) like '%"
									+ partial.trim().toLowerCase()
									+ "%' or  lower(o.zjf) like '%"
									+ partial.trim().toLowerCase()
									+ "%' order by o.name asc")
					.setMaxResults(10).getResultList();
		} catch (Exception e) {
			facesMessages.add("机构类型过滤查询错误，请重试！");
			e.printStackTrace();
		}
		return matches;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> filterDocType(Object event) {
		List<Object[]> matches = null;
		try {
			String partial = event.toString().replaceFirst(" - .*$", "");
			matches = entityManager
					.createQuery(
							"select o.des,o.name  from DocType  o where lower(o.des) like '%"
									+ partial.trim().toLowerCase()
									+ "%' or  lower(o.zjf) like '%"
									+ partial.trim().toLowerCase()
									+ "%' order by o.des asc")
					.setMaxResults(10).getResultList();
		} catch (Exception e) {
			facesMessages.add("文档类型过滤查询错误，请重试！");
			e.printStackTrace();
		}
		return matches;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> filterCanton(Object event) {
		List<Object[]> matches = null;
		try {
			String partial = event.toString().replaceFirst(" - .*$", "");
			matches = entityManager
					.createQuery(
							"select o.wn,o.code  from Canton  o where lower(o.wn) like '%"
									+ partial.trim().toLowerCase()
									+ "%' or  lower(o.zjf) like '%"
									+ partial.trim().toLowerCase()
									+ "%' order by o.wn asc").setMaxResults(10)
					.getResultList();
		} catch (Exception e) {
			facesMessages.add("行政区划过滤查询错误，请重试！");
			e.printStackTrace();
		}
		return matches;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> filterOrgan(Object event) {
		List<Object[]> matches = null;
		try {
			String partial = event.toString().replaceFirst(" - .*$", "");
			matches = entityManager
					.createQuery(
							"select o.name,o.id  from Organ  o where lower(o.name) like '%"
									+ partial.trim().toLowerCase() + "%' "
									+ "or  lower(o.zjf) like '%"
									+ partial.trim().toLowerCase() + "%' "
									+ "order by o.name asc").setMaxResults(10)
					.getResultList();
		} catch (Exception e) {
			facesMessages.add("机构过滤查询错误，请重试！");
			e.printStackTrace();
		}
		return matches;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> filterCiOrgan(Object event) {
		List<Object[]> matches = null;
		try {
			String partial = event.toString().replaceFirst(" - .*$", "");
			matches = entityManager
					.createQuery(
							"select o.name,o.id  from CiOrgan  o where lower(o.name) like '%"
									+ partial.trim().toLowerCase() + "%' "
									+ "or  lower(o.zjf) like '%"
									+ partial.trim().toLowerCase() + "%' "
									+ "order by o.name asc").setMaxResults(10)
					.getResultList();
		} catch (Exception e) {
			facesMessages.add("机构过滤查询错误，请重试！");
			e.printStackTrace();
		}
		return matches;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> filterUserGroup(Object event) {
		List<Object[]> matches = null;
		try {
			String partial = event.toString().replaceFirst(" - .*$", "");
			matches = entityManager
					.createQuery(
							"select o.des,o.name  from UserGroup  o where lower(o.des) like '%"
									+ partial.trim().toLowerCase() + "%' "
									+ "or  lower(o.zjf) like '%"
									+ partial.trim().toLowerCase() + "%' "
									+ "order by o.des asc").setMaxResults(10)
					.getResultList();
		} catch (Exception e) {
			facesMessages.add("工程类型过滤查询错误，请重试！");
			e.printStackTrace();
		}
		return matches;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> filterEmployee(Object event) {
		List<Object[]> matches = null;
		try {
			String partial = event.toString().replaceFirst(" - .*$", "");
			matches = entityManager
					.createQuery(
							"select o.person.name,o.id  from Employee o where lower(o.person.name) like '%"
									+ partial.trim().toLowerCase() + "%' "
									+ "or  lower(o.person.zjf) like '%"
									+ partial.trim().toLowerCase() + "%' "
									+ "order by o.person.name")
					.setMaxResults(10).getResultList();
		} catch (Exception e) {
			facesMessages.add("员工过滤查询错误，请重试！");
			e.printStackTrace();
		}
		return matches;
	}

	@SuppressWarnings("unused")
	private <T> List<T> findAll(Class<T> type) {
		return findAll(type, null);
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> findAll(Class<T> type, String orderBy) {
		StringBuilder query = new StringBuilder();
		query.append("select e from ").append(type.getSimpleName())
				.append(" e");
		if (orderBy != null) {
			query.append(" order by e.").append(orderBy);
		}

		List<T> records = entityManager.createQuery(query.toString())
				.setHint("org.hibernate.readOnly", true).getResultList();
		if (records.size() > 25) {
			log.warn("You should be using auto-complete for "
					+ type.getSimpleName() + " rather than a list of values.");
		}
		return records;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> filterWfGroup(Object event) {
		List<Object[]> matches = null;
		try {
			String partial = event.toString().replaceFirst(" - .*$", "");
			matches = entityManager.createQuery(
					"select o.des,o.name from WfGroup o where lower(o.name) like '%"
							+ partial.trim().toLowerCase() + "%' "
							+ "or  lower(o.des) like '%"
							+ partial.trim().toLowerCase() + "%' "
							+ "order by o.name,o.des").getResultList();
		} catch (Exception e) {
			facesMessages.add("工作流组过滤查询错误，请重试！");
			e.printStackTrace();
		}
		return matches;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> filterProcessDefinitions(Object event) {
		List<Object[]> matches = null;
		try {
			String partial = event.toString().replaceFirst(" - .*$", "");
			matches = entityManager
					.createNativeQuery(
							"select distinct t.description_ des,t.name_ name from jbpm_processdefinition t "
									+ " where "
									+ " (lower( t.name_) like '%"
									+ partial.trim().toLowerCase()
									+ "%' "
									+ "or  lower( t.name_) like '%"
									+ partial.trim().toLowerCase()
									+ "%' "
									+

									"or lower(t.description_) like '%"
									+ partial.trim().toLowerCase()
									+ "%' "
									+ "or  lower(t.description_) like '%"
									+ partial.trim().toLowerCase()
									+ "%' "
									+ ") and t.name_ !='bljl2process' ")
					.getResultList();
		} catch (Exception e) {
			facesMessages.add("流程文件查询错误，请重试！");
			e.printStackTrace();
		}
		return matches;
	}
}
