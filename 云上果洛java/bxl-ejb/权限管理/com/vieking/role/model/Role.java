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
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.util.GB2Alpha;
import com.vieking.sys.util.HibernateUtil;

/**
 * 安全角色 <br>
 * 
 * <p>
 * <a href="Role.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 * 
 */
@Entity
@Table(name = "Sys_Roles")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@BatchSize(size = 50)
public class Role extends BaseEntity {

	private static final long serialVersionUID = -3877393783755840668L;

	/** 角色名称 作为Id使用 */
	@Id
	@GeneratedValue(generator = "assignedGenerator")
	@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
	@Column(length = 40)
	private String name;

	/** 描述 */
	@Column(length = 200, nullable = true)
	private String des;

	/** 工作流角色 */
	@Column
	private boolean actor = false;

	/** 其他类型角色 */
	@Column
	private boolean other = false;

	/** 角色应用 */
	@OneToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "role")
	@BatchSize(size = 100)
	private List<RoleApp> roleApps = new ArrayList<RoleApp>();

	/** 角色用户组 */
	@OneToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "role")
	@BatchSize(size = 100)
	private List<UserGroupRole> ugrs = new ArrayList<UserGroupRole>();


	/** 禁用--> state = DataState.停用 */

	public void addNewRoleApp(RoleApp ra) {
		ra.setRole(this);
		if (getRoleApps().contains(ra)) {
			ra.setApp(null);
			return;
		}
		getRoleApps().add(ra);
	}

	public Role() {

	}

	/**
	 * @return 角色编码 用于与数据过滤定义关联
	 */
	public Role(String coding, String name, String des) {
		super();
		setName(name);
		setDes(des);
	}

	@Override
	public String toString() {
		return "Role [name=" + name + ", des=" + des + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = StringUtils.upperCase(StringUtils.trim(name));
	}

	public String getDes() {

		return des;
	}

	public void setDes(String des) {
		setZjf(GB2Alpha.getAlpha(des));
		this.des = des;
	}

	public boolean isActor() {
		return actor;
	}

	public void setActor(boolean actor) {
		this.actor = actor;
	}

	public boolean isOther() {
		return other;
	}

	public void setOther(boolean other) {
		this.other = other;
	}

	@Override
	public String getId() {
		return getName();
	}

	@Override
	public void setId(String id) {
		this.name = id;
	}

	public Role(String name, String des, boolean actor) {
		super();
		setName(name);
		setDes(des);
		this.actor = actor;
	}

	public List<RoleApp> getRoleApps() {
		return roleApps;
	}

	public void setRoleApps(List<RoleApp> roleApps) {
		this.roleApps = roleApps;
	}

	public Role(String name) {
		super();
		this.name = name;
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
		Role other = (Role) target;
		if (getClass() != other.getClass())
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public List<UserGroupRole> getUgrs() {
		return ugrs;
	}

	public void setUgrs(List<UserGroupRole> ugrs) {
		this.ugrs = ugrs;
	}

	

}
