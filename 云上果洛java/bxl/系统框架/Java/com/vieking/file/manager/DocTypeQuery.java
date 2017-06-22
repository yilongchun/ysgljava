package com.vieking.file.manager;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.file.model.DocType;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;

@Name("sys.docTypeQuery")
@Scope(ScopeType.PAGE)
public class DocTypeQuery extends BaseNqtQuery<DocType> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7143583542160447428L;

	public void processRequestParams() {

	}

	@Override
	public void executeQueryCounts() {
	}

	public String getQueryDesc() {
		return "附件类型管理";
	}

	public DocTypeQuery() {
		setOrder("o.name asc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.sys.docType";
	}

	@Override
	public String nameQueryName() {
		return "sys.docType";
	}

}
