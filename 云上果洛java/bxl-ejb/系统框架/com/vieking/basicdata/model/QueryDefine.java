package com.vieking.basicdata.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.sys.model.BaseEntity;

/**
 * <p>
 * 查询定义 <a href="QueryDefine.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "SysQ_QueryDefine")
@Inheritance(strategy = InheritanceType.JOINED)
@BatchSize(size = 50)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@XmlRootElement(name = "queryDefine")
@XmlSeeAlso(value = { QueryFilter.class, NameQuery.class })
public class QueryDefine extends BaseEntity {

	private static final long serialVersionUID = -1063897460308637954L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 描述 */
	@Column(length = 500)
	private String des;

	/** 查询定义类型 */
	private QueryDefineType type;

	/** 是否启用EL */
	@Column(length = 500)
	private String enabledEL;

	/** 查询参数 */
	@OneToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "queryDefine")
	@OrderBy(value = "sort asc")
	@BatchSize(size = 200)
	private List<QueryParamDefine> qpds = new ArrayList<QueryParamDefine>();

	public QueryDefine() {
		super();
	}

	public QueryDefine(String des, QueryDefineType type, String enabledEL) {
		super();
		this.des = des;
		this.type = type;
		this.enabledEL = enabledEL;
	}

	@XmlAttribute(name = "des")
	public String getDes() {
		return des;
	}

	@XmlElement(name = "eel")
	public String getEnabledEL() {
		return enabledEL;
	}

	@XmlTransient
	public String getId() {
		return id;
	}

	@XmlElement(name = "qpd")
	public List<QueryParamDefine> getQpds() {
		return qpds;
	}

	@XmlElement(name = "qdt")
	public QueryDefineType getType() {
		return type;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public void setEnabledEL(String enabledEL) {
		this.enabledEL = enabledEL;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setQpds(List<QueryParamDefine> qpds) {
		this.qpds = qpds;
	}

	public void setType(QueryDefineType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "QueryDefine [id=" + id + ", des=" + des + ", type=" + type
				+ ", enabledEL=" + enabledEL + "]";
	}

}
