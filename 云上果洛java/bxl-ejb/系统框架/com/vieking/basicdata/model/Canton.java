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

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.basicdata.enumerable.CantonLevel;
import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.util.GB2Alpha;

/**
 * 行政区划 <br>
 * 
 * <p>
 * <a href="Canton.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Canton")
@BatchSize(size = 50)
public class Canton extends BaseEntity {

	private static final long serialVersionUID = -3800767226437745722L;

	/** 行政机构级别 */
	@Enumerated(EnumType.STRING)
	@Column(name = "c_level")
	private CantonLevel level;

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
	private Canton superior;

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

	public Canton getSuperior() {
		return superior;
	}

	public void setSuperior(Canton superior) {
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
	public Canton(String 本级代码, String 名称, Canton 上级) {
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

	/**
	 * 获取地市州级
	 */
	public Canton getCityCanton() {
		Canton tmp = this;
		if (tmp.getLevel().equals(CantonLevel.地级市)
				|| tmp.getLevel().equals(CantonLevel.州)
				|| tmp.getLevel().equals(CantonLevel.省直管))
			return tmp;
		while (tmp.getSuperior() != null) {
			tmp = (Canton) tmp.getSuperior();
			if (tmp.getLevel().equals(CantonLevel.地级市)
					|| tmp.getLevel().equals(CantonLevel.州)
					|| tmp.getLevel().equals(CantonLevel.省直管))
				return tmp;
		}
		return null;
	}

	/** 获取区县级 */
	public Canton getCountyCanton() {
		Canton tmp = this;
		if (tmp.getLevel().equals(CantonLevel.区)
				|| tmp.getLevel().equals(CantonLevel.县)
				|| tmp.getLevel().equals(CantonLevel.旗)
				|| tmp.getLevel().equals(CantonLevel.直管县)
				|| tmp.getLevel().equals(CantonLevel.县级市)
				|| tmp.getLevel().equals(CantonLevel.农场))

			return tmp;
		while (tmp.getSuperior() != null) {
			tmp = (Canton) tmp.getSuperior();
			if (tmp.getLevel().equals(CantonLevel.区)
					|| tmp.getLevel().equals(CantonLevel.县)
					|| tmp.getLevel().equals(CantonLevel.旗)
					|| tmp.getLevel().equals(CantonLevel.直管县)
					|| tmp.getLevel().equals(CantonLevel.县级市)
					|| tmp.getLevel().equals(CantonLevel.农场))
				return tmp;
		}
		return null;
	}

	public CantonLevel getLevel() {
		return level;
	}

	/**
	 * 获取省级
	 */
	public Canton getProvinceCanton() {
		Canton tmp = this;
		if (tmp.getLevel().equals(CantonLevel.省级))
			return tmp;
		while (tmp.getSuperior() != null) {
			tmp = (Canton) tmp.getSuperior();
			if (tmp.getLevel().equals(CantonLevel.省级))
				return tmp;
		}
		return null;
	}

	public String getSpell() {
		return spell;
	}

	/**
	 * 获取乡镇
	 */
	public Canton getTownCanton() {
		Canton tmp = this;
		if (tmp.getLevel().equals(CantonLevel.街道)
				|| tmp.getLevel().equals(CantonLevel.乡)
				|| tmp.getLevel().equals(CantonLevel.镇))
			return tmp;
		while (tmp.getSuperior() != null) {
			tmp = (Canton) tmp.getSuperior();
			if (tmp.getLevel().equals(CantonLevel.街道)
					|| tmp.getLevel().equals(CantonLevel.乡)
					|| tmp.getLevel().equals(CantonLevel.镇))
				return tmp;
		}
		return null;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public void setLevel(CantonLevel level) {
		this.level = level;
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
	public Canton(String 行政区划代码, String 行政区划名称, Canton 上级行政区划,
			CantonLevel 行政区划级别) {
		this(行政区划代码, 行政区划名称, 上级行政区划);
		this.level = 行政区划级别;
	}

	public Canton() {

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
