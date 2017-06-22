package com.vieking.role.model;

import java.io.Serializable;
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
import org.hibernate.validator.Length;

import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.util.GB2Alpha;
import com.vieking.sys.util.HibernateUtil;

/**
 * 用户组 <br>
 * <br>
 * 
 * <p>
 * <a href="UserGroup.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Sys_UserGroups")
@BatchSize(size = 50)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class UserGroup extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 7766343616420889242L;

	/** 组名 作为ID使用 */
	@Id
	@GeneratedValue(generator = "assignedGenerator")
	@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
	@Column(length = 40)
	private String name;

	/** 说明 */
	@Column(length = 50)
	@Length(max = 50, min = 3)
	private String des;

	/** 用户桌面 */
	@Column(length = 300, nullable = false)
	@Length(min = 3, max = 300)
	private String desktop;

	/** 用户组角色 */
	@OneToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "group")
	@BatchSize(size = 100)
	private List<UserGroupRole> roles = new ArrayList<UserGroupRole>();

	@Override
	public String toString() {
		return "UserGroup [name=" + name + ", des=" + des + ", desktop="
				+ desktop + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = StringUtils.lowerCase(StringUtils.trim(name));
	}

	public String getDesktop() {
		return desktop;
	}

	public void setDesktop(String desktop) {
		this.desktop = desktop;
	}

	public UserGroup() {
		super();
	}

	public UserGroup(String name, String des, String desktop) {
		super();
		setName(name);
		setDes(des);
		this.desktop = desktop;
	}

	/***
	 * 向应用列表中加入新的应用，如果重复去掉原来的应用定义
	 */
	public void addRole(UserGroupRole gr) {
		gr.setGroup(this);
		if (getRoles().contains(gr)) {
			getRoles().remove(gr);
			getRoles().add(gr);
		}
		getRoles().add(gr);
	}

	/***
	 * 向应用列表中加入新的应用，如果重复保留原来的应用定义
	 */
	public void addNewRole(UserGroupRole gr) {
		gr.setGroup(this);
		if (getRoles().contains(gr)) {
			gr.setGroup(null);
			return;
		}
		getRoles().add(gr);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Object target = HibernateUtil.relObject(obj);
		if (getClass() != target.getClass())
			return false;
		UserGroup other = (UserGroup) target;
		if (getClass() != other.getClass())
			return false;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
		setZjf(GB2Alpha.getAlpha(des));
	}

	@Override
	public String getId() {
		return name;
	}

	@Override
	public void setId(String id) {
		this.name = id;
	}

	public List<UserGroupRole> getRoles() {
		return roles;
	}

	public void setRoles(List<UserGroupRole> roles) {
		this.roles = roles;
	}

}
