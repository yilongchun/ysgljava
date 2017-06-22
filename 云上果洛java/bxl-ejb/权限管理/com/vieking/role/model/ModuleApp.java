package com.vieking.role.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.Length;

import com.vieking.sys.model.BaseEntity;
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
@Table(name = "Sys_ModuleApp", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"moduleId", "appId" }) })
@BatchSize(size = 50)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class ModuleApp extends BaseEntity {

	private static final long serialVersionUID = -5277658239022913173L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 模块 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "moduleId", nullable = false)
	@BatchSize(size = 50)
	private Module module;

	/** 应用 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appId", nullable = false)
	@BatchSize(size = 50)
	private Application app;

	/** 应用别名 */
	@Column(length = 30, nullable = true)
	@Length(max = 30)
	private String appAlias;

	/** 菜单位置 */
	@Column(nullable = false)
	private int sort = 10;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Application getApp() {
		return app;
	}

	public void setApp(Application app) {
		this.app = app;
	}

	public String getAppAlias() {
		return appAlias;
	}

	public void setAppAlias(String appAlias) {
		this.appAlias = "".equals(StringUtils.trim(appAlias)) ? null
				: StringUtils.trim(appAlias);
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "ModuleApp [id=" + id + ", app=" + app + ", module=" + module
				+ ", appAlias=" + appAlias + ", sort=" + sort + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((app == null) ? 0 : app.hashCode());
		result = prime * result + ((module == null) ? 0 : module.hashCode());
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
		ModuleApp other = (ModuleApp) target;
		if (getClass() != other.getClass())
			return false;
		if (app == null) {
			if (other.app != null)
				return false;
		} else if (!app.equals(other.app))
			return false;
		if (module == null) {
			if (other.module != null)
				return false;
		} else if (!module.equals(other.module))
			return false;
		return true;
	}

	public ModuleApp(Module module, Application app, String appAlias, int sort) {
		super();
		this.module = module;
		this.app = app;
		this.appAlias = appAlias;
		this.sort = sort;
	}

	public ModuleApp() {
		super();
	}

	public ModuleApp(Application app, String appAlias, int sort) {
		super();
		this.app = app;
		this.appAlias = appAlias;
		this.sort = sort;
	}

}
