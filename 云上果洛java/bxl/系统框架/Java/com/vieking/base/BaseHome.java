package com.vieking.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.bpm.Actor;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Events;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.faces.Redirect;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.framework.EntityNotFoundException;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

import com.vieking.basicdata.dao.DictionaryDao;
import com.vieking.basicdata.dao.NqtDao;
import com.vieking.basicdata.model.Dictionary;
import com.vieking.role.model.Application;
import com.vieking.role.model.User;
import com.vieking.sys.base.BaseQueryHelp;
import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.service.NameQueryManger;
import com.vieking.sys.util.Config;
import com.vieking.sys.util.StringUtil;

/**
 * <br>
 * <br>
 * 
 * <p>
 * <a href="BaseHome.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
public abstract class BaseHome<T> extends EntityHome<T> {

	private static final long serialVersionUID = 7941561227611883684L;

	@In
	protected EntityManager entityManager;

	@In
	protected FacesMessages facesMessages;

	@In(create = true)
	protected Redirect redirect;

	@In(value = "dictionaryDao")
	protected DictionaryDao dictionaryDao;

	protected String localCantonCode;

	@In(value = "app.config")
	protected Config config;

	@Logger
	protected Log log;

	@In(value = Const.CURRUSER, required = false)
	protected User currUser;

	@In(required = false)
	protected Actor actor;

	@In(required = false)
	protected Identity identity;

	@In(required = false, value = "app.nameQueryManger")
	protected NameQueryManger nqm;

	@In("sys.nqtDao")
	protected NqtDao nqtDao;

	protected String lastStateChange;

	protected String method;

	protected String fromCid;

	protected Boolean autoNew;

	protected String fromUrl;

	protected Long taskId;

	protected String componentName;

	protected Application app;

	/** 规则模板名称 */
	protected String rtn;

	/** 规则运行结果 Map String：用途 Object 运行结果，运行异常返回false */
	public Map<String, Object> ruleRunResultMap = new HashMap<String, Object>();

	/**
	 * persist方法调用
	 * 
	 * persist_update_doclink_keyValue
	 * 
	 * 转换对象附件dl.keyValue tmpId--->id
	 * 
	 * @param tmpid
	 *            对象临时Id
	 * @param id
	 *            对象持久化后Id
	 */
	public void pudk(String tmpid, String id) {
		entityManager
				.createQuery(
						"update DocLink  dl set dl.keyValue=:id where  dl.keyValue=:tmpId")
				.setParameter("id", id).setParameter("tmpId", tmpid)
				.executeUpdate();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object ruleElValue(String elStr) {
		Object value = null;
		try {
			ValueExpression elHome = Expressions.instance()
					.createValueExpression("#{_rrHome}");
			elHome.setValue(this);
			ValueExpression elObj = Expressions.instance()
					.createValueExpression("#{_rrObj}");
			elObj.setValue(instance);
			ValueExpression el = Expressions.instance().createValueExpression(
					elStr);
			value = el.getValue();
		} catch (Exception e) {
			value = null;
		}
		return value;
	}

	@Override
	public String persist() {
		createdMessage();
		Events.instance().raiseEvent(
				"com.vieking.seam.afterPersisted." + getSimpleEntityName());
		return "persisted";
	}

	@Override
	public String update() {
		updatedMessage();
		Events.instance().raiseEvent(
				"com.vieking.seam.afterUpdated." + getSimpleEntityName());
		return "updated";
	}

	@Override
	public String remove() {
		deletedMessage();
		Events.instance().raiseEvent(
				"com.vieking.seam.afterRemoved." + getSimpleEntityName());
		return "removed";
	}

	public void cancel() {
		if (isManaged()) {
			entityManager.refresh(getInstance());

		}
	}

	@Observer(value = "org.jboss.seam.endConversation", create = false)
	public void clearTmpQhb() {
		if (instance == null)
			return;
		if (getInstance() instanceof BaseEntity) {
			getLog().debug("清理临时NameQuery！");
			String[] names = Contexts.getSessionContext().getNames();
			for (int i = 0; i < names.length; i++) {

				if (names[i].toLowerCase().indexOf(
						"tmpqhb_" + ((BaseEntity) getInstance()).getTmpId()) >= 0) {
					Contexts.getSessionContext().remove(names[i]);
				}
			}
		}
	}

	public void setQhbPv(String qhbName, String pn, int pl, Object value) {
		BaseQueryHelp qhb = (BaseQueryHelp) Contexts.getSessionContext().get(
				qhbName);
		if (qhb == null) {
			qhb = new BaseQueryHelp();
			Contexts.getSessionContext().set(qhbName, qhb);
		}
		qhb.plParm(pn, pl).setValue(value);
	}

	public Object getQhbPv(String qhbName, String pn, Object nullRetrun) {

		BaseQueryHelp qhb = (BaseQueryHelp) Contexts.getSessionContext().get(
				qhbName);
		if (qhb != null) {
			return qhb.pv(pn) == null ? nullRetrun : qhb.pv(pn);
		} else {
			return nullRetrun;
		}
	}

	public Object getQhbPv(String qhbName, String pn) {
		return getQhbPv(qhbName, pn, null);
	}

	public String getQhbPvStr(String qhbName, String pn) {
		Object pv = getQhbPv(qhbName, pn);
		return pv == null ? null : StringUtil.toStr(pv, null);
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public T getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	@SuppressWarnings({ "rawtypes" })
	public String instanceInfoStr() {
		ValueExpression el = Expressions.instance().createValueExpression(
				instanceInfo());
		return String.valueOf(el.getValue());
	}

	@Override
	public void create() {
		super.create();
		Expressions expressions = new Expressions();
		super.setCreatedMessage(expressions.createValueExpression("添加"
				+ instanceInfo() + "成功"));
		super.setUpdatedMessage(expressions.createValueExpression("修改"
				+ instanceInfo() + "成功"));
		super.setDeletedMessage(expressions.createValueExpression("删除"
				+ instanceInfo() + "成功"));

	}

	public abstract String instanceInfo();

	public abstract String appName();

	/**
	 * 
	 * @param codingHql
	 *            编码查询Sql
	 * @param parmMap
	 *            查询参数
	 * @param cdlike
	 *            like 字符串 like字符串 中使用 % 作为通配符 前后可以加字符串 但 % 不能出现两次
	 * @param cdlen
	 *            编码长度
	 * @return 编码
	 * @throws DaoException
	 */
	synchronized public String coding(String codingHql,
			Map<String, Object> parmMap, String cdlike, Integer cdlen)
			throws DaoException {
		String[] saLike = null;
		if (!StringUtils.isEmpty(cdlike)) {
			if (!cdlike.contains("%")) {
				cdlike = cdlike + "%";
			}
			saLike = cdlike.split("%");
			if (saLike.length > 2) {
				throw new DaoException("编码【" + cdlike + "】Like字符串错误！");
			}
		} else {
			throw new DaoException("编码Like字符串为空！");
		}
		long maxid = getMaxIDQuery(codingHql, cdlike, cdlen, parmMap);
		maxid++;
		String temp = String.valueOf(maxid);
		if (!StringUtils.isEmpty(cdlike)) {

			if (saLike.length == 1) {
				return saLike[0]
						+ "00000000000000".substring(0,
								cdlen - saLike[0].length() - temp.length())
						+ temp;
			} else if (saLike.length == 2) {
				return saLike[0]
						+ "00000000000000".substring(0,
								cdlen - saLike[0].length() - saLike[1].length()
										- temp.length()) + temp + saLike[1];
			}
		}
		throw new DaoException("编码错误！");
	}

	/***
	 * 
	 * @param query
	 *            查询字符串
	 * @param cdlike
	 *            like
	 * @param cdlen
	 *            编码长度
	 * @param parmMap
	 *            参数
	 * @return 当前最大值
	 * @throws DaoException
	 */
	public long getMaxIDQuery(String query, String cdlike, long cdlen,
			Map<String, Object> parmMap) throws DaoException {
		Query q = entityManager.createQuery(query);
		q.setParameter("cdlike", cdlike);
		q.setParameter("cdlen", cdlen);
		if (parmMap != null) {
			for (Iterator<String> iterator = parmMap.keySet().iterator(); iterator
					.hasNext();) {
				String pk = iterator.next();
				q.setParameter(pk, parmMap.get(pk));
			}
		}
		String maxCode = (String) q.getSingleResult();
		if (maxCode == null) {
			return 0;
		} else {
			try {
				String tmpNumber = "";
				if (!StringUtils.isEmpty(cdlike)) {
					String[] saLike = cdlike.split("%");
					if (saLike.length == 1) {
						tmpNumber = maxCode.substring(saLike[0].length(),
								maxCode.length());
					} else if (saLike.length == 2) {
						tmpNumber = maxCode.substring(saLike[0].length(),
								maxCode.length() - saLike[1].length());
					} else {
						throw new Exception("编码【" + cdlike + "】Like字符串错误！");
					}

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

	protected User entityUser() {
		if (currUser == null)
			return null;
		return entityManager.find(User.class, currUser.getId());
	}

	public String validateEntityFound() {
		try {
			this.getInstance();
		} catch (EntityNotFoundException e) {
			addFacesMessage("【id={0}】 数据未找到", getObjId());
			return "invalid";
		}
		return this.isManaged() ? "valid" : "invalid";
	}

	public String getObjId() {
		return (String) getId();
	}

	public void setObjId(String objId) {
		if (StringUtils.isEmpty(objId)) {
			setId(null);
		} else {
			setId(objId.trim());
		}
	}

	public String getLastStateChange() {
		return lastStateChange;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getFromCid() {
		return fromCid;
	}

	public void setFromCid(String fromCid) {
		this.fromCid = fromCid;
	}

	public Boolean getAutoNew() {
		return autoNew;
	}

	public void setAutoNew(Boolean autoNew) {
		this.autoNew = autoNew;
	}

	public String getFromUrl() {
		return fromUrl;
	}

	public void setFromUrl(String fromUrl) {
		this.fromUrl = fromUrl;
	}

	public Application getApp() {
		return app;
	}

	public void setApp(Application app) {
		this.app = app;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public List<Dictionary> dictions(String code) {
		return dictionaryDao.dictions(code);
	}

	public List<Dictionary> dictions(String code, String superior) {
		return dictionaryDao.dictions(code, superior);
	}

	public List<Dictionary> dictions(String code, List<String> strs) {
		return dictionaryDao.dictions(code, strs);
	}

	public List<Dictionary> dictionsNo(String code, List<String> strs) {
		return dictionaryDao.dictionsNo(code, strs);
	}
}
