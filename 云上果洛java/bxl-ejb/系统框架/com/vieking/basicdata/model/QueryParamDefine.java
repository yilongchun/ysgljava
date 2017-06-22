package com.vieking.basicdata.model;

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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.Length;

import com.vieking.sys.enumerable.DataType;
import com.vieking.sys.model.BaseEntity;

/**
 * <p>
 * 查询参数定义 <a href="QueryParamDefine.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "SysQ_QueryParamDefine")
@BatchSize(size = 50)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@XmlRootElement(name = "qpd")
public class QueryParamDefine extends BaseEntity {

	private static final long serialVersionUID = -1063897460308637954L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "queryDefineId", nullable = false)
	@BatchSize(size = 50)
	private QueryDefine queryDefine;

	/** 参数名称 */
	@Column(length = 50, nullable = true)
	@Length(max = 50)
	private String name;

	/** 参数说明 */
	@Column(length = 20, nullable = true)
	@Length(max = 20)
	private String des;

	/** 参数类型 */
	@Enumerated(EnumType.STRING)
	@Column(length = 20, nullable = false)
	private DataType type = DataType.DT003;

	/** 参数值表达式 */
	@Column(length = 500)
	private String valueEL;

	/** 默认值 */
	@Column(length = 500)
	private String defineValue;

	/** 参数排序 */
	@Column
	private int sort = 10;

	public QueryParamDefine() {
		super();
	}

	public QueryParamDefine(QueryDefine queryDefine, String name, String des,
			DataType type, String valueEL, String defineValue, int sort) {
		super();
		this.queryDefine = queryDefine;
		this.name = name;
		this.des = des;
		this.type = type;
		this.valueEL = valueEL;
		this.defineValue = defineValue;
		this.sort = sort;
	}

	@XmlElement(name = "dv")
	public String getDefineValue() {
		return defineValue;
	}

	@XmlAttribute(name = "des")
	public String getDes() {
		return des;
	}

	@XmlTransient
	public String getId() {
		return id;
	}

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	@XmlTransient
	public QueryDefine getQueryDefine() {
		return queryDefine;
	}

	@XmlAttribute(name = "sort")
	public int getSort() {
		return sort;
	}

	@XmlElement(name = "dt")
	public DataType getType() {
		return type;
	}

	@XmlElement(name = "vel")
	public String getValueEL() {
		return valueEL;
	}

	public void setDefineValue(String defineValue) {
		this.defineValue = StringUtils.trim(defineValue);
	}

	public void setDes(String des) {
		this.des = des;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = StringUtils.trim(name);
	}

	public void setQueryDefine(QueryDefine queryDefine) {
		this.queryDefine = queryDefine;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public void setValueEL(String valueEL) {
		this.valueEL = StringUtils.trim(valueEL);
	}

	@Override
	public String toString() {
		return "QueryParamDefine [name=" + name + ", queryDefine="
				+ queryDefine + ", type=" + type + ", valueEL=" + valueEL + "]";
	}

}
