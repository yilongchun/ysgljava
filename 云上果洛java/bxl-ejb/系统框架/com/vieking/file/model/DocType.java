package com.vieking.file.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.util.GB2Alpha;
import com.vieking.sys.util.HibernateUtil;

/**
 * <p>
 * 文档类型 <a href="DocType.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Sys_DocType")
@BatchSize(size = 50)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class DocType extends BaseEntity {

	private static final long serialVersionUID = 683024392923498901L;

	/** 类型 */
	@Id
	@GeneratedValue(generator = "assignedGenerator")
	@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
	@Column(length = 40)
	private String name;

	/** 描述 */
	@Column(nullable = false, length = 200)
	private String des;

	/** 路径 */
	@Column(nullable = false, length = 200)
	private String path;

	/** 文件类型 '|'分隔 */
	@Column(nullable = false, length = 200)
	private String fileType;

	/** 上传文件的最大长度限制 kb */
	@Column(nullable = false)
	private int maxLength = 150;

	/** 显示图标 */
	@Column(nullable = true, length = 200)
	private String ico;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
		setZjf(GB2Alpha.getAlpha(des));
	}

	@XmlAttribute
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@XmlAttribute
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	@XmlAttribute
	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	public String toString() {
		return "DocType [des=" + des + ", docType=" + name + "]";
	}

	@XmlAttribute
	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public DocType(String name, String des, String path, String fileType,
			int maxLength, String ico) {
		super();
		this.name = name;
		this.des = des;
		this.path = path;
		this.fileType = fileType;
		this.maxLength = maxLength;
		this.ico = ico;
	}

	public DocType() {
		super();
	}

	@Override
	@XmlTransient
	public String getId() {
		return name;
	}

	@Override
	public void setId(String id) {
		this.name = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Object target = HibernateUtil.relObject(obj);
		if (getClass() != target.getClass())
			return false;
		DocType other = (DocType) target;
		if (getClass() != other.getClass())
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
