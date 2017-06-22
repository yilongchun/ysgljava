package com.vieking.basicdata.manager;

import java.util.List;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;

import com.vieking.basicdata.model.NameQuery;
import com.vieking.basicdata.model.QueryParamDefine;
import com.vieking.sys.exception.Const;

/**
 * 命名查询 <br>
 * <br>
 * 
 * <p>
 * <a href="NameQueryHome.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("sys.nameQueryHome")
@AutoCreate
public class NameQueryHome extends QueryDefineHome<NameQuery> {

	@In(value = "sys.queryFilterHome")
	private QueryFilterHome qfhome;

	private static final long serialVersionUID = 7941561227611883684L;

	public boolean isWired() {
		return true;
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
		}
	}

	@SuppressWarnings("unchecked")
	public boolean validation() {
		boolean result = true;
		if (getInstance().getName() == null) {
			facesMessages.add("提示", "命名不能为空！");
			return false;
		}
		List<NameQuery> ls = entityManager
				.createQuery(
						"from NameQuery o where o.name=:name and o.id <> :id")
				.setParameter("name", getInstance().getName())
				.setParameter(
						"id",
						isManaged() ? getInstance().getId()
								: "xxxxxxxxxxxxxxxx").getResultList();
		if (!ls.isEmpty()) {
			facesMessages.add("错误提示", "{0}命名查询已存在！", getInstance().getName());
			return false;
		}
		return result;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String persist() {
		if (!validation()) {
			return null;
		}
		NameQuery o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		lastStateChange = super.persist();
		Events.instance().raiseEvent(Const.NAMEQUERYCHANGED,
				getInstance().getName());
		return lastStateChange;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String update() {
		if (!validation()) {
			return null;
		}
		NameQuery o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		lastStateChange = super.update();
		// 可能存在的问题 如果修改名称后 系统可能同时装载 改名前和改名后的两个NameQuery
		Events.instance().raiseEvent(Const.NAMEQUERYCHANGED,
				getInstance().getName());
		return lastStateChange;
	}

	@Override
	public String appName() {
		return null;
	}

	@Override
	public String instanceInfo() {
		return "命名查询【 #{sys.nameQueryHome.getInstance().name} 】";
	}

	public QueryParamDefine getQpd() {
		return qpd;
	}

	public void setQpd(QueryParamDefine qpd) {
		this.qpd = qpd;
	}

	public QueryFilterHome getQfhome() {
		return qfhome;
	}

	public void setQfhome(QueryFilterHome qfhome) {
		this.qfhome = qfhome;
	}

	@Override
	public void qpdChangedProcess() {
		Events.instance().raiseEvent(Const.NAMEQUERYCHANGED,
				getInstance().getName());

	}
}
