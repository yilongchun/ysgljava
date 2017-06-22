package com.vieking.file.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.file.enumerable.UrlType;
import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.util.FileUtils;

/**
 * <p>
 * 文档信息 <a href="DocInfo.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "DocInfo")
@BatchSize(size = 50)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class DocInfo extends BaseEntity {

	private static final long serialVersionUID = -1447327842684922325L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 文档类型 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dtId", nullable = false)
	@BatchSize(size = 50)
	private DocType docType;

	/** 文档说明 */
	@Column(nullable = true, length = 200)
	private String des;

	@Column(name = "docSize", nullable = true)
	private double size;

	/**
	 * 文档URL 文档保存在服务器上的随机路径 或url 文件在服务器上保存文件为
	 * uploadRootPath+urlName+xx+originalName 其中xx表示缩率图比例
	 */
	@Column(nullable = false, length = 400)
	private String urlName;

	/** 文档URL类型 */
	@Enumerated(EnumType.STRING)
	private UrlType urlType;

	/** 原始文件名 */
	@Column(nullable = true, length = 200)
	private String originalName;

	/** 文件后缀 */
	@Column(nullable = true, length = 50)
	private String fileExt;

	/** 文件相对路径 非持久化 */
	@Transient
	private String fileDir;

	/** 播放时长 秒 */
	@Column
	private long duration = 0;

	/** 已处理，jpg 文档表示已生成缩率图 */
	@Column(name = "processed", nullable = false)
	private boolean processed = false;

	/** 已处理80缩率图 */
	@Column(name = "hs80", nullable = true)
	private Boolean hs80 = false;

	/** 已处理200缩率图 */
	@Column(name = "hs200", nullable = true)
	private Boolean hs200 = false;

	/** 已处理600缩率图 */
	@Column(name = "hs600", nullable = true)
	private Boolean hs600 = false;

	/** 文件随机文件名 非持久化 */
	@Transient
	private String randomFilename;

	public boolean isJpgImage() {
		if (!StringUtils.isEmpty(originalName)) {
			fileExt = FileUtils.getTypePart(originalName);
			if (fileExt.toLowerCase().matches("jpg")) {
				return true;
			}
		}
		return false;
	}

	public boolean isDOC() {
		if (!StringUtils.isEmpty(originalName)) {
			fileExt = FileUtils.getTypePart(originalName);
			if (fileExt.toLowerCase().matches("doc")) {
				return true;
			}
		}
		return false;
	}

	public String getDes() {
		return des;
	}

	public DocType getDocType() {
		return docType;
	}

	public String getFileDir() {
		return fileDir;
	}

	public String getFileExt() {
		return fileExt;
	}

	public String getId() {
		return id;
	}

	public String getOriginalName() {
		return originalName;
	}

	public String getRandomFilename() {
		return randomFilename;
	}

	public String getUrlName() {
		return urlName;
	}

	public UrlType getUrlType() {
		return urlType;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public void setDocType(DocType docType) {
		this.docType = docType;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
		this.fileExt = FileUtils.getTypePart(originalName).toLowerCase();
	}

	public void setRandomFilename(String randomFilename) {
		this.randomFilename = randomFilename;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public void setUrlType(UrlType urlType) {
		this.urlType = urlType;
	}

	@Override
	public String toString() {
		return "DocInfo [des=" + des + ", docType=" + docType + "]";
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public Boolean getHs80() {
		if (hs80 == null) {
			return false;
		}
		return hs80;
	}

	public void setHs80(Boolean hs80) {
		this.hs80 = hs80;
	}

	public Boolean getHs200() {
		if (hs200 == null) {
			return false;
		}
		return hs200;
	}

	public void setHs200(Boolean hs200) {
		this.hs200 = hs200;
	}

	public Boolean getHs600() {
		if (hs600 == null) {
			return false;
		}
		return hs600;
	}

	public void setHs600(Boolean hs600) {
		this.hs600 = hs600;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

}
