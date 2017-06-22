package com.vieking.basicdata.manager;

import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.basicdata.model.QueryDefine;
import com.vieking.basicdata.model.QueryParamDefine;

/**
 * 查询定义 <br>
 * <br>
 * 
 * <p>
 * <a href="QueryDefineHome.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
public abstract class QueryDefineHome<T> extends BaseHome<T> {

	private static final long serialVersionUID = -1163302111016102584L;

	protected QueryParamDefine qpd;

	public abstract void qpdChangedProcess();

	public void newQpd() {
		qpd = new QueryParamDefine();
		qpd.setQueryDefine((QueryDefine) getInstance());
	}

	@Transactional
	public void addQpd() {
		entityManager.persist(qpd);
		entityManager.flush();
		entityManager.refresh(getInstance());
		qpdChangedProcess();
		newQpd();
	}

	@Transactional
	public void updateQpd() {
		entityManager.persist(qpd);
		entityManager.flush();
		entityManager.refresh(getInstance());
		qpdChangedProcess();
	}

	@Transactional
	public void delQpd(QueryParamDefine _qpd) {
		if (_qpd == null)
			return;
		log.debug("_qpd-->{0} {1}", _qpd.getId(), _qpd);
		if (qpd != null && qpd.getId() != null
				&& qpd.getId().equals(_qpd.getId())) {
			qpd = null;
		}
		entityManager.remove(_qpd);
		entityManager.flush();
		((QueryDefine) getInstance()).getQpds().clear();
		entityManager.refresh(getInstance());
		qpdChangedProcess();
	}

	public QueryParamDefine getQpd() {
		return qpd;
	}

	public void setQpd(QueryParamDefine qpd) {
		this.qpd = qpd;
	}

}
