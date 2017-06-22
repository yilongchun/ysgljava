package com.vieking.sys.doc;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jfree.util.Log;

import com.vieking.file.model.DocInfo;
import com.vieking.role.model.User;
import com.vieking.sys.base.AppContext;
import com.vieking.sys.base.EntityResolver;
import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.ImageDimension;

@Name("sys.image.diag")
@Scope(ScopeType.PAGE)
@AutoCreate
public class ImageDiag implements Serializable {

	private static final long serialVersionUID = -3472179424477102208L;

	@In(value = "vieking.ui.ac", required = true)
	AppContext appContext;

	@In(value = Const.CURRUSER, required = true)
	User user;

	@In
	protected FacesMessages facesMessages;

	@In("entityResolver")
	protected EntityResolver er;

	private int size = 600;

	private String reRender;

	private String view;

	private boolean initok = false;

	private DocInfo docInfo;

	@RequestParameter
	private String docInfoId;

	public void showImage(String docInfoId, String view, String reRender,
			int size) {
		DocInfo di = er.findEntityByUid(DocInfo.class, docInfoId);
		if (di == null) {
			appContext.setPopUrl("");
			facesMessages.addFromResourceBundleOrDefault( "文档信息未找到", "文档信息未找到");
			return;
		}
		showImage(di, view, reRender, size);

	}

	/***
	 * 显示文档上传对话框 一般用户
	 * 
	 * @param _docType
	 *            文档类型
	 * @param _reRender
	 */
	public void showImage(DocInfo docInfo, String view, String reRender,
			int size) {
		reset();
		this.docInfo = docInfo;
		if (!StringUtils.isEmpty(view)) {
			this.view = view;
		}
		if (!StringUtils.isEmpty(reRender)) {
			this.reRender = reRender;
		}
		if (size >= 0) {
			this.size = size;
		}
		init();
		if (initok) {
			appContext.setPopUrl(this.view);
		} else {
			appContext.setPopUrl("");
		}
		Log.debug(appContext.getPopUrl());
	}

	public void showImage(DocInfo docInfo) {
		showImage(docInfo, "", "", -1);
	}

	public void showImage(DocInfo docInfo, int size) {
		showImage(docInfo, "", "", size);

	}

	public void showImageById(String docInfoId, int size) {
		showImage(docInfoId, "", "", size);

	}

	public void showImageByIdUrl() {
		this.docInfo = er.findEntityByUid(DocInfo.class, docInfoId);
		if (docInfo == null) {
			facesMessages.addFromResourceBundleOrDefault("文档信息未找到", "文档信息未找到");
			return;
		}
	}

	private void init() {

		int _size = ImageDimension.getInstance(size).getX();
		if (size != _size) {
			facesMessages.addFromResourceBundleOrDefault("要求显示的图像尺寸错误！！",
					"要求显示的图像尺寸错误！！");
		}
		initok = true;
	}

	private void reset() {
		reRender = "";
		initok = false;
		view = "/common/image/imageDiag.xhtml";
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getReRender() {
		return reRender;
	}

	public void setReRender(String reRender) {
		this.reRender = reRender;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public DocInfo getDocInfo() {
		return docInfo;
	}

	public void setDocInfo(DocInfo docInfo) {
		this.docInfo = docInfo;
	}

	public boolean isInitok() {
		return initok;
	}

	public String getDocInfoId() {
		return docInfoId;
	}

	public void setDocInfoId(String docInfoId) {
		this.docInfoId = docInfoId;
	}

}
