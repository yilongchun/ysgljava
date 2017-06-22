package com.vieking.file.selector;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.base.NqtSelector;
import com.vieking.file.enumerable.DocLinkCount;
import com.vieking.file.model.AppDocType;
import com.vieking.file.model.DocInfo;
import com.vieking.sys.base.AppContext;
import com.vieking.sys.exception.Const;

@Name("sys.docInfoSelector")
@Scope(ScopeType.PAGE)
public class DocInfoSelector extends NqtSelector<DocInfo> {

	private static final long serialVersionUID = 3323511211039919092L;

	private AppDocType adt;

	@In(value = "vieking.ui.ac", required = true)
	AppContext appContext;

	/** 打开选择对话框 */
	public void linkDocInfo(AppDocType adt, String title, String tmpId,
			int pageSize, String reRender) {
		this.adt = adt;
		super.event = Const.添加已上传文件文档链接V2;
		if (DocLinkCount.单个.equals(adt.getLinkCount())) {
			selectOne(tmpId, title, pageSize, reRender);
		} else {
			selectMany(tmpId, title, pageSize, reRender);
		}
		if (popMode) {
			setOpen(true);
		}
	}

	/** 基类回调方法 返回ID */
	@Override
	protected Object getObjectId(DocInfo obj) {
		return obj.getId();

	}

	/** 基类回调方法 返回对象的名称 或描述 */
	@Override
	protected String getObjectName(DocInfo obj) {
		return obj.getDes();
	}

	/** 基类回调方法 指定用于显示的页面 */
	@Override
	protected String getViewId() {
		return "/common/selector/docInfoSelector.xhtml";
	}

	@Override
	public String defEvent() {
		return "event.sel.com.vieking.sys.model.DocInfo";
	}

	@Override
	public void processRequestParams() {
		getQhb().parm("docType", "DT003").setSv(adt.getDocType().getName());
	}

	@Override
	public String getQueryDesc() {
		return "上传文档查询";
	}

	@Override
	public String nameQueryName() {

		return "sys.docInfo";
	}

	@Override
	public String queryHelpName() {
		return "sys.docInfoSelect";

	}
}
