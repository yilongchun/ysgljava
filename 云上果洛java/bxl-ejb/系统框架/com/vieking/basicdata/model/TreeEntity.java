package com.vieking.basicdata.model;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.util.GB2Alpha;

/**
 * <p>
 * 树形实体
 * 
 * <a href="TreeEntity.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
// @Entity
// @org.hibernate.annotations.Entity(polymorphism = PolymorphismType.EXPLICIT)
// @Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class TreeEntity extends BaseEntity {

	private static final long serialVersionUID = 4788022951287772114L;

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
	private TreeEntity superior;

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

	public TreeEntity getSuperior() {
		return superior;
	}

	public void setSuperior(TreeEntity superior) {
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
	public TreeEntity(String 本级代码, String 名称, TreeEntity 上级) {
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

	public TreeEntity() {
	}

	@Override
	public String toString() {
		return "TreeEntity [code=" + code + ", wn=" + wn + "]";
	}

}
