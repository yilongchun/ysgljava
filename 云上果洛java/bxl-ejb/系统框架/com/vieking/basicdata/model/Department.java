package com.vieking.basicdata.model;

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

import com.vieking.basicdata.enumerable.CantonLevel;
import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.util.GB2Alpha;

/**
 * 部门组织结构<br>
 * 
 * <p>
 * <a href="Department.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Department")
@BatchSize(size = 50)
public class Department extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1527961135502289033L;

	/** 电话区号 */
	@Column(length = 50, nullable = true)
	private String areaCode;

	/** 拼音 */
	@Column(length = 50, nullable = true)
	private String spell;

	/** 编码 作为ID使用 */
	@Id
	@GeneratedValue(generator = "assignedGenerator")
	@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
	@Column(length = 40)
	private String code;

	/** 本级代码 current Level Code */
	@Column(length = 8, nullable = false)
	private String clc;

	/** 完整的名称 wholeName */
	@Column(length = 250, nullable = false)
	private String wn;

	/** 名称 */
	@Column(length = 50, nullable = false)
	private String name;

	/** 上级 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scId", nullable = true)
	@BatchSize(size = 50)
	private Department superior;

	/** 简称 ShortName */
	@Column(length = 50, nullable = true)
	private String sn;

	/** 末级 */
	@Column(nullable = false)
	private boolean isLeaf = true;

	public String getCode() {
		if (getVersion() == null) {
			if (superior != null) {
				this.code = superior.getCode()
						+ (getClc() == null ? "" : getClc());
			} else {
				this.code = getClc();
			}
		}
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getClc() {
		return clc;
	}

	public void setClc(String clc) {
		this.clc = clc;
	}

	public String getWn() {
		if (getVersion() == null) {
			if (superior != null) {
				this.wn = superior.getWn() + '_'
						+ (getName() == null ? "" : getName());
			} else {
				this.wn = getName();
			}
			if (wn != null) {
				setZjf(GB2Alpha.getAlpha(wn.replaceAll("_", "")));
			} else {
				setZjf(null);
			}
		}
		return wn;
	}

	public void setWn(String wn) {
		this.wn = wn;
		if (wn != null) {
			setZjf(GB2Alpha.getAlpha(wn.replaceAll("_", "")));
		} else {
			setZjf(null);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Department getSuperior() {
		return superior;
	}

	public void setSuperior(Department superior) {
		this.superior = superior;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	/**
	 * 构造方法
	 * 
	 * @param 代码
	 * @param 名称
	 * @param 上级区划
	 * @param 级别
	 */
	public Department(String 本级代码, String 名称, Department 上级) {
		super();
		this.clc = 本级代码;
		this.name = 名称;
		this.superior = 上级;
		if (superior != null) {
			this.code = superior.getCode() + 本级代码;
			superior.isLeaf = false;
		} else {
			this.code = 本级代码;
		}
		if (superior != null) {
			this.wn = superior.getWn() + '_' + 名称;
		} else {
			this.wn = 名称;
		}
		setZjf(GB2Alpha.getAlpha(wn));
	}

	public String getAreaCode() {
		return areaCode;
	}

	public String getSpell() {
		return spell;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}

	@Override
	public String toString() {
		return "Canton [code=" + getCode() + ", wn=" + getWn() + "]";
	}

	/**
	 * 构造方法
	 * 
	 * @param 代码
	 * @param 名称
	 * @param 上级区划
	 * @param 级别
	 */
	public Department(String 行政区划代码, String 行政区划名称, Department 上级行政区划,
			CantonLevel 行政区划级别) {
		this(行政区划代码, 行政区划名称, 上级行政区划);

	}

	public Department() {

	}

	@Override
	public String getId() {

		return getCode();
	}

	@Override
	public void setId(String id) {
		setCode(id);

	}
}
