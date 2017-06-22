package com.vieking.sys.doc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;

import com.vieking.file.model.DocInfo;
import com.vieking.sys.service.FileManager;
import com.vieking.sys.util.FileUtils;

@Name("fileDownLaod")
@Scope(ScopeType.EVENT)
@AutoCreate
public class FileDownLaod implements Serializable {

	private static final long serialVersionUID = 966603431659085042L;

	@In("fileManager")
	private FileManager fileManager;

	@In
	protected EntityManager entityManager;

	@RequestParameter
	private String fileId;

	@Logger
	protected Log log;

	private String fileExt;
	private String fileName;
	private String contentType;
	private String fileType;
	private byte[] data;

	public byte[] loadFile(String fn) {
		File imageResource = fileManager.getFileByPath(fn);
		if ((imageResource != null) && (imageResource.exists())) {

			try {
//				ByteArrayOutputStream out = null;
//				FileInputStream in = new FileInputStream(imageResource);
//				fileType = FileType.getFileType(in);
//				in.close();
//				out = FileUtils.getFileOutputStream(imageResource);
				return  FileUtils.getFiletoByteBuffer(imageResource);
			//	return out.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}
		return null;
	}

	public String get内容类型() {
		if ("doc".equalsIgnoreCase(fileType)) {

			if ("doc".equalsIgnoreCase(fileExt)) {
				return "application/msword";
			}
			if ("xls".equalsIgnoreCase(fileExt)) {
				return "application/vnd.ms-excel";
			}
			if ("ppt".equalsIgnoreCase(fileExt)) {
				return "application/vnd.ms-powerpoint";
			}
		}
		if ("docx".equalsIgnoreCase(fileType)) {
			if ("doc".equalsIgnoreCase(fileType)) {
				this.contentType = "application/msword";
			}
		}
		if ("pdf".equalsIgnoreCase(fileType)) {
			return "application/pdf";
		}
		if ("dwg".equalsIgnoreCase(fileType)) {
			return "image/vnd.dwg";
		}
		return "application/octet-stream";
	}

	@Create
	public void create() {
		DocInfo docInfo = entityManager.find(DocInfo.class, fileId);
		if (docInfo == null) {
			this.fileName = "error.txt";
			try {
				this.data = "文件不存在!!!".getBytes("GB2312");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			this.contentType = "text/plain";
		} else {
			String fn = docInfo.getUrlName() + "/" + docInfo.getOriginalName();
			fileExt = docInfo.getFileExt();
			data = loadFile(fn);
			if (data == null) {
				this.fileName = "error.txt";
				try {
					this.data = "文件未找到或读取错误!!!".getBytes("GB2312");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				this.contentType = "text/plain";
			} else {

				try {
					this.fileName = java.net.URLEncoder.encode(
							docInfo.getOriginalName(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					this.fileName = docInfo.getId() + "."
							+ docInfo.getFileExt();
					e.printStackTrace();
				}
				this.contentType = get内容类型();

				log.debug(fileName);
			}
		}
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public byte[] getData() {
		return data;
	}

}
