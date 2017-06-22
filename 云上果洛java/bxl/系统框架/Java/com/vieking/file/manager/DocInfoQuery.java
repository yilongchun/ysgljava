package com.vieking.file.manager;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;

import com.vieking.file.model.DocInfo;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;
import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.DaoException;

@Name("sys.docInfoQuery")
@Scope(ScopeType.PAGE)
public class DocInfoQuery extends BaseNqtQuery<DocInfo> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7143583542160447428L;

	@Observer(create = false, value = Const.文档上传完毕)
	public void 文档上传完后刷新() throws DaoException {
		log.debug("文档上传完后刷新");
		applyFilter();
		prepareResults();
	}

	public void processRequestParams() {

	}

	@Override
	public void executeQueryCounts() {
	}

	public String getQueryDesc() {
		return "附件管理";
	}

	public DocInfoQuery() {
		setOrder("o.ct desc o.originalName ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.sys.docInfo";
	}

	@Override
	public String nameQueryName() {
		return "sys.docInfo";
	}

}
