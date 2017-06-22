package com.vieking.basicdata.dao;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;

import com.vieking.sys.base.BaseDaoHibernate;
import com.vieking.sys.bean.QueryParamBean;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.model.BaseEntity;

@Name("sys.nqtDao")
@AutoCreate
public class NqtDao extends BaseDaoHibernate {

	private static final long serialVersionUID = -189228877334733044L;

	@SuppressWarnings("rawtypes")
	public Object simpleQuery(String qlStr, boolean isNativeQuery) {
		log.debug("简单查询query【{0}】查询语句 {1}", qlStr);
		qlStr = qlStr.replaceAll("\\^", "'");
		try {
			Query query = null;
			if (isNativeQuery) {
				query = entityManager.createNativeQuery(qlStr);
			} else {
				query = entityManager.createQuery(qlStr);
			}
			List ls = query.getResultList();
			if (ls.isEmpty()) {
				return null;
			} else if (ls.size() == 1) {
				return ls.get(0);
			}
			return ls;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public Object list(String qlStr, boolean isNativeQuery) {
		qlStr = qlStr.replaceAll("\\^", "'");
		log.debug("简单查询query【{0}】查询语句 {1}", qlStr);
		try {
			Query query = null;
			if (isNativeQuery) {
				query = entityManager.createNativeQuery(qlStr);
			} else {
				query = entityManager.createQuery(qlStr);
			}
			List ls = query.getResultList();
			return ls;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean isItemUnique(BaseEntity obj, String nqn,
			QueryParamBean... params) throws DaoException {
		return isItemUnique(obj, nqn, null, params);
	}

	@SuppressWarnings("rawtypes")
	public boolean isItemUnique(BaseEntity item, String nqn, String objDesc,
			QueryParamBean... params) throws DaoException {
		List ls = query(nqn, null, params);
		if (ls.isEmpty()) {
			return true;
		}
		if (ls.size() == 1) {
			if (((BaseEntity) ls.get(0)).getId().equals(item.getId())) {
				return true;
			} else {
				return false;
			}
		} else {
			new DaoException("{0} 存在多个重复数据！ ",
					StringUtils.isEmpty(objDesc) ? item.toString() : objDesc);
		}
		return false;
	}
}
