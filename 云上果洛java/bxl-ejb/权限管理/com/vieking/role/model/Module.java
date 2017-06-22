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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.Length;

import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.util.GB2Alpha;
import com.vieking.sys.util.HibernateUtil;

/**
 * <p>
 * 模块 <a href="Module.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Sys_Module")
@BatchSize(size = 50)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Module extends BaseEntity implements Comparable<Module> {

	private static final long serialVersionUID = -8738385875070099430L;

	/** 模块名称 作为ID使用 */
	@Id
	@GeneratedValue(generator = "assignedGenerator")
	@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
	@Column(length = 40)
	private String name;

	/** 描述 */
	@Column(length = 50)
	@Length(max = 50)
	private String des;

	/** 是否在菜单上显示 */
	@Column(nullable = false)
	private boolean showInMenu = true;

	/** 是否在桌面显示 */
	@Column(nullable = false)
	private boolean showInDesktop = true;

	/** 菜单位置 */
	@Column(nullable = false)
	private int menuPos = 10;

	/** 显示图标 */
	@Column(nullable = true, length = 50)
	@Length(max = 50)
	private String ico;

	/** 模块应用 */
	@OneToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "module")
	@BatchSize(size = 100)
	@OrderBy("sort asc")
	private List<ModuleApp> moduleApps = new ArrayList<ModuleApp>();

	public int compareTo(final Module other) {
		return new CompareToBuilder().append(menuPos, other.menuPos)
				.toComparison();
	}

	/***
	 * 向应用列表中加入新的文档类型，如果重复保留原来的文档类型
	 */
	public void addNewModuleApp(ModuleApp ma) {
		ma.setModule(this);
		if (getModuleApps().contains(ma)) {
			ma.setModule(null);
			return;
		}
		getModuleApps().add(ma);
	}

	public Module(String name, String des) {
		super();
		setName(name);
		this.des = des;
	}

	public Module() {
		super();
	}

	public String getDes() {
		return des;
	}

	public String getName() {
		return name;
	}

	public void setDes(String des) {
		this.des = des;
		setZjf(GB2Alpha.getAlpha(des));
	}

	public void setName(String name) {
		this.name = StringUtils.lowerCase(StringUtils.trim(name));
	}

	@Override
	public String toString() {
		return "Module [des=" + des + ", name=" + name + "]";
	}

	public int getMenuPos() {
		return menuPos;
	}

	public void setMenuPos(int menuPos) {
		this.menuPos = menuPos;
	}

	public Module(String name, String des, int menuPos) {
		super();
		this.name = (name == null ? null : name.toLowerCase());
		setDes(des);
		this.menuPos = menuPos;
	}

	@Override
	public String getId() {
		return name;
	}

	@Override
	public void setId(String id) {
		this.name = id;
	}

	public List<ModuleApp> getModuleApps() {
		return moduleApps;
	}

	public void setModuleApps(List<ModuleApp> moduleApps) {
		this.moduleApps = moduleApps;
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
		Module other = (Module) target;
		if (getClass() != other.getClass())
			return false;

		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public boolean isShowInMenu() {
		return showInMenu;
	}

	public void setShowInMenu(boolean showInMenu) {
		this.showInMenu = showInMenu;
	}

	public boolean isShowInDesktop() {
		return showInDesktop;
	}

	public void setShowInDesktop(boolean showInDesktop) {
		this.showInDesktop = showInDesktop;
	}

}
