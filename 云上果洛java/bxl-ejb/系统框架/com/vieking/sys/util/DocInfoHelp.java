package com.vieking.sys.util;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.vieking.file.dao.DocInfoDao;
import com.vieking.file.enumerable.UrlType;
import com.vieking.file.model.DocInfo;
import com.vieking.file.model.DocType;
import com.vieking.sys.service.FileManager;

@Name("sys.docInfo.help")
@Scope(ScopeType.EVENT)
@AutoCreate
public class DocInfoHelp implements Serializable {

	private static final long serialVersionUID = 4969087557225414955L;

	@In(value = "sys.dao.docInfo")
	private DocInfoDao docInfoDao;

	@In(value = "fileManager")
	private FileManager fileManager;

	@Logger
	protected Log log;

	public DocInfo newDocInfo(String fn, String des, DocType dt) {
		// 构建文档信息对象
		DocInfo di = new DocInfo();
		di.setOriginalName(fn);
		di.setDes(des);
		di.setDocType(dt);
		String newPath = generateNewPath(di);
		di.setUrlName(newPath);
		di.setUrlType(UrlType.FILE);
		return di;
	}

	public String getFileAbsolutePath(DocInfo di) {
		if (di == null) {
			return null;
		} else {
			String fn = "/" + di.getUrlName() + "/" + di.getOriginalName();
			return fileManager.getFileAbsolutePath(fn);
		}
	}

	public String getFileAbsolutePath(String diId) {
		if (org.apache.commons.lang.StringUtils.isEmpty(diId)) {
			return null;
		} else {
			DocInfo di = docInfoDao.eo(diId);
			return getFileAbsolutePath(di);
		}
	}

	public String generateNewPath(DocInfo di) {
		String newPath = di.getLmOid() + "/" + di.getDocType().getPath() + "/"
				+ FileUtils.generateRandomFilename();
		return newPath;
	}
}
