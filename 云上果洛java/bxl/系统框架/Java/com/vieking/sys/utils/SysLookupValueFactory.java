/*
 * EDAS2 - TetraTech, Inc.
 *
 * Distributable under GPL license.
 * See terms of license at gnu.org.
 */
package com.vieking.sys.utils;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.vieking.basicdata.enumerable.CantonLevel;
import com.vieking.file.model.DocLink;
import com.vieking.role.model.Application;
import com.vieking.role.model.User;
import com.vieking.sys.exception.Const;
import com.vieking.sys.util.JsfUtil;
import com.vieking.sys.util.Message;
import com.vieking.sys.util.ReflectionUtil;

@Name("sys.lvf")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class SysLookupValueFactory {
	@Logger
	private Log log;
	@In
	private EntityManager entityManager;

	@In(value = Const.CURRUSER)
	protected User currUser;

	@Factory(value = "antonLevel", scope = ScopeType.APPLICATION)
	public CantonLevel[] elCantonLevel() {
		return CantonLevel.values();
	}

	@Factory(value = "applications", scope = ScopeType.SESSION)
	public List<Application> applications() {

		return findAll(Application.class, "name");

	}

	/***
	 * 
	 * @param clazzName
	 *            枚举类名
	 * @return 所有枚举数组
	 */
	@SuppressWarnings("rawtypes")
	public Enum[] enmuValues(String clazzName) {
		try {
			Class c = Class.forName(clazzName);
			if (c.isEnum()) {
				return (Enum[]) c.getEnumConstants();
			}
		} catch (ClassNotFoundException e) {
			JsfUtil.setPopMsg(new Message("获取枚举列表错误", "枚举【" + clazzName
					+ "】类没有找到！"));
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 
	 * @param clazzName
	 *            枚举类名
	 * @param method
	 *            静态方法名
	 * @return 枚举列表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Enum> evls(String clazzName, String method) {
		try {
			Class c = Class.forName(clazzName);
			if (c.isEnum()) {
				return (List<Enum>) ReflectionUtil.invokeStatic(c, method);
			}
		} catch (ClassNotFoundException e) {
			JsfUtil.setPopMsg(new Message("获取枚举列表错误", "枚举【" + clazzName
					+ "】类没有找到！"));
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 
	 * @param clazzName
	 *            枚举类名
	 * @param method
	 *            静态方法名
	 * @return 枚举列表
	 */
	@SuppressWarnings("rawtypes")
	public Enum enumVaule(String clazzName, String method) {
		try {
			Class c = Class.forName(clazzName);
			if (c.isEnum()) {
				return (Enum) ReflectionUtil.invokeStatic(c, method);
			}
		} catch (ClassNotFoundException e) {
			JsfUtil.setPopMsg(new Message("获取枚举列表错误", "枚举【" + clazzName
					+ "】类没有找到！"));
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 
	 * @param clazzName
	 *            枚举类名
	 * @return 所有枚举列表
	 */
	@SuppressWarnings("rawtypes")
	public List<Enum> evls(String clazzName) {
		Enum[] eas = enmuValues(clazzName);
		if (eas != null) {
			return Arrays.asList(eas);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<DocLink> filterAppLinks(String docType, Application app,
			String entId) {
		List<DocLink> ls = entityManager
				.createQuery(
						" from DocLink o where  o.document.docType.docType=:docType "
								+ "  and o.ebcn=:ebcn   and o.keyProp=:keyProp "
								+ "  and o.keyValue=:keyValue")
				.setParameter("docType", docType)
				.setParameter("ebcn", app.getEbcn())
				.setParameter("keyProp", app.getKeyProp())
				.setParameter("keyValue", entId).getResultList();
		return ls;
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

	public static void main(String[] args) {

	}
}
