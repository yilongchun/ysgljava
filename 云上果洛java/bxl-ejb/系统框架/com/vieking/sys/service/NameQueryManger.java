package com.vieking.sys.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.vieking.basicdata.model.NameQuery;
import com.vieking.basicdata.model.QueryDefine;
import com.vieking.basicdata.model.QueryFilter;
import com.vieking.basicdata.model.QueryParamDefine;
import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.DaoException;

@Name("app.nameQueryManger")
@Scope(ScopeType.APPLICATION)
@Startup(depends = "app.config")
public class NameQueryManger implements Serializable {

	private static final long serialVersionUID = 5504980973800632206L;

	@Logger
	protected Log log;

	@In
	protected FacesMessages facesMessages;

	private Map<String, NameQuery> nameQuerys = new HashMap<String, NameQuery>();

	@In
	protected EntityManager entityManager;

	public void reload(String name) throws DaoException {
		log.debug("reload---> {0}", name);
		if (StringUtils.isEmpty(name)) {
			throw new DaoException("命名查询命名为空字符！");
		}
		name = name.toLowerCase();
		NameQuery nq = entity(name);
		if (nq == null)
			return;
		if (nameQuerys.containsKey(name)) {
			nameQuerys.remove(name);
		}
		nameQuerys.put(name, loadNameQuery(nq));
	}

	public NameQuery loadNameQuery(NameQuery e_nq) {
		// public NameQuery(String des, QueryDefineType type, String enabledEL,
		// String name,
		// boolean userNativeQuery, String findQuery, String orderBy, String
		// countQuery)
		NameQuery nq = new NameQuery(e_nq.getDes(), e_nq.getType(),
				e_nq.getEnabledEL(), e_nq.getName(), e_nq.isUserNativeQuery(),
				e_nq.getFindQuery(), e_nq.getOrderBy(), e_nq.getCountQuery(),
				e_nq.getAuOrder());
		nq.setOid(e_nq.getId());
		loadParams(nq, e_nq);
		nq.getQfs().clear();
		for (Iterator<QueryFilter> iterator = e_nq.getQfs().iterator(); iterator
				.hasNext();) {
			QueryFilter e_qf = iterator.next();
			// public QueryFilter(String des, String
			// enabledEL, NameQuery nameQuery,
			// String filter, String insTag, int sort)
			QueryFilter qf = new QueryFilter(e_qf.getDes(),
					e_qf.getEnabledEL(), e_qf.getNameQuery(), e_qf.getFilter(),
					e_qf.getInsTag(), e_qf.getSort());
			loadParams(qf, e_qf);
			nq.getQfs().add(qf);
		}
		return nq;
	}

	public void loadParams(QueryDefine qd, QueryDefine e_qd) {
		qd.getQpds().clear();
		for (Iterator<QueryParamDefine> iterator = e_qd.getQpds().iterator(); iterator
				.hasNext();) {
			QueryParamDefine e_qpd = iterator.next();
			// public QueryParamDefine(QueryDefine queryDefine, String name,
			// String des, DataType type,
			// String valueEL, String defineValue, int sort)
			QueryParamDefine qpd = new QueryParamDefine(qd, e_qpd.getName(),
					e_qpd.getDes(), e_qpd.getType(), e_qpd.getValueEL(),
					e_qpd.getDefineValue(), e_qpd.getSort());
			qd.getQpds().add(qpd);
		}
	}

	@SuppressWarnings("unchecked")
	public void loadAll() {
		List<NameQuery> ls = entityManager.createQuery(
				"from NameQuery o order by o.name ").getResultList();
		for (Iterator<NameQuery> iterator = ls.iterator(); iterator.hasNext();) {
			NameQuery nq = iterator.next();
			nameQuerys.put(nq.getName().toLowerCase(), loadNameQuery(nq));
		}
		entityManager.clear();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public void reloadAll() {
		List<String> ls = entityManager.createQuery(
				"select o.name from NameQuery o order by o.name ")
				.getResultList();
		try {
			for (Iterator<String> iterator = ls.iterator(); iterator.hasNext();) {
				String nqn = iterator.next();
				reload(nqn);
			}

		} catch (DaoException e) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		entityManager.clear();
	}

	@Observer(create = false, value = Const.NAMEQUERYCHANGED)
	public void onChanged(String name) throws DaoException {
		reload(name);
	}

	@Create
	public void init() {
		nameQuerys.clear();
		loadAll();
	}

	/**
	 * 此方法用于 调试程序时使用
	 * 
	 * @param nq
	 *            命名查询
	 * @throws DaoException
	 */
	public void putQuery(NameQuery nq) throws DaoException {
		if (StringUtils.isEmpty(nq.getName())) {
			throw new DaoException("命名查询命名为空字符！");
		}
		String name = nq.getName().toLowerCase();
		NameQuery q = nameQuerys.get(name);
		if (q == null) {
			nameQuerys.put(nq.getName(), nq);
		} else {
			nameQuerys.remove(name);
			nameQuerys.put(name, nq);
		}

	}

	@SuppressWarnings("unchecked")
	public NameQuery entity(String name) {
		List<NameQuery> ls = entityManager
				.createQuery("from NameQuery o where o.name=:name ")
				.setParameter("name", name).getResultList();
		if (ls.isEmpty()) {
			return null;
		} else {
			return ls.get(0);
		}
	}

	/***
	 * 
	 * @param name
	 *            查询名称
	 * @return 命名查询
	 * @throws DaoException
	 */
	public NameQuery query(String name) throws DaoException {
		name = name.toLowerCase().trim();
		NameQuery result = nameQuerys.get(name);
		if (result == null) {

			result = entity(name);
			if (result != null) {
				nameQuerys.put(name, result);
			}
		}
		if (result == null) {
			throw new DaoException("命名查询【" + name + "】定义未找到！");
		} else {
			return result;
		}
	}

	/***
	 * 
	 * @param name
	 *            查询名称
	 * @return 命名查询
	 * @throws DaoException
	 */
	@SuppressWarnings("deprecation")
	public NameQuery qq(String name) {
		try {
			return query(name);

		} catch (DaoException e) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, e.getMessage());
			return null;
		}
	}

	/***
	 * 
	 * @param name
	 *            查询名称
	 * @return 主查询 语句
	 * @throws DaoException
	 */
	public String findQuery(String name) throws DaoException {
		return query(name).getFindQuery();
	}

	/***
	 * 
	 * @param name
	 *            查询名称
	 * @return 数量查询语句
	 * @throws DaoException
	 */
	public String countQuery(String name) throws DaoException {
		return query(name).getCountQuery();
	}

	public Map<String, NameQuery> getNameQuerys() {
		return nameQuerys;
	}

}
