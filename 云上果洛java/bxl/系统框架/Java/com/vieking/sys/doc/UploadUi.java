package com.vieking.sys.doc;

import java.io.Serializable;

import javax.faces.application.ViewHandler;
import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;

import com.vieking.file.model.AppDocType;
import com.vieking.file.model.DocType;
import com.vieking.role.model.User;
import com.vieking.sys.base.AppContext;
import com.vieking.sys.base.EntityResolver;
import com.vieking.sys.exception.Const;

@Name("sys.upload.ui")
@Scope(ScopeType.PAGE)
@AutoCreate
public class UploadUi implements Serializable {

	private static final long serialVersionUID = 6820580341937493381L;

	@In(value = "vieking.ui.ac", required = true)
	AppContext appContext;

	@In(value = Const.CURRUSER, required = true)
	User user;

	@In
	protected EntityManager entityManager;

	@In
	protected FacesMessages facesMessages;

	@In("entityResolver")
	protected EntityResolver er;

	private DocType docType;

	private AppDocType adt;

	private String entId;

	private String reRender;

	private String view;

	private String title;

	private int maxFilesQuantity = 10;

	private boolean initok = false;

	private void reset() {

		title = "";
		entId = null;
		reRender = "";
		initok = false;
		maxFilesQuantity = 10;
		view = "/common/docInfo/uploadDiag.xhtml";
		Events.instance().raiseEvent(Const.CLEAR_FILE_UPLOAD_EVENT);
		Contexts.getSessionContext().set(ViewHandler.CHARACTER_ENCODING_KEY,
				"UTF-8");
	}

	/***
	 * 验证数据是否具备上传条件
	 * 
	 * @param mode
	 *            1:文档上传 2：文档上传并链接文档到应用
	 * @return
	 */
	private boolean verification(int mode) {
		boolean result = true;

		if (mode == 1) {

			if (docType == null) {
				facesMessages.addFromResourceBundleOrDefault("文档类型未选择！",
						"文档类型未选择");
				result = false;
			}
		}
		if (mode == 2) {
			if (StringUtils.isEmpty(entId)) {
				facesMessages.addFromResourceBundleOrDefault("文档关联ID为空！",
						"文档关联ID为空！");
				result = false;
			}
			if (adt == null) {
				facesMessages.addFromResourceBundleOrDefault("应用文档定义为空！",
						"应用文档定义为空！");
				result = false;
			}
		}
		return result;
	}

	/***
	 * 显示文档上传对话框 一般用户
	 * 
	 * @param _docType
	 *            文档类型
	 * @param _reRender
	 */
	public void showUploadDiag(String _docTypeDes, String _reRender) {
		reset();

		if (StringUtils.isEmpty(_docTypeDes)) {
			docType = null;
		} else {
			docType = er.findEntityByUid(DocType.class, _docTypeDes);
		}
		reRender = _reRender;
		initok = verification(1);
		if (initok) {
			appContext.setPopUrl(view);
		} else {
			appContext.setPopUrl("");
		}
	}

	/***
	 * 显示文档上传对话框 一般用户
	 * 
	 * @param _docType
	 *            文档类型
	 * @param tmpId
	 * @param _reRender
	 * 
	 */
	public void showUploadDiag(String _docTypeDes, String fwId, String _reRender) {
		FileWrapper fw = (FileWrapper) Contexts.getPageContext().get(
				FileWrapper.class);
		if (fw == null) {
			fw = new FileWrapper();
			fw.setFwId(fwId);
			Contexts.getPageContext().set("sys.upload.fw", fw);
		} else {
			fw.setFwId(fwId);
		}
		reset();

		if (StringUtils.isEmpty(_docTypeDes)) {
			docType = null;
		} else {
			docType = er.findEntityByUid(DocType.class, _docTypeDes);
		}
		reRender = _reRender;
		initok = verification(1);
		if (initok) {
			appContext.setPopUrl(view);
		} else {
			appContext.setPopUrl("");
		}
	}

	/**
	 * 此方法仅供系统管理员角色使用，可选择机构进行文件上传
	 * 
	 * @param _docType
	 *            文档类型
	 * @param _organ
	 *            选择的机构
	 * @param _view
	 *            视图
	 * @param _reRendar
	 *            重绘区域
	 */
	public void showUploadDiag(String _docTypeName, String _organId,
			String _view, String _reRendar) {
		reset();
		if (StringUtils.isEmpty(_docTypeName)) {
			docType = null;
		} else {
			docType = er.findEntityByUid(DocType.class, _docTypeName);
		}

		reRender = _reRendar;
		initok = verification(1);
		if (!StringUtils.isEmpty(_view)) {
			view = _view;
		}
		if (initok) {
			appContext.setPopUrl(view);
		} else {
			appContext.setPopUrl("");
		}
	}

	/**
	 * 上传文档并建立文档链接
	 * 
	 * @param _appDocType
	 *            应用文档类型
	 * @param _entId
	 *            实体Id
	 * @param _reRender
	 *            重绘区域
	 */
	public void showUploadLinkDiag(AppDocType _appDocType, String fwId,
			String title, String _reRender) {
		FileWrapper fw = (FileWrapper) Contexts.getPageContext().get(
				FileWrapper.class);
		if (fw == null) {
			fw = new FileWrapper();
			fw.setFwId(fwId);
			Contexts.getPageContext().set("sys.upload.fw", fw);
		} else {
			fw.setFwId(fwId);
		}
		reset();
		this.entId = fwId;
		this.title = title;

		this.adt = _appDocType;
		this.docType = _appDocType.getDocType();
		reRender = _reRender;
		initok = verification(2);
		if (initok) {
			appContext.setPopUrl(view);
		} else {
			appContext.setPopUrl("");
		}
	}

	public DocType getDocType() {
		return docType;
	}

	public void setDocType(DocType docType) {
		this.docType = docType;
	}

	public String getEntId() {
		return entId;
	}

	public boolean isInitok() {
		return initok;
	}

	public String getReRender() {
		return reRender;
	}

	public int getMaxFilesQuantity() {
		return maxFilesQuantity;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public AppDocType getAdt() {
		return adt;
	}

	public void setAdt(AppDocType adt) {
		this.adt = adt;
	}

}
