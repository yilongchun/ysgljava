package com.vieking.sys.base;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

import com.vieking.basicdata.model.NameQuery;
import com.vieking.basicdata.model.QueryFilter;
import com.vieking.basicdata.model.QueryParamDefine;
import com.vieking.sys.base.BaseQueryHelp;
import com.vieking.sys.bean.NameQueryBean;
import com.vieking.sys.bean.QueryParamBean;
import com.vieking.sys.bean.QueryParamDefineBean;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.service.NameQueryManger;
import com.vieking.sys.util.ExpressUtil;
import com.vieking.sys.util.StringUtil;

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
abstract public class BaseDaoHibernate extends BaseDao {

	private static final long serialVersionUID = 2355525380958545251L;

	@In(value = "app.nameQueryManger")
	protected NameQueryManger nqm;

	protected final Log log = Logging.getLog(getClass());

	private NameQueryBean nqb = new NameQueryBean();

	private BaseQueryHelp qhb;

	private NameQuery nq;

	private String nqn;

	public NameQuery getNq() throws DaoException {
		if (nq == null) {
			nq = nqm.query(nqn);
		} else if (!nq.getName().equalsIgnoreCase(nqn)) {
			nq = nqm.query(nqn);
			nqb.reset();
		}
		return nq;
	}

	private void initNqb() throws DaoException {
		log.debug("DAO重新初始化NameQueryBean ");
		if (!evb(getNq().getEnabledEL(), true)) {
			throw new DaoException("无权使用此查询");
		}
		nqb.setOrderBy(getNq().getOrderBy());
		nqb.setUserNativeQuery(getNq().isUserNativeQuery());
		nqb.setQl(evs(getNq().getFindQuery(), "   "));
		for (Iterator<QueryParamDefine> iterator = getNq().getQpds().iterator(); iterator
				.hasNext();) {
			QueryParamDefine qpd = iterator.next();
			nqb.putQpd(
					qpd.getName(),
					new QueryParamDefineBean(qpd.getName(), qpd.getDes(), qpd
							.getType(), qpd.getValueEL(), qpd.getDefineValue()));
		}
		for (Iterator<QueryFilter> iterator = getNq().getQfs().iterator(); iterator
				.hasNext();) {
			QueryFilter qf = iterator.next();
			initQueryFilter(qf);
		}
		// 清理未使用嵌入标签
		nqb.setQl(nqb.getQl().replaceAll(
				"@@([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@@", "  "));
		if (!StringUtils.isEmpty(nqb.getQlCount())) {
			nqb.setQlCount(nqb.getQlCount().replaceAll(
					"@@([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@@", "  "));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object elValue(String elStr) {
		Object value = null;
		try {
			ValueExpression elQhb = Expressions.instance()
					.createValueExpression("#{qhb}");
			elQhb.setValue(qhb);
			ValueExpression el = Expressions.instance().createValueExpression(
					elStr);
			value = el.getValue();
		} catch (Exception e) {
			value = null;
		}
		return value;
	}

	/**
	 * 
	 * @param elStr
	 *            表达式字符串
	 * @param isNullReturn
	 *            el为Null 时返回值
	 * @return Boolean 结果
	 */
	public Boolean evb(String elStr, boolean isNullReturn) {
		if (StringUtils.isEmpty(elStr)) {
			return isNullReturn;
		}
		Object _v = elValue(elStr);
		if (_v instanceof Boolean) {
			return (Boolean) _v;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param elStr
	 *            表达式字符串
	 * @param isNullReturn
	 *            el为Null 时返回值
	 * @return String 结果
	 */
	public String evs(String elStr, String isNullReturn) {
		if (StringUtils.isEmpty(elStr)) {
			return isNullReturn;
		}
		Object _v = elValue(elStr);
		if (_v instanceof String) {
			return String.valueOf(_v);
		} else {
			return isNullReturn;
		}
	}

	/**
	 * 初始化查询过滤
	 * 
	 * @param qf
	 *            查询过滤定义
	 */
	public void initQueryFilter(QueryFilter qf) {
		// 如果判断不需要处理
		if (!evb(qf.getEnabledEL(), true)) {
			return;
		}
		if (StringUtils.isEmpty(qf.getInsTag())) {
			nqb.setQl(nqb.getQl() + "   \r\n  " + evs(qf.getFilter(), "  ")
					+ "   \r\n  ");
			if (!StringUtils.isEmpty(nqb.getQlCount())) {
				nqb.setQlCount(nqb.getQlCount() + "   \r\n  "
						+ evs(qf.getFilter(), "  ") + "   \r\n  ");
			}
		} else {
			nqb.setQl(nqb.getQl().replaceAll(qf.getInsTag(),
					evs(qf.getFilter(), "  ")));
			if (!StringUtils.isEmpty(nqb.getQlCount())) {
				nqb.setQlCount(nqb.getQlCount().replaceAll(qf.getInsTag(),
						evs(qf.getFilter(), "  ")));
			}
		}
		for (Iterator<QueryParamDefine> iterator = qf.getQpds().iterator(); iterator
				.hasNext();) {
			QueryParamDefine qpd = iterator.next();
			nqb.putQpd(
					qpd.getName(),
					new QueryParamDefineBean(qpd.getName(), qpd.getDes(), qpd
							.getType(), qpd.getValueEL(), qpd.getDefineValue()));
		}
	}

	/***
	 * 
	 * @param query
	 * @return
	 * @throws DaoException
	 */
	protected Query applyParmToQuery(Query query) throws DaoException {
		Map<String, QueryParamDefineBean> qpds = nqb.getQpds();
		for (Iterator<String> iterator = qpds.keySet().iterator(); iterator
				.hasNext();) {
			String name = iterator.next();
			QueryParamDefineBean qpdb = qpds.get(name);
			// 如果没有定义取值表达式，则从Qhb中获取
			if (StringUtils.isEmpty(qpdb.getValueEL())) {
				Object value = qhb.pv(name);
				query.setParameter(name, value);
			} else {
				query.setParameter(name, elValue(qpdb.getValueEL()));
			}
		}
		return query;
	}

	private Query applyCriteriaToQuery(Query query) throws DaoException {
		applyParmToQuery(query);
		return query;
	}

	public BaseQueryHelp initQhb(Object... nvs) throws DaoException {
		BaseQueryHelp _qhb = new BaseQueryHelp();
		if (nvs.length % 2 != 0) {
			throw new DaoException("错误，参数应为偶数！");
		}
		for (int i = 0; i < nvs.length; i = i + 2) {
			if (nvs[i] != null && nvs[i] instanceof String) {

				String _pn = (String) nvs[i];
				if (StringUtils.isEmpty(_pn))
					continue;
				QueryParamBean qpb = new QueryParamBean();
				qpb.setPn(_pn);
				// 如果值类型为 字符串，分析是否需要计算
				if (nvs[i + 1] != null && nvs[i + 1] instanceof String) {
					String _valStr = (String) nvs[i + 1];
					if (!StringUtils.isEmpty(_valStr)) {
						if (StringUtil.hasExp(_valStr)) {
							_valStr = _valStr.replaceAll("\\^", "'");
							Object _val = ExpressUtil.elValue(_valStr);
							qpb.setValue(_val);
						} else {
							qpb.setValue(_valStr);
						}
					} else {
						qpb.setValue(null);
					}
				} else
				// 如果值类型为非字符串数据 放入参数中
				{
					qpb.setValue(nvs[i + 1]);
				}
				if (qpb != null && !qpb.isDataNull()) {
					_qhb.getQpbs().put(qpb.getPn(), qpb);
				}
			} else {
				continue;
			}
		}
		return _qhb;
	}

	@SuppressWarnings("rawtypes")
	public List queryNvs(String _nqn, String orderBy, Object... nvs)
			throws DaoException {
		BaseQueryHelp _qhb = initQhb(nvs);
		return query(_nqn, orderBy, _qhb);
	}

	public long countQueryNvs(String _nqn, Object... nvs) throws DaoException {
		BaseQueryHelp _qhb = initQhb(nvs);
		return countQuery(_nqn, _qhb);
	}

	public long countQuery(String _nqn, QueryParamBean... params)
			throws DaoException {
		BaseQueryHelp _qhb = new BaseQueryHelp();
		for (int i = 0; i < params.length; i++) {
			if (params[i] == null)
				continue;
			QueryParamBean qpb = params[i];
			if (qpb != null && !qpb.isDataNull()) {
				_qhb.getQpbs().put(qpb.getPn(), qpb);
			}
		}
		return countQuery(_nqn, _qhb);
	}

	@SuppressWarnings("rawtypes")
	public List query(String _nqn, String orderBy, QueryParamBean... params)
			throws DaoException {
		BaseQueryHelp _qhb = new BaseQueryHelp();
		for (int i = 0; i < params.length; i++) {
			if (params[i] == null)
				continue;
			QueryParamBean qpb = params[i];
			if (qpb != null && !qpb.isDataNull()) {
				_qhb.getQpbs().put(qpb.getPn(), qpb);
			}
		}
		return query(_nqn, orderBy, _qhb);
	}

	public Long countQuery(String _nqn, BaseQueryHelp _qhb)
			throws DaoException {
		if (_qhb == null) {
			throw new DaoException("参数Bean不能为空！");
		}
		this.qhb = _qhb;
		this.nqn = _nqn;
		initNqb();
		String ql = nqb.getQl();
		log.debug("命名查询countQuery【{0}】查询语句 {1}", _nqn, ql);
		Query query = null;
		if (nq.isUserNativeQuery()) {
			query = applyCriteriaToQuery(entityManager.createNativeQuery(ql));
		} else {
			query = applyCriteriaToQuery(entityManager.createQuery(ql));
		}
		Object _val = query.getSingleResult();
		if (_val == null)
			return 0l;
		if (_val instanceof BigDecimal) {
			BigDecimal _dbv = (BigDecimal) _val;
			return _dbv.longValue();

		}
		if (_val instanceof Long) {
			Long _lv = (Long) _val;
			return _lv.longValue();

		}
		if (_val instanceof Integer) {
			Integer _iv = (Integer) _val;
			return _iv.longValue();
		}
		if (_val instanceof String) {
			String _strv = (String) _val;
			return new Long(_strv).longValue();
		}
		return 0l;

	}

	@SuppressWarnings("rawtypes")
	public List query(String _nqn, String orderBy, BaseQueryHelp _qhb)
			throws DaoException {
		if (_qhb == null) {
			throw new DaoException("参数Bean不能为空！");
		}
		this.qhb = _qhb;
		this.nqn = _nqn;
		initNqb();
		String nqlStr = nqb.getQl();
		String ql = "";
		if (StringUtils.isNotEmpty(orderBy)) {
			ql = nqlStr + " order by " + orderBy;
		} else {
			ql = nqlStr
					+ (StringUtils.isNotEmpty(nqb.getOrderBy()) ? " order by "
							+ nqb.getOrderBy() : "");
		}
		log.debug("命名查询query【{0}】查询语句 {1}", _nqn, ql);
		Query query = null;
		if (nq.isUserNativeQuery()) {
			query = applyCriteriaToQuery(entityManager.createNativeQuery(ql));
		} else {
			query = applyCriteriaToQuery(entityManager.createQuery(ql));
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public long getNqMaxID(String _nqn, String cdlike, Integer cdlen)
			throws DaoException {
		List<String> ls = query(_nqn, "", new QueryParamBean("cdlike",
				cdlike + '%'), new QueryParamBean("cdlen", cdlen));
		if (ls.isEmpty()) {
			return 0;
		} else {
			String maxCode = ls.get(0);
			if (StringUtils.isEmpty(maxCode))
				return 0;
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
	
	
	@Transactional(TransactionPropagationType.REQUIRED)
	public void upData(String model,int verifcode, List<String> codes) {		
		entityManager
				.createQuery(
						"update "+model+" o set o.syncSign=((o.syncSign+:verifcode)- bitand(o.syncSign, :verifcode))  where  o.id  in (:codes)")  
				.setParameter("verifcode", verifcode)
				.setParameter("codes", codes).executeUpdate();
	}

}
