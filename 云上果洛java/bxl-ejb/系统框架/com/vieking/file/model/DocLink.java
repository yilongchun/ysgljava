package com.vieking.file.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.sys.model.BaseEntity;

/**
 * <p>
 * 文档关联 <a href="DocLink.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "DocLink")
@BatchSize(size = 50)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class DocLink extends BaseEntity {

	private static final long serialVersionUID = -6231460370839342907L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 文档 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "docId", nullable = false)
	@BatchSize(size = 50)
	private DocInfo document;

	/** 实体Bean类名 Entity Bean Class Name */
	@Column(nullable = false, length = 200)
	private String ebcn;

	/** 实体Bean关键域 */
	@Column(nullable = false, length = 50)
	private String keyProp;

	/** 实体Bean关键域值 */
	@Column(nullable = false, length = 50)
	private String keyValue;

	public DocInfo getDocument() {
		return document;
	}

	public String getEbcn() {
		return ebcn;
	}

	public String getId() {
		return id;
	}

	public String getKeyProp() {
		return keyProp;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setDocument(DocInfo document) {
		this.document = document;
	}

	public void setEbcn(String ebcn) {
		this.ebcn = ebcn;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setKeyProp(String keyProp) {
		this.keyProp = keyProp;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	@Override
	public String toString() {
		return "DocLink [document=" + document + ", ebcn=" + ebcn
				+ ", keyValue=" + keyValue + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
		DocLink other = (DocLink) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	public DocLink(DocInfo document, String ebcn, String keyProp,
			String keyValue) {
		super();
		this.document = document;
		this.ebcn = ebcn;
		this.keyProp = keyProp;
		this.keyValue = keyValue;
	}

	public DocLink() {
		super();
	}

}
