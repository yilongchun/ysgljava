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

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.File;
import java.io.Serializable;

import org.jboss.beans.metadata.api.annotations.Create;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.vieking.file.dao.DocInfoDao;
import com.vieking.file.enumerable.UrlType;
import com.vieking.file.model.DocInfo;
import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.service.FileManager;
import com.vieking.sys.util.FileUtils;

@Name("sys.upload.manager")
@Scope(ScopeType.EVENT)
@AutoCreate
public class FileUploadManager implements Serializable {

	private static final long serialVersionUID = -856052952779353184L;

	@Logger
	protected Log log;

	@In(value = "sys.dao.docInfo", create = true)
	private DocInfoDao docInfoDao;

	@In(value = "sys.upload.fw", required = true, scope = ScopeType.PAGE)
	@Out(value = "sys.upload.fw", scope = ScopeType.PAGE)
	private FileWrapper fileWrapper;

	@In(value = "sys.upload.ui")
	private UploadUi uploadUi;

	@In(value = "fileManager")
	private FileManager fileManager;

	public void listener(UploadEvent event) {
		UploadItem item = event.getUploadItem();
		// 构建文档信息对象
		DocInfo docInfo = constructDocInfo(item);
		if (docInfo.getDocType() == null) {
			addError(item, docInfo, "文档类型为空！");
			return;
		}
		// if (docInfo.getLmOid() == null) {
		// addError(item, docInfo, "上传机构为空！");
		// return;
		// }
		if (docInfo.getSize() > docInfo.getDocType().getMaxLength() * 1024) {
			addError(item, docInfo, "文档超出限制长度【"
					+ docInfo.getDocType().getMaxLength() + "】！");
			return;
		}
		try {
			// If exist generate new path for image
			String newPath = generateNewPath(docInfo);
			docInfo.setUrlName(newPath);
			docInfo.setUrlType(UrlType.FILE);
			if ("YP".equals(docInfo.getDocType().getName())
					|| "SP".equals(docInfo.getDocType().getName())) {
				Encoder encoder = new Encoder();
				try {
					MultimediaInfo m = encoder.getInfo(item.getFile());
					long ls = m.getDuration();
					docInfo.setDuration(ls / 1000);
					// System.out.println("此视频时长为:" + ls / 60000 + "分" + ls /
					// 1000
					// + "秒！");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				docInfo.setDuration(0);
			}

			if (!docInfo.isJpgImage()) {
				docInfo.setProcessed(true);
			} else {
				docInfo.setProcessed(false);
			}
			// 保存文档信息 ,处理文档路径
			
			String fileName = FileUtils.generateRandomFilename() + docInfo.getOriginalName().substring(docInfo.getOriginalName().lastIndexOf("."));
			docInfo.setOriginalName(fileName);
			
			docInfoDao.save(docInfo);
		} catch (DaoException e) {
			addError(item, docInfo, "文档数据信息保存失败！");
			return;
		}
		// Save to disk
		try {
			
			
			
			
			fileManager.addDoc(docInfo.getUrlName(), docInfo.getOriginalName(),
					item.getFile().getPath(), docInfo.isJpgImage());
		} catch (DaoException e) {
			addError(item, docInfo, "文档文件保存失败！");
			return;
		}
		// Prepare to show in UI
		fileWrapper.setComplete(false);
		fileWrapper.getDocs().add(docInfo);
		item.getFile().delete();
	}

	public void uploadComplete() {
		fileWrapper.setComplete(true);
		Events.instance().raiseEvent(Const.文档上传完毕);
		Events.instance().raiseEvent(Const.文档上传完毕V2, fileWrapper);
	}

	private String generateNewPath(DocInfo di) {
		String newPath = di.getLmOid() + "/" + di.getDocType().getPath() + "/";
//				+ FileUtils.generateRandomFilename();
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

	@Create
	public void create() {
		log.debug("sys.upload.manager  create");
	}

	private DocInfo constructDocInfo(UploadItem item) {
		DocInfo di = new DocInfo();
		di.setDocType(uploadUi.getDocType());
		di.setOriginalName(item.getFileName());
		di.setSize(item.getFileSize());
		di.setDes(item.getFileName());
		return di;
	}
}
