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
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.file.enumerable.DocLinkCount;
import com.vieking.role.model.Application;
import com.vieking.sys.model.BaseEntity;

/**
 * <p>
 * 应用文档类型 <a href="AppDocType.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Sys_AppDocType", uniqueConstraints = @UniqueConstraint(columnNames = {
		"appId", "dtId" }))
@BatchSize(size = 50)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@XmlRootElement(name = "AppDocType")
public class AppDocType extends BaseEntity {

	private static final long serialVersionUID = -1063897460308637954L;

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

	/** 应用 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appId", nullable = false)
	@BatchSize(size = 50)
	private Application app;

	/**
	 * 允许关联文档数量
	 */
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private DocLinkCount linkCount = DocLinkCount.不限;

	@XmlTransient
	public Application getApp() {
		return app;
	}

	@XmlElement(name = "DocType")
	public DocType getDocType() {
		return docType;
	}

	@XmlTransient
	public String getId() {
		return id;
	}

	public void setApp(Application app) {
		this.app = app;
	}

	public void setDocType(DocType docType) {
		this.docType = docType;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "AppDocType [app=" + app + ", docType=" + docType + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((app == null) ? 0 : app.hashCode());
		result = prime * result + ((docType == null) ? 0 : docType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppDocType other = (AppDocType) obj;
		if (getApp() == null) {
			if (other.getApp() != null)
				return false;
		} else if (!getApp().equals(other.getApp()))
			return false;
		if (getDocType() == null) {
			if (other.getDocType() != null)
				return false;
		} else if (!getDocType().equals(other.getDocType()))
			return false;
		return true;
	}

	public AppDocType(DocType docType, DocLinkCount linkCount) {
		super();
		this.docType = docType;
		this.linkCount = linkCount;
	}

	@XmlElement(name = "dlc")
	public DocLinkCount getLinkCount() {
		return linkCount;
	}

	public void setLinkCount(DocLinkCount linkCount) {
		this.linkCount = linkCount;
	}

	public AppDocType() {
		super();
	}

	public AppDocType(DocType docType, Application app, DocLinkCount linkCount) {
		super();
		this.docType = docType;
		this.app = app;
		this.linkCount = linkCount;
	}

}
