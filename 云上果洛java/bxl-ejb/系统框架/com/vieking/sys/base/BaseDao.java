package com.vieking.sys.base;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.sf.querytool.FilterTag;
import net.sf.querytool.provider.QueryTool;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.In;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

import com.vieking.basicdata.dao.DictionaryDao;
import com.vieking.basicdata.dao.NqtDao;
import com.vieking.sys.exception.DaoException;

/**
 * Dao </p>
 * 
 * <p>
 * <a href="BaseDaoHibernate.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision:$<br>
 *          $Id:$
 */
abstract public class BaseDao implements Serializable {

	private static final long serialVersionUID = -4314645728839605040L;

	@In
	protected EntityManager entityManager;
	
	@In(value = "dictionaryDao")
	protected DictionaryDao dictionaryDao;

	@In("sys.nqtDao")
	protected NqtDao nqtDao;
	
	protected final Log log = Logging.getLog(getClass());

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object load(Class clazz, Object id) {
		return entityManager.find(clazz, id);
	}

	public long getMaxID(String nameQuery, String cdlike, Integer cdlen)
			throws DaoException {
		String maxCode = (String) entityManager.createNamedQuery(nameQuery)
				.setParameter("cdlike", cdlike + '%')
				.setParameter("cdlen", cdlen).getSingleResult();
		if (maxCode == null) {
			return 0;
		} else {
			try {
				String tmpNumber = "";
				if (!StringUtils.isEmpty(cdlike)) {
					tmpNumber = maxCode.substring(cdlike.length(),
							maxCode.length());
				} else {
					tmpNumber = maxCode;
				}
				return new Long(tmpNumber).longValue();
			} catch (Exception e) {
				e.printStackTrace();
				throw new DaoException("编码生成错误", "编码管理", "001");
			}
		}
	}

	public long czgetMaxID(String nameQuery, String cdlike, Integer cdlen)
			throws DaoException {
		String maxCode = (String) entityManager.createNamedQuery(nameQuery)
				.setParameter("cdlike", cdlike + '%')
				.setParameter("cdlen", cdlen).getSingleResult();
		if (maxCode == null) {
			return 0;
		} else {
			try {
				String tmpNumber = "";
				if (!StringUtils.isEmpty(cdlike)) {
					tmpNumber = maxCode.substring(cdlike.length(),
							maxCode.length());
				} else {
					tmpNumber = maxCode;
				}
				return new Long(tmpNumber).longValue();
			} catch (Exception e) {
				e.printStackTrace();
				throw new DaoException("编码生成错误", "编码管理", "001");
			}
		}
	}

	public long getMaxIDQuery(String query, String cdlike, long cdlen)
			throws DaoException {
		String maxCode = (String) entityManager.createQuery(query)
				.setParameter("cdlike", cdlike + '%')
				.setParameter("cdlen", cdlen).getSingleResult();
		if (maxCode == null) {
			return 0;
		} else {
			try {
				String tmpNumber = "";
				if (!StringUtils.isEmpty(cdlike)) {
					tmpNumber = maxCode.substring(cdlike.length(),
							maxCode.length());
				} else {
					tmpNumber = maxCode;
				}
				return new Long(tmpNumber).longValue();
			} catch (Exception e) {
				e.printStackTrace();
				throw new DaoException("编码生成错误", "编码管理", "001");
			}
		}
	}

	public static final QueryTool QT = new QueryTool(new FilterTag("ignore",
			"<", ">", "</", ">"));

	public static final QueryTool QTBracket = new QueryTool(new FilterTag(
			"ignore", "{", " ", " ", "}"));

	@SuppressWarnings("rawtypes")
	public void setQueryBeanParam(Query result, Map params) {
		for (int i = 0; i < params.size(); i++) {
			Object e = params.get(new Integer(i + 1));
			if (e.getClass().equals(String.class)) {
				result.setParameter(i, (String) e);
				continue;
			}
			if (e.getClass().equals(Integer.class)) {
				result.setParameter(i, ((Integer) e).intValue());
				continue;
			}
			if (e.getClass().equals(Long.class)) {
				result.setParameter(i, ((Long) e).longValue());
				continue;
			}
			if (e.getClass().equals(Boolean.class)) {
				result.setParameter(i, ((Boolean) e).booleanValue());
				continue;
			}
			if (e.getClass().equals(Object[].class)) {
				continue;
			}
			if (e instanceof Collection) {
				continue;
			}
			if (e instanceof Calendar) {
				result.setParameter(i, (Calendar) e);
				continue;
			}
//			if (e instanceof TrueOrFalse) {
//				result.setParameter(i, TrueOrFalse.是.equals((TrueOrFalse) e));
//				continue;
//			}
			result.setParameter(i, e);
		}
	}

}
