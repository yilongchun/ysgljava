package com.vieking.basicdata.manager;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.basicdata.model.QueryParamDefine;

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
@Name("sys.queryParamDefineHome")
@AutoCreate
public class QueryParamDefineHome extends BaseHome<QueryParamDefine> {

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
		QueryParamDefine o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		lastStateChange = super.persist();

		return lastStateChange;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String update() {
		if (!validation()) {
			return null;
		}
		QueryParamDefine o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		lastStateChange = super.update();
		return lastStateChange;
	}

	@Override
	public String instanceInfo() {
		return "查询参数定义【 #{sys.queryParamDefineHome.getInstance().name} 】 ";
	}

	@Override
	public String appName() {

		return "查询参数定义";
	}

}
