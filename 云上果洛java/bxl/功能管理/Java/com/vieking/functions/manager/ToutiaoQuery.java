package com.vieking.functions.manager;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.role.model.Assistance;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;

@Name("fun.toutiaoQuery")
@Scope(ScopeType.PAGE)
public class ToutiaoQuery extends BaseNqtQuery<Assistance> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8259795018899602446L;
	
	private String cxlx;
	
	public String getCxlx() {
		return cxlx;
	}
	public void setCxlx(String cxlx) {
		this.cxlx = cxlx;
	}

	@In
	protected EntityManager entityManager;

	public void processRequestParams() {
		if (cxlx != null) {
			getQhb().parm("cxlx", "DT003").setSv(cxlx);
		}
	}

	@Override
	public void executeQueryCounts() {
	}

	public String getQueryDesc() {
		return "头条";
	}

	public ToutiaoQuery() {
		setOrder("o.ct desc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.fun.toutiaoQuery";
	}

	@Override
	public String nameQueryName() {
		return "fun.toutiaoQuery";
	}

}
