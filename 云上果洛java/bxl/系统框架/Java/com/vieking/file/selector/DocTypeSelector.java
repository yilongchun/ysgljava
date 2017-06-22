package com.vieking.file.selector;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.base.NqtSelector;
import com.vieking.file.model.DocType;

@Name("sys.docTypeSelector")
@Scope(ScopeType.PAGE)
public class DocTypeSelector extends NqtSelector<DocType> {

	private static final long serialVersionUID = 3323511211039919092L;

	/** 打开选择对话框 */
	public void open(String _tagTmpId, String title, int pageSize,
			String reRender) {
		selectMany(_tagTmpId, title, pageSize, reRender);
	}

	/** 基类回调方法 返回ID */
	@Override
	protected Object getObjectId(DocType obj) {
		return obj.getName();

	}

	/** 基类回调方法 返回对象的名称 或描述 */
	@Override
	protected String getObjectName(DocType obj) {
		return obj.getDes();
	}

	/** 基类回调方法 指定用于显示的页面 */
	@Override
	protected String getViewId() {
		return "/common/selector/docTypeSelector.xhtml";
	}

	@Override
	public void processRequestParams() {

	}

	@Override
	public String getQueryDesc() {
		return "文档类型选择器";
	}

	@Override
	public String nameQueryName() {
		return "sys.docType";
	}

	@Override
	public String queryHelpName() {
		return "sys.docTypeSelect";
	}

	@Override
	public String defEvent() {

		return "event.sel.com.vieking.sys.model.DocType";
	}

}
