package com.vieking.sys.util;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.vieking.file.dao.DocInfoDao;
import com.vieking.file.dao.DocLinkDao;
import com.vieking.file.enumerable.UrlType;
import com.vieking.file.model.DocInfo;
import com.vieking.file.model.DocLink;
import com.vieking.file.model.DocType;
import com.vieking.sys.enumerable.DataState;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.exception.KeException;
import com.vieking.sys.exception.RunException;
import com.vieking.sys.service.FileManager;

@Name("sys.docLinkUpload.help")
@Scope(ScopeType.EVENT)
@AutoCreate
public class DocLinkHelp implements Serializable {

	private static final long serialVersionUID = 4969087557225414955L;

	@In(value = "sys.dao.docLink")
	private DocLinkDao docLinkDao;

	@In(value = "sys.dao.docInfo")
	private DocInfoDao docInfoDao;

	@In(value = "fileManager")
	private FileManager fileManager;

	@In(value = "app.config")
	private Config config;

	@Logger
	protected Log log;

	/***
	 * 获取系统配置信息
	 * 
	 * @param key
	 *            配置文件中的key
	 * @return 配置文件中的值
	 */
	public String config(String key) {
		try {
			return config.get(key);
		} catch (KeException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param urlObj
	 *            url对象
	 * @return 处理文档路径获取 urlObj中的文件信息
	 * 
	 *         1.如果返回为字符直接加入
	 * 
	 *         2.如果返回为DocInfo，获取绝对路径 加入
	 * 
	 *         3.如果返回为 List<DocInfo> 转换为绝对路径后 按顺序加入
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public List<String> toFileUrls(Object urlObj) {
		List<String> urls = new ArrayList<String>();
		if (urlObj != null) {
			if (urlObj instanceof String) {
				urls.add((String) urlObj);
			}
			if (urlObj instanceof DocInfo) {
				urls.add(getFileAbsolutePath((DocInfo) urlObj));
			}
			if (urlObj instanceof DocLink) {
				urls.add(getFileAbsolutePath((DocLink) urlObj));
			}
			if (urlObj instanceof List) {
				for (Iterator iterator = ((List) urlObj).iterator(); iterator
						.hasNext();) {
					Object _obj = iterator.next();
					if (_obj instanceof String) {
						urls.add((String) _obj);
					}
					if (_obj instanceof DocInfo) {
						urls.add(getFileAbsolutePath((DocInfo) _obj));
					}
					if (_obj instanceof DocLink) {
						urls.add(getFileAbsolutePath((DocLink) _obj));
					}
				}
			}
		}
		return urls;
	}

	/**
	 * 
	 * @param templateName
	 *            模板名称可包含路径，如果包含路径开头不带'/'
	 * @param newfileName
	 *            创建的文件名称 不带路径
	 * @param dt
	 *            文档类型
	 * @param organ
	 *            创建机构
	 * @param ebcn
	 *            关联实体类名称
	 * @param keyProp
	 *            关联属性
	 * @param entId
	 *            实体ID
	 * @throws DaoException
	 */

	public void createFromTemp(String templateName, String newfileName,
			DocType dt, String ebcn, String keyProp, String entId)
			throws DaoException {
		// 构建文档信息对象
		DocInfo di = new DocInfo();
		di.setOriginalName(newfileName);
		di.setDes(newfileName);
		di.setDocType(dt);
		String newPath = generateNewPath(di);
		di.setUrlName(newPath);
		di.setUrlType(UrlType.FILE);
		// 复制文件到上传文档中
		fileManager.newFileFromTemplate(di.getUrlName(), di.getOriginalName(),
				templateName);
		// 构建文档链接
		DocLink docLink = new DocLink();
		docLink.setDocument(di);
		docLink.setEbcn(ebcn);
		docLink.setKeyProp(keyProp);
		docLink.setKeyValue(entId);
		docLink.setState(DataState.锁定);
		// 保存文档链接
		docLinkDao.add(docLink);
	}

	public DocLink newDocLink(String fn, String des, DocType dt, String ebcn,
			String keyProp, String keyValue) {
		// 构建文档信息对象
		DocInfo di = newDocInfo(fn, des, dt);
		DocLink docLink = newDocLink(di, ebcn, keyProp, keyValue);
		return docLink;
	}

	public DocLink newDocLink(DocInfo di, String ebcn, String keyProp,
			String keyValue) {
		// 构建文档信息对象
		DocLink docLink = new DocLink();
		docLink.setDocument(di);
		docLink.setEbcn(ebcn);
		docLink.setKeyProp(keyProp);
		docLink.setKeyValue(keyValue);
		return docLink;
	}

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

	public void add(DocLink docLink) throws DaoException {
		docLinkDao.add(docLink);
	}

	public String getFileAbsolutePath(DocLink dl) {
		if (dl == null) {
			return null;
		} else {
			return getFileAbsolutePath(dl.getDocument());
		}
	}

	public String getFileAbsolutePath(DocInfo di) {
		if (di == null) {
			return null;
		} else {
			String fn = "/" + di.getUrlName() + "/" + di.getOriginalName();
			return fileManager.getFileAbsolutePath(fn);
		}
	}

	public Long fileSize(DocInfo docInfo) {
		String fn = docInfo.getUrlName() + "/" + docInfo.getOriginalName();
		File file = fileManager.getFileByPath(fn);
		if ((file != null) && (file.exists())) {
			return file.length();
		} else {
			throw new RunException("文件未找到！" + docInfo.getOriginalName());
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

	public String fileContext(String diId) throws DaoException {
		try {
			DocInfo di = docInfoDao.eo(diId);
			String fn = "/" + di.getUrlName() + "/" + di.getOriginalName();
			byte[] data = fileManager.loadFile(fn);
			return new String(data, "UTF-8");

		} catch (Exception e) {
			throw DaoException.instance("文档内容读取失败 ！【{0}】", diId);
		}
	}
}
