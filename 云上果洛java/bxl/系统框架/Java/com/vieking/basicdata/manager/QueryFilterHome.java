package com.vieking.basicdata.manager;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;

import com.vieking.basicdata.model.NameQuery;
import com.vieking.basicdata.model.QueryFilter;
import com.vieking.sys.exception.Const;

/**
 * 查询过滤 <br>
 * <br>
 * 
 * <p>
 * <a href="QueryFilterHome.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("sys.queryFilterHome")
@AutoCreate
public class QueryFilterHome extends QueryDefineHome<QueryFilter> {

	private static final long serialVersionUID = 7941561227611883684L;

	private String nqId;

	@In(value = "sys.nameQueryHome", required = false, create = false)
	private NameQueryHome nqHome;

	public boolean isWired() {
		return (instance != null) && (instance.getNameQuery() != null);
	}

	/***
	 * 页面初始化方法
	 */
	public void wire() {
		log.debug("wire:Id{0}", getId());
		if (isIdDefined()) {
			getInstance();
		} else {
			clearInstance();
			getInstance();
			if (nqHome != null) {
				getInstance().setNameQuery(nqHome.getInstance());
			} else {
				if (StringUtils.isNotEmpty(nqId)) {
					getInstance().setNameQuery(
							entityManager.find(NameQuery.class, nqId));

				}
			}
		}
	}

	public boolean validation() {
		boolean result = true;

		return result;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String persist() {
		if (!validation()) {
			return null;
		}
		QueryFilter o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		if (autoNew) {
			clearInstance();
			wire();
		}
		lastStateChange = super.persist();
		Events.instance().raiseEvent("queryFilter_persisted");
		Events.instance().raiseEvent(Const.NAMEQUERYCHANGED,
				getInstance().getNameQuery().getName());
		return lastStateChange;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String update() {
		if (!validation()) {
			return null;
		}
		QueryFilter o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		lastStateChange = super.update();
		Events.instance().raiseEvent("queryFilter_updated");
		Events.instance().raiseEvent(Const.NAMEQUERYCHANGED,
				getInstance().getNameQuery().getName());
		return lastStateChange;
	}

	@Override
	@Transactional
	public String remove() {
		if (instance.readOnly()) {
			facesMessages.add("数据处于锁定状态，不能删除！");
			return null;
		}
		try {
			getEntityManager().remove(getInstance());
			getEntityManager().flush();
			lastStateChange = super.remove();
			Events.instance().raiseEvent("queryFilter_removed");
			Events.instance().raiseEvent(Const.NAMEQUERYCHANGED,
					getInstance().getNameQuery().getName());
			getInstance().setNameQuery(null);
			return lastStateChange;
		} catch (Exception e) {
			e.printStackTrace();
			facesMessages.add("删除数据失败！存在其他数据关联！");
		}

		return null;
	}

	@Override
	public String instanceInfo() {
		return "过滤条件【 #{sys.queryFilterHome.getInstance().des} 】 ";
	}

	@Override
	public String appName() {
		return "过滤条件";
	}

	@Override
	public void qpdChangedProcess() {
		Events.instance().raiseEvent(Const.NAMEQUERYCHANGED,
				getInstance().getNameQuery().getName());

	}
}
