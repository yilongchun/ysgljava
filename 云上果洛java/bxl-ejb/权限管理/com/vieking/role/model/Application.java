package com.vieking.role.model;

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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.Length;

import com.vieking.file.model.AppDocType;
import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.util.HibernateUtil;

/**
 * <p>
 * 应用定义 <a href="Application.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Sys_Application")
@BatchSize(size = 50)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@XmlRootElement(name = "Application")
public class Application extends BaseEntity {

	private static final long serialVersionUID = -2142649993537819680L;

	/** 应用名称 作为ID使用 */
	@Id
	@GeneratedValue(generator = "assignedGenerator")
	@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
	@Column(length = 40)
	private String name;

	/** 描述 */
	@Column(length = 50)
	@Length(max = 50)
	private String des;

	/** 路径 */
	@Column(length = 500)
	@Length(max = 500)
	private String path;

	/** 菜单位置 */
	@Column(nullable = false)
	private int menuPos = 10;

	/** Saem管理组件 seam manager Bean Class Name */
	@Length(min = 3, max = 100)
	@Column(nullable = true, length = 200)
	private String smbcn;

	/** 实体Bean类名 Entity Bean Class Name */
	@Column(nullable = false, length = 200)
	private String ebcn;

	/** 实体Bean关键域 */
	@Column(nullable = false, length = 50)
	private String keyProp;

	/** 使用文档管理 */
	@Column(nullable = false, length = 50)
	private boolean docEnabled = true;

	/** 默认排序 */
	@Column(nullable = true, length = 500)
	private String orderby;

	/** 限制条件 */
	@Column(nullable = true, length = 1000)
	private String restrictions;

	/** 显示图标 */
	@Column(nullable = true, length = 50)
	@Length(max = 50)
	private String ico;

	/** 显示图标b */
	@Column(nullable = true, length = 50)
	@Length(max = 50)
	private String icob;

	@Transient
	private String currView;

	/** 应用文档 */
	@OneToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "app")
	@BatchSize(size = 100)
	private List<AppDocType> docTypes = new ArrayList<AppDocType>();

	/** 模块应用 */
	@OneToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "app")
	@BatchSize(size = 100)
	@OrderBy("module asc")
	private List<ModuleApp> moduleApps = new ArrayList<ModuleApp>();

	/** 角色应用 */
	@OneToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "app")
	@BatchSize(size = 100)
	@OrderBy("role asc")
	private List<RoleApp> roleApps = new ArrayList<RoleApp>();

	public Application(String 名称Id, Module 模块, String 说明, String 路径,
			String 管理组件, int 菜单位置, String 实体类名, String 主键属性, boolean 需要文档管理) {
		super();
		this.name = (名称Id == null ? null : 名称Id.toLowerCase());
		setDes(说明);
		this.smbcn = 管理组件;
		this.path = 路径;
		this.menuPos = 菜单位置;
		this.ebcn = 实体类名;
		this.keyProp = 主键属性;
		this.docEnabled = 需要文档管理;
	}

	public Application(String 名称Id, Module 模块, String 说明, String 路径,
			String 管理组件, int 菜单位置, String 实体类名, String 主键属性, boolean 需要文档管理,
			String 限制条件) {
		super();
		this.name = (名称Id == null ? null : 名称Id.toLowerCase().trim());
		setDes(说明);
		this.smbcn = 管理组件;
		this.path = 路径;
		this.menuPos = 菜单位置;
		this.ebcn = 实体类名;
		this.keyProp = 主键属性;
		this.docEnabled = 需要文档管理;
		this.restrictions = 限制条件;
	}

	public Application() {
		super();
	}

	@XmlTransient
	public String getCurrView() {
		return currView;
	}

	public void setCurrView(String currView) {
		this.currView = currView;
	}

	@Override
	public String toString() {
		return "Application [name=" + name + ", des=" + des + "]";
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = StringUtils.lowerCase(StringUtils.trim(name));
	}

	@XmlAttribute
	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	@XmlAttribute
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@XmlAttribute
	public int getMenuPos() {
		return menuPos;
	}

	public void setMenuPos(int menuPos) {
		this.menuPos = menuPos;
	}

	@XmlAttribute
	public String getEbcn() {
		return ebcn;
	}

	public void setEbcn(String ebcn) {
		this.ebcn = ebcn;
	}

	@XmlAttribute
	public String getKeyProp() {
		return keyProp;
	}

	public void setKeyProp(String keyProp) {
		this.keyProp = keyProp;
	}

	@XmlAttribute
	public boolean isDocEnabled() {
		return docEnabled;
	}

	public void setDocEnabled(boolean docEnabled) {
		this.docEnabled = docEnabled;
	}

	@XmlAttribute
	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	@XmlAttribute
	public String getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(String restrictions) {
		this.restrictions = restrictions;
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
		Application other = (Application) target;
		if (getClass() != other.getClass())
			return false;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

	@XmlAttribute
	public String getSmbcn() {
		return smbcn;
	}

	public void setSmbcn(String smbcn) {
		this.smbcn = smbcn;
	}

	@XmlElement(name = "AppDocType")
	public List<AppDocType> getDocTypes() {
		return docTypes;
	}

	public void setDocTypes(List<AppDocType> docTypes) {
		this.docTypes = docTypes;
	}

	/***
	 * 向应用列表中加入新的文档类型，如果重复保留原来的文档类型
	 */
	public void addNewDocType(AppDocType adt) {
		adt.setApp(this);
		if (getDocTypes().contains(adt)) {
			adt.setApp(null);
			return;
		}
		getDocTypes().add(adt);
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

	public Application(String name, String des, String path, String smbcn,
			String ebcn, String keyProp, boolean docEnabled, String orderby,
			int menuPos, String restrictions, String currView) {
		super();
		this.name = name;
		this.des = des;
		this.path = path;
		this.smbcn = smbcn;
		this.ebcn = ebcn;
		this.keyProp = keyProp;
		this.docEnabled = docEnabled;
		this.orderby = orderby;
		this.menuPos = menuPos;
		this.restrictions = restrictions;
		this.currView = currView;
	}

	public void setPropertis(String name, String des, String path,
			String smbcn, String ebcn, String keyProp, boolean docEnabled,
			String orderby, int menuPos, String restrictions, String currView) {
		this.name = name;
		this.des = des;
		this.path = path;
		this.smbcn = smbcn;
		this.ebcn = ebcn;
		this.keyProp = keyProp;
		this.docEnabled = docEnabled;
		this.orderby = orderby;
		this.menuPos = menuPos;
		this.restrictions = restrictions;
		this.currView = currView;
	}

	@XmlAttribute
	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	@XmlAttribute
	public String getIcob() {
		return icob;
	}

	public void setIcob(String icob) {
		this.icob = icob;
	}

	@XmlTransient
	public List<ModuleApp> getModuleApps() {
		return moduleApps;
	}

	public void setModuleApps(List<ModuleApp> moduleApps) {
		this.moduleApps = moduleApps;
	}

	@XmlTransient
	public List<RoleApp> getRoleApps() {
		return roleApps;
	}

	public void setRoleApps(List<RoleApp> roleApps) {
		this.roleApps = roleApps;
	}
}
