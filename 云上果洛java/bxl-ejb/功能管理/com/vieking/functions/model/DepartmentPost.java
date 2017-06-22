package com.vieking.functions.model;

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

import com.vieking.basicdata.model.Department;
import com.vieking.sys.model.BaseEntity;

/**
 * 职务 <br>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "DepartmentPost")
@BatchSize(size = 50)
public class DepartmentPost extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8916557613089183068L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 职务编号 */
	@Column(length = 20)
	private String code;

	/** 职务名称 */
	@Column(length = 20)
	private String name;

	/** 部门 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = " bm", nullable = true)
	@BatchSize(size = 50)
	private Department bm;

	@Override
	public String toString() {
		return "DepartmentPost [code=" + code + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Department getBm() {
		return bm;
	}

	public void setBm(Department bm) {
		this.bm = bm;
	}

}
