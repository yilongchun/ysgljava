package com.vieking.sys.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.faces.DataModels;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.vieking.basicdata.model.NameQuery;
import com.vieking.basicdata.model.QueryFilter;
import com.vieking.basicdata.model.QueryParamDefine;
import com.vieking.role.model.User;
import com.vieking.sys.bean.NameQueryBean;
import com.vieking.sys.bean.QueryParamDefineBean;
import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.service.NameQueryManger;
import com.vieking.sys.util.CookiesUtil;
import com.vieking.sys.util.StringUtil;

public abstract class BaseQuery<T> implements Serializable {

	private static final long serialVersionUID = 5937587154984703263L;

	@SuppressWarnings("unused")
	private static final Pattern ORDER_COLUMN_PATTERN = Pattern
			.compile("^\\w+(\\.\\w+)*$");

	private Class<T> entityClass;

	@Logger
	protected Log log;

	@In(value = Const.CURRUSER, required = false)
	protected User currUser;

	@In(required = false)
	protected javax.faces.context.FacesContext facesContext;

	@In(required = false, value = "app.nameQueryManger")
	protected NameQueryManger nqm;

	protected boolean resultsStale = true;
	protected boolean countStale = true;
	protected boolean exp = false;

	/* 条件查询 全部查询 是否显示标志 */
	protected boolean seatchbool = false;

	private NameQueryBean nqb = new NameQueryBean();

	protected List<T> expResults = new ArrayList<T>();

	protected List<T> results = new ArrayList<T>();

	protected T selectedItem;

	private DataModel dataModel;

	protected Map<String, Object> resultChildCounts = new HashMap<String, Object>();

	public int viewPage = 0;

	private String order = "";

	private String auOrder = "";

	private boolean isPageSizeChanging;

	private String qhn;

	private String nqn;

	private BaseQueryHelp qhb;

	protected NameQuery nq;

	private ScopeType qhbScope;

	protected String name = "全部";

	protected String times;

	/** 查询列表 */
	public abstract void executeQuery() throws DaoException;

	/**
	 * 查询列表行数用于分页
	 * 
	 * @throws DaoException
	 */
	public abstract void executeCountQuery() throws DaoException;

	/** 查询行分组合计数据 用于在行中显示统计数据 */
	public abstract void executeQueryCounts() throws DaoException;

	/** 复位过滤器 */
	public abstract void resetFilter();

	public abstract EntityManager getEntityManager();

	public abstract FacesMessages getFacesMessages();

	public abstract String queryHelpName();

	public abstract String nameQueryName();

	public abstract BaseQueryHelp queryHelpInstance();

	protected abstract boolean isPostback();

	public abstract String getPageSizeCookiesName();

	public abstract String getQueryDesc();

	@Create
	public void create() {
		log.debug("初始化组件用户模式！", "");
	}

	/***
	 * 
	 * @param query
	 * @return
	 * @throws DaoException
	 */
	protected Query applyParmToQuery(Query query) throws DaoException {
		Map<String, QueryParamDefineBean> qpds = getNqb().getQpds();
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

	public void refresh() {
		clearDataModel();
	}

	public void refreshResults() throws DaoException {
		refresh();
		executeCountQuery();
		if (getQhb().getTotalRecordCount() == 0) {
			results.clear();
			resultChildCounts.clear();
		} else {
			executeQuery();
			executeQueryCounts();
		}

	}

	protected void clearDataModel() {
		dataModel = null;
	}

	@Transactional
	public DataModel getDataModel() {
		if (dataModel == null) {
			dataModel = DataModels.instance().getDataModel(results);
		}
		return dataModel;
	}

	/**
	 * Get the selected row of the JSF {@link DataModel}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public T getDataModelSelection() {
		return (T) getDataModel().getRowData();
	}

	/**
	 * Get the index of the selected row of the JSF {@link DataModel}
	 * 
	 */
	public int getDataModelSelectionIndex() {
		return getDataModel().getRowIndex();
	}

	public void initExpResults() throws DaoException {
		exp = true;
		executeQuery();
		exp = false;
	}

	public void prepareResults() throws DaoException {
		refresh();
		String cps = CookiesUtil.getCookies(getPageSizeCookiesName());
		if (cps != null) {
			try {
				if (!isPageSizeChanging & getQhb().getPage() == 0) {
					getQhb().setPageSize(Integer.valueOf(cps));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (countStale) {
			executeCountQuery();
			countStale = false;
		}
		if (resultsStale) {
			if (getQhb().getTotalRecordCount() == 0) {
				results.clear();
				resultChildCounts.clear();
			} else {
				executeQuery();
				executeQueryCounts();
			}
			resultsStale = false;
		}
		isPageSizeChanging = false;
		dataModel = null;
	}

	public void updateRefresh() {
		resultsStale = true;
		query();
	}

	public void applyFilter() {
		// 重新构造查询参数及语句
		System.out.println("Reset NameQueryBean ");
		nqb.reset();
		resetQuery();
	}

	public void next() {
		getQhb().next();
		resultsStale = true;
		query();
	}

	public void previous() {
		getQhb().previous();
		resultsStale = true;
		query();
	}

	public void first() {
		getQhb().first();
		resultsStale = true;
		query();
	}

	public void last() {
		getQhb().last();
		log.debug("last --------> {0}", getQhb().getPage());
		resultsStale = true;
		query();
	}

	public void gotoPage(int _page) {
		getQhb().setPage(_page);
		resultsStale = true;
		query();
	}

	public void pageChanged(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			log.debug("pageChanged --------> {0}", event.getNewValue());
			int _page = (Integer) event.getNewValue() - 1;
			if (_page < 0) {
				return;
			}
			if (getQhb().pageChanged(_page)) {
				resultsStale = true;
				query();
			}
		}
	}

	/***
	 * 查询数据，当页面处于 on-postback="false"时
	 */
	public void query() {
		if (!isPostback()) {
			try {
				prepareResults();
			} catch (DaoException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isNextAvailable() {
		return getQhb().isNextAvailable();
	}

	public boolean isPreviousAvailable() {
		return getQhb().isPreviousAvailable();
	}

	public void resetQuery() {
		resultsStale = true;
		countStale = true;
		first();
	}

	public void resetState() {
		resultsStale = true;
		countStale = true;
	}

	public long getStart() {
		return getQhb().getStart();
	}

	public int getOffset() {
		return getQhb().getOffset();
	}

	public int getPage() {
		return getQhb().getPage();
	}

	public void setPage(int page) {
		if (getQhb().getPage() != page) {
			getQhb().setPage(page);
			resultsStale = true;
		}
	}

	public int getPageSize() {
		return getQhb().getPageSize();
	}

	public void setPageSize(int pageSize) {
		if (getQhb().getPageSize() != pageSize) {
			getQhb().setPageSize(pageSize);
			resultsStale = true;
		}
	}

	public long getRecordCount() {
		return getQhb().getRecordCount();
	}

	public long getTotalRecordCount() {
		return getQhb().getTotalRecordCount();
	}

	protected void setTotalRecordCount(long totalRecordCount) {
		getQhb().setTotalRecordCount(totalRecordCount);
	}

	public List<T> getResults() {
		return results;
	}

	protected void setResults(List<T> results) {
		if (exp) {
			this.expResults = results;
		} else {
			this.results = null;
			this.results = results;
			getQhb().setRecordCount(results.size());
		}
	}

	public T getSelectedItem() {
		return selectedItem;
	}

	public int getIndexOfSelectedItem() {
		return results.indexOf(selectedItem);
	}

	public void orderChanged() {
		if (log.isDebugEnabled()) {
			log.debug("排序改变  #0", getOrder());
		}
		resetQuery();
	}

	public void pageSizeChanged(ValueChangeEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("每页记录数 改变 #0 to #1", event.getOldValue(),
					event.getNewValue());
		}
		if (event.getNewValue() != null) {
			CookiesUtil.setCookie(getPageSizeCookiesName(),
					String.valueOf(event.getNewValue()));
			setPageSize((Integer) event.getNewValue());
			isPageSizeChanging = true;
			first();
		}
	}

	/**
	 * 选择对象
	 */
	public void selectItem() {
		selectedItem = getDataModelSelection();
		log.debug("选择对象{0}", selectedItem);
	}

	public Object getResultChildCount(String key) {
		return resultChildCounts.get(key);
	}

	@SuppressWarnings("rawtypes")
	public void setResultChildCounts(List list) {
		resultChildCounts.clear();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			String key = (String) o[0];
			Long value = (Long) o[1];
			resultChildCounts.put(key, value);

		}
	}

	/**
	 * The order clause of the query
	 */

	public String getOrder() {
		String column = getQhb().getOrderColumn();

		if (column == null) {
			return order;
		}

		String direction = getQhb().getOrderDirection();

		if (direction == null) {
			return column;
		} else {
			return column + ' ' + direction;
		}
	}

	@SuppressWarnings({ "unused", "static-access" })
	private String sanitizeOrderDirection(String direction) {
		if (direction == null || direction.length() == 0) {
			return null;
		} else if (direction.equalsIgnoreCase(getQhb().DIR_ASC)) {
			return getQhb().DIR_ASC;
		} else if (direction.equalsIgnoreCase(getQhb().DIR_DESC)) {
			return getQhb().DIR_DESC;
		} else {
			throw new IllegalArgumentException("invalid order direction");
		}
	}

	public int getViewPage() {
		viewPage = getQhb().getPage() + 1;
		return viewPage;

	}

	public void setViewPage(int viewPage) {
		this.viewPage = viewPage;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getQhn() {
		if (StringUtils.isEmpty(qhn)) {
			qhn = queryHelpName();
		}
		return qhn;
	}

	public void setQhn(String qhn) throws DaoException {
		qhn = StringUtils.trim(qhn);
		if (StringUtils.isEmpty(qhn)) {
			throw new DaoException("设置查询帮助Bean名称不能为空！");
		}
		if (qhn.equals(this.qhn)) {
			return;
		} else {
			qhb = null;
		}
		this.qhn = qhn;
	}

	/**
	 * 设置NameQuery名称
	 * 
	 * @param qhn
	 * @throws DaoException
	 */
	public void setNqn(String _nqn) throws DaoException {
		_nqn = StringUtils.trim(_nqn);
		if (StringUtils.isEmpty(_nqn)) {
			throw new DaoException("设置命名查询名称不能为空！");
		}
		if (_nqn.equals(this.qhn)) {
			return;
		} else {
			nq = null;
			nqb.reset();
		}
		this.nqn = _nqn;
	}

	public String getNqn() {
		if (StringUtils.isEmpty(nqn)) {
			nqn = nameQueryName();
		}
		return nqn;
	}

	public void setQhb(BaseQueryHelp qhb) {
		this.qhb = qhb;
	}

	public BaseQueryHelp getQhb() {
		if (qhb == null) {
			if (getQhbScope() == null
					|| ScopeType.SESSION.equals(getQhbScope())) {
				qhb = (BaseQueryHelp) Contexts.getSessionContext()
						.get(getQhn());
				if (qhb == null) {
					qhb = queryHelpInstance();
					Contexts.getSessionContext().set(qhn, qhb);
				}
			} else if (ScopeType.CONVERSATION.equals(getQhbScope())) {
				qhb = (BaseQueryHelp) Contexts.getConversationContext().get(
						getQhn());
				if (qhb == null) {
					qhb = queryHelpInstance();
					Contexts.getConversationContext().set(qhn, qhb);
				}
			} else if (ScopeType.PAGE.equals(getQhbScope())) {
				qhb = (BaseQueryHelp) Contexts.getPageContext().get(getQhn());
				if (qhb == null) {
					qhb = queryHelpInstance();
					Contexts.getPageContext().set(qhn, qhb);
				}
			}
		}
		return qhb;
	}

	public List<T> getExpResults() {
		return expResults;
	}

	public void setExpResults(List<T> expResults) {
		this.expResults = expResults;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object elValue(String elStr) {
		Object value = null;
		try {
			ValueExpression elQhb = Expressions.instance()
					.createValueExpression("#{qhb}");
			elQhb.setValue(getQhb());
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

	public String getNqlStr() throws DaoException {
		if (evb(getNq().getEnabledEL(), true)) {
			String ql = getNq().getFindQuery();

			return ql;
		} else {
			throw new DaoException("无权使用此查询");
		}

	}

	public String getNqlCountStr() throws DaoException {
		if (evb(getNq().getEnabledEL(), true)) {
			String ql = getNq().getFindQuery();

			return ql;
		} else {
			throw new DaoException("无权使用此查询");
		}
	}

	public NameQuery getNq() throws DaoException {
		if (nq == null) {
			nq = nqm.query(getNqn());
		}
		return nq;
	}

	public void setNq(NameQuery nq) {
		this.nq = nq;
	}

	private void initNqb() throws DaoException {
		log.debug("重新初始化NameQueryBean ");
		if (!evb(getNq().getEnabledEL(), true)) {
			throw new DaoException("无权使用此查询");
		}
		nqb.setOrderBy(getNq().getOrderBy());
		// 如果用户未选择查询列，而且定义了默认查询，设置当前查询排序
		if ((getQhb().getOrderColumn() == null)
				&& (!StringUtils.isEmpty(nqb.getOrderBy()))) {
			order = nqb.getOrderBy();
		}
		auOrder = nq.getAuOrder();
		nqb.setUserNativeQuery(getNq().isUserNativeQuery());
		nqb.setQl(evs(getNq().getFindQuery(), "   "));
		nqb.setQlCount(StringUtils.trim(getNq().getCountQuery()));
		if (!StringUtils.isEmpty(nqb.getQlCount())) {
			nqb.setQlCount(evs(nqb.getQlCount(), null));
		}
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
		if ("今天".equals(times)) {
			nqb.setQl(nqb.getQl() + " and  to_days(o.ct) = to_days(now())");
			nqb.setQlCount(nqb.getQlCount()
					+ " and  to_days(o.ct) = to_days(now())");
		}
		if ("本周".equals(times)) {
			nqb.setQl(nqb.getQl()
					+ " and  yearweek(date_format(o.ct,'%Y-%m-%d')) = yearweek(now())");
			nqb.setQlCount(nqb.getQlCount()
					+ " and  yearweek(date_format(o.ct,'%Y-%m-%d')) = yearweek(now())");
		}
		if ("本月".equals(times)) {
			nqb.setQl(nqb.getQl()
					+ " and date_format(o.ct,'%Y%m') = date_format(curdate( ),'%Y%m')");
			nqb.setQlCount(nqb.getQlCount()
					+ " and date_format(o.ct,'%Y%m') = date_format(curdate( ),'%Y%m')");
		}
		if ("一年".equals(times)) {
			nqb.setQl(nqb.getQl() + " and year(o.ct)=year(curdate())");
			nqb.setQlCount(nqb.getQlCount() + " and year(o.ct)=year(curdate())");
		}

		// 清理未使用嵌入标签
		nqb.setQl(nqb.getQl().replaceAll(
				"@@([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@@", "  "));
		if (!StringUtils.isEmpty(nqb.getQlCount())) {
			nqb.setQlCount(nqb.getQlCount().replaceAll(
					"@@([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@@", "  "));
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

	public NameQueryBean getNqb() throws DaoException {
		if (nqb.isNeedInit()) {
			initNqb();
		}
		return nqb;
	}

	public void setNqb(NameQueryBean nqb) {
		this.nqb = nqb;
	}

	public void setQhbPv(String pn, int pl, Object value) {
		getQhb().plParm(pn, pl).setValue(value);
	}

	public Object getQhbPv(String pn) {
		return getQhb().pv(pn);
	}

	public String getQhbPvStr(String pn) {
		Object pv = getQhbPv(pn);
		return pv == null ? null : StringUtil.toStr(pv, null);
	}

	public void setQhbPv(String qhbName, String pn, int pl, Object value) {
		BaseQueryHelp qhb = (BaseQueryHelp) Contexts.getSessionContext().get(
				qhbName);
		qhb.plParm(pn, pl).setValue(value);
	}

	public Object getQhbPv(String qhbName, String pn) {

		BaseQueryHelp qhb = (BaseQueryHelp) Contexts.getSessionContext().get(
				qhbName);
		if (qhb != null) {
			return qhb.pv(pn);
		} else {
			return null;
		}
	}

	public String getQhbPvStr(String qhbName, String pn) {
		Object pv = getQhbPv(qhbName, pn);
		return pv == null ? null : StringUtil.toStr(pv, null);
	}

	/**
	 * Get the class of the entity being managed. <br />
	 * If not explicitly specified, the generic type of implementation is used.
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getEntityClass() {
		if (entityClass == null) {
			Type type = getClass().getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				ParameterizedType paramType = (ParameterizedType) type;
				entityClass = (Class<T>) paramType.getActualTypeArguments()[0];
			} else {
				throw new IllegalArgumentException(
						"Could not guess entity class by reflection");
			}
		}
		return entityClass;
	}

	public String getAuOrder() {
		return auOrder;
	}

	public void setAuOrder(String auOrder) {
		this.auOrder = auOrder;
	}

	public boolean isExp() {
		return exp;
	}

	public boolean isSeatchbool() {
		return seatchbool;
	}

	public void setSeatchbool(boolean seatchbool) {
		this.seatchbool = seatchbool;
	}

	public ScopeType getQhbScope() {
		return qhbScope;
	}

	/**
	 * 通过字符串设置 qhb 组件空间范围
	 * 
	 * @param qhbScopeStr
	 *            字符描述 组件空间范围
	 * @throws DaoException
	 */
	public void setQhbScopeStr(String qhbScopeStr) throws DaoException {
		log.debug("设置qhbScopeStr-->{0}", qhbScopeStr);
		qhbScopeStr = StringUtils.trim(StringUtils.upperCase(qhbScopeStr));
		if (StringUtils.isEmpty(qhbScopeStr)) {
			throw new DaoException("设置qhb Scope空间不能为空！");
		}
		ScopeType _qhbScope = ScopeType.valueOf(qhbScopeStr);
		if (_qhbScope == null) {
			throw DaoException.instance("设置qhb Scope空间 值【{0}】无效", qhbScopeStr);
		}
		setQhbScope(_qhbScope);
	}

	public void setQhbScope(ScopeType qhbScope) throws DaoException {
		if (qhbScope == null) {
			throw new DaoException("错误,设置qhb空间属性为空！");
		}
		if (this.qhb != null) {
			throw new DaoException("qhb已创建，不能重新设置组件空间属性！");
		}
		if (this.qhbScope != null) {
			throw new DaoException("qhb空间属性已设置，不能重复设置！");
		}
		if (ScopeType.APPLICATION.equals(qhbScope)
				|| ScopeType.BUSINESS_PROCESS.equals(qhbScope)
				|| ScopeType.EVENT.equals(qhbScope)
				|| ScopeType.METHOD.equals(qhbScope)
				|| ScopeType.UNSPECIFIED.equals(qhbScope)) {
			throw DaoException.instance("错误，不允许设置的qhb空间属性-->{0}", qhbScope);
		}
		this.qhbScope = qhbScope;
		setQhb(null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

}
