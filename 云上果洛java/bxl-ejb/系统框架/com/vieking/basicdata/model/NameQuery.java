package com.vieking.basicdata.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.BatchSize;

/**
 * <p>
 * 命名查询 <a href="NameQuery.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "SysQ_NameQuery", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@BatchSize(size = 50)
@XmlRootElement(name = "nameQuery")
public class NameQuery extends QueryDefine {

	private static final long serialVersionUID = -1063897460308637954L;

	/** 查询命名 */
	@Column(length = 100, nullable = false)
	private String name;

	/** 是否使用数据库 sql查询 */
	@Column(nullable = false)
	private boolean userNativeQuery = false;

	/** 主查询 语句 */
	@Lob
	@Column(name = "findQuery")
	@Basic(fetch = FetchType.LAZY)
	private String findQuery;

	/** 默认排序 */
	@Column(name = "orderBy")
	private String orderBy;

	/** 行数查询语句 */
	@Lob
	@Column(name = "countQuery")
	@Basic(fetch = FetchType.LAZY)
	private String countQuery;

	/** 查询参数 */
	@OneToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "nameQuery")
	@OrderBy(value = "sort asc")
	@BatchSize(size = 100)
	private List<QueryFilter> qfs = new ArrayList<QueryFilter>();

	/** 一直使用的排序 */
	@Column(name = "auOrder")
	private String auOrder;

	public NameQuery() {
		super();
		setType(QueryDefineType.QDT001);
	}

	public NameQuery(String des, QueryDefineType type, String enabledEL) {
		super(des, type, enabledEL);
	}

	public NameQuery(String des, QueryDefineType type, String enabledEL,
			String name, boolean userNativeQuery, String findQuery,
			String orderBy, String countQuery, String auOrder) {
		super(des, type, enabledEL);
		this.name = name;
		this.userNativeQuery = userNativeQuery;
		this.findQuery = findQuery;
		this.orderBy = orderBy;
		this.countQuery = countQuery;
		this.auOrder = auOrder;

	}

	public NameQuery(String name, String des, String findQuery,
			String countQuery) {
		this();
		setDes(des);
		this.name = name;
		this.findQuery = findQuery;
		this.countQuery = countQuery;
	}

	public NameQuery(String des, String enabledEL, String name,
			String findQuery, String countQuery, String orderBy, String auOrder) {

		super(des, QueryDefineType.QDT001, enabledEL);
		this.name = name;
		this.findQuery = findQuery;
		this.countQuery = countQuery;
		this.orderBy = orderBy;
		this.auOrder = auOrder;
	}

	@XmlElement(name = "cq")
	public String getCountQuery() {
		return countQuery;
	}

	@XmlElement(name = "fq")
	public String getFindQuery() {
		return findQuery;
	}

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	@XmlAttribute(name = "ob")
	public String getOrderBy() {
		return orderBy;
	}

	@XmlElement(name = "qfilter")
	public List<QueryFilter> getQfs() {
		return qfs;
	}

	@XmlAttribute(name = "isUNQ")
	public boolean isUserNativeQuery() {
		return userNativeQuery;
	}

	public void setCountQuery(String countQuery) {
		this.countQuery = countQuery;
	}

	public void setFindQuery(String findQuery) {
		this.findQuery = findQuery;
	}

	public void setName(String name) {
		this.name = StringUtils.trim(StringUtils.lowerCase(name));
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public void setQfs(List<QueryFilter> qfs) {
		this.qfs = qfs;
	}

	public void setUserNativeQuery(boolean userNativeQuery) {
		this.userNativeQuery = userNativeQuery;
	}

	@XmlAttribute(name = "auOrder")
	public String getAuOrder() {
		return auOrder;
	}

	public void setAuOrder(String auOrder) {
		this.auOrder = auOrder;
	}
}
