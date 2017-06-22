package com.vieking.basicdata.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.sys.model.BaseEntity;

/**
 * 字典描述表 <br>
 * 
 * <p>
 * <a href="DictionaryType.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Dic_DictionaryType")
@BatchSize(size = 50)
public class DictionaryType extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4031454467953421410L;

	/** 编码 作为ID使用 */
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	private String code;

	/** 编号 */
	@Column(length = 50, nullable = false)
	private String bh;

	/** 名称 */
	@Column(length = 50, nullable = false)
	private String name;

	/** 作废 */
	@Column(nullable = false)
	private boolean invalid = false;

	/** 字典定义信息 */
	@OneToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "dictionaryType")
	@OrderBy("code")
	@BatchSize(size = 50)
	private List<Dictionary> dictionarys = new ArrayList<Dictionary>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}

	public List<Dictionary> getDictionarys() {
		return dictionarys;
	}

	public void setDictionarys(List<Dictionary> dictionarys) {
		this.dictionarys = dictionarys;
	}

	@Override
	public String toString() {
		return "HousesDictionary [code=" + getCode() + "]";
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
