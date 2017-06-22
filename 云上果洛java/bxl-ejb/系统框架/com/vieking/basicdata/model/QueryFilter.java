package com.vieking.basicdata.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.Length;

/**
 * <p>
 * 查询过滤定义 <a href="QueryFilter.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "SysQ_QueryFilter")
@BatchSize(size = 50)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@XmlRootElement(name = "QueryFilter")
public class QueryFilter extends QueryDefine {

	private static final long serialVersionUID = -1063897460308637954L;

	/** 主查询定义 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nameQueryId", nullable = false)
	@BatchSize(size = 50)
	private NameQuery nameQuery;

	/** 过滤条件 */
	@Lob
	@Column(name = "filter")
	@Basic(fetch = FetchType.LAZY)
	private String filter;

	/** 插入标记 */
	@Column(nullable = true, length = 20)
	@Length(max = 20)
	private String insTag;

	/** 过滤条件排序 */
	@Column(nullable = false)
	private int sort;

	@XmlElement(name = "filter")
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	@XmlAttribute(name = "insTag")
	public String getInsTag() {
		return insTag;
	}

	public void setInsTag(String insTag) {
		this.insTag = insTag;
	}

	@XmlAttribute(name = "sort")
	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@XmlTransient
	public NameQuery getNameQuery() {
		return nameQuery;
	}

	public void setNameQuery(NameQuery nameQuery) {
		this.nameQuery = nameQuery;
	}

	public QueryFilter(String des, String enabledEL, NameQuery nameQuery,
			String filter, String insTag, int sort) {
		super(des, QueryDefineType.QDT002, enabledEL);
		this.nameQuery = nameQuery;
		this.filter = filter;
		this.insTag = insTag;
		this.sort = sort;
	}

	public QueryFilter(String des, String enabledEL) {
		super(des, QueryDefineType.QDT002, enabledEL);
	}

	public QueryFilter() {
		super();
		setType(QueryDefineType.QDT002);
	}

}
