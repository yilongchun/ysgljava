package com.vieking.role.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;

import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.util.HibernateUtil;

/**
 * 用户组应用 <br>
 * <br>
 * 
 * <p>
 * <a href="UserGroupApp.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Sys_RoleApps", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"roleId", "appId" }) })
@BatchSize(size = 50)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class RoleApp extends BaseEntity implements Comparable<RoleApp>,
		Serializable {

	private static final long serialVersionUID = -9012284106767651560L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 角色 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleId", nullable = false)
	@BatchSize(size = 50)
	@Index(name = "IDX_RoleApp", columnNames = { "Id", "roleId,appId" })
	private Role role;

	/** 应用 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appId", nullable = false)
	@BatchSize(size = 50)
	private Application app;

	/** 应用限制条件 */
	@Column(nullable = true, length = 1000)
	private String restricts;

	public int compareTo(final RoleApp other) {
		return new CompareToBuilder().append(app, other.app).toComparison();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Application getApp() {
		return app;
	}

	public void setApp(Application app) {
		this.app = app;
	}

	public String getRestricts() {
		return restricts;
	}

	public void setRestricts(String restricts) {
		this.restricts = restricts;
	}

	public RoleApp(Role role, Application app, String restricts) {
		super();
		this.role = role;
		this.app = app;
		this.restricts = restricts;
	}

	public RoleApp() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((app == null) ? 0 : app.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		RoleApp other = (RoleApp) target;
		if (getClass() != other.getClass())
			return false;

		if (getApp() == null) {
			if (other.getApp() != null)
				return false;
		} else if (!getApp().equals(other.getApp()))
			return false;
		if (getRole() == null) {
			if (other.getRole() != null)
				return false;
		} else if (!getRole().equals(other.getRole()))
			return false;
		return true;
	}

	public RoleApp(Application app, String restricts) {
		super();
		this.app = app;
		this.restricts = restricts;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "RoleApp [id=" + id + ", role=" + role + ", app=" + app
				+ ", restricts=" + restricts + "]";
	}

}
