/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package com.vieking.sys.doc;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Events;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.vieking.file.dao.DocLinkDao;
import com.vieking.file.enumerable.UrlType;
import com.vieking.file.model.DocInfo;
import com.vieking.file.model.DocLink;
import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.service.FileManager;
import com.vieking.sys.util.FileUtils;

@Name("sys.docLinkUpload.manager")
@Scope(ScopeType.EVENT)
public class DocLinkUploadManager implements Serializable {

	private static final long serialVersionUID = 4969087557225414955L;

	@In(value = "sys.dao.docLink")
	private DocLinkDao docLinkDao;

	@In(value = "sys.upload.fw", required = true, scope = ScopeType.PAGE)
	@Out(value = "sys.upload.fw", scope = ScopeType.PAGE)
	private FileWrapper fileWrapper;

	@In(value = "fileManager")
	private FileManager fileManager;

	public void listener(UploadEvent event) {
		UploadItem item = event.getUploadItem();
		// 构建文档信息对象
		DocInfo docInfo = constructDocInfo(item);
		if (docInfo == null) {
			addError(item, docInfo, "文档数据构造错误！");
			item.getFile().delete();
			return;
		}
		if (docInfo.getDocType() == null) {
			addError(item, docInfo, "文档类型为空！");
			item.getFile().delete();
			return;
		}
		if (docInfo.getLmOid() == null) {
			addError(item, docInfo, "上传机构为空！");
			item.getFile().delete();
			return;
		}
		if (docInfo.getSize() > docInfo.getDocType().getMaxLength() * 1024) {
			addError(item, docInfo, "文档超出限制长度【"
					+ docInfo.getDocType().getMaxLength() + "】！");
			item.getFile().delete();
			return;
		}
		String newPath = generateNewPath(docInfo);
		docInfo.setUrlName(newPath);
		docInfo.setUrlType(UrlType.FILE);
		if (!docInfo.isJpgImage()) {
			docInfo.setProcessed(true);
		} else {
			docInfo.setProcessed(false);
		}
		// Save to disk
		try {
			fileManager.addDoc(docInfo.getUrlName(), docInfo.getOriginalName(),
					item.getFile().getPath(), docInfo.isJpgImage());
		} catch (DaoException e) {
			addError(item, docInfo, "文档文件保存失败！");
			item.getFile().delete();
			return;
		}
		try {
			// 保存文档信息 ,处理文档路径
			DocLink docLink = constructDocLink(docInfo);
			if (docLink == null) {
				addError(item, docInfo, "文档链接数据构造失败！");
				item.getFile().delete();
			} else {
				docLinkDao.add(docLink);
			}
		} catch (DaoException e) {
			addError(item, docInfo, "文档链接数据信息保存失败！");
			item.getFile().delete();
			return;
		}
		// Prepare to show in UI
		fileWrapper.setComplete(false);
		fileWrapper.getDocs().add(docInfo);
		item.getFile().delete();
	}

	public void uploadComplete() {
		fileWrapper.setComplete(true);
		Events.instance().raiseEvent(Const.文档链接发生变化);
	}

	private String generateNewPath(DocInfo di) {
		String newPath = di.getLmOid() + "/" + di.getDocType().getPath() + "/"
				+ FileUtils.generateRandomFilename();
		return newPath;
	}

	private void addError(UploadItem item, DocInfo di, String error) {
		fileWrapper.onFileUploadError(di, error);
		item.getFile().delete();
	}

	@SuppressWarnings("unused")
	private void addError(DocInfo di, String error) {
		fileWrapper.onFileUploadError(di, error);
	}

	private DocLink constructDocLink(DocInfo docInfo) {
		DocLink docLink = new DocLink();
		docLink.setDocument(docInfo);
		if (Contexts.getPageContext().isSet("sys.docLink.ui")) {
			DocLinkUi docLinkUi = (DocLinkUi) Contexts.getPageContext().get(
					"sys.docLink.ui");
			docLink.setEbcn(docLinkUi.getAppDocType().getApp().getEbcn());
			docLink.setKeyProp(docLinkUi.getAppDocType().getApp().getKeyProp());
			docLink.setKeyValue(docLinkUi.getEntId());
		} else if (Contexts.getPageContext().isSet("sys.docLink.uiForAppName")) {
			DocLinkUiAppName docLinkUi = (DocLinkUiAppName) Contexts
					.getPageContext().get("sys.docLink.uiForAppName");
			docLink.setEbcn(docLinkUi.getAppDocType().getApp().getEbcn());
			docLink.setKeyProp(docLinkUi.getAppDocType().getApp().getKeyProp());
			docLink.setKeyValue(docLinkUi.getEntId());
		} else {
			docLink = null;
		}

		return docLink;
	}

	private DocInfo constructDocInfo(UploadItem item) {
		DocInfo di = new DocInfo();

		di.setOriginalName(item.getFileName());
		di.setSize(item.getFileSize());
		di.setDes(item.getFileName());
		if (Contexts.getPageContext().isSet("sys.docLink.ui")) {
			DocLinkUi docLinkUi = (DocLinkUi) Contexts.getPageContext().get(
					"sys.docLink.ui");
			di.setDocType(docLinkUi.getAppDocType().getDocType());

		} else if (Contexts.getPageContext().isSet("sys.docLink.uiForAppName")) {
			DocLinkUiAppName docLinkUi = (DocLinkUiAppName) Contexts
					.getPageContext().get("sys.docLink.uiForAppName");
			di.setDocType(docLinkUi.getAppDocType().getDocType());

		} else {
			di = null;
		}
		return di;
	}
}
