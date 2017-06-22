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

import com.vieking.role.model.Role;
import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.util.HibernateUtil;

/**
 * 用户组角色 <br>
 * <br>
 * 
 * <p>
 * <a href="UserGroupRole.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Sys_UserGroupRoles", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"groupId", "roleId" }) })
@BatchSize(size = 50)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class UserGroupRole extends BaseEntity implements
		Comparable<UserGroupRole>, Serializable {

	private static final long serialVersionUID = -9012284106767651560L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 用户组 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groupId", nullable = false)
	@BatchSize(size = 50)
	@Index(name = "IDX_UserGroupRole", columnNames = { "groupId", "roleId" })
	private UserGroup group;

	/** 应用 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleId", nullable = false)
	@BatchSize(size = 50)
	private Role role;

	/** 应用限制条件 */
	@Column(nullable = true, length = 1000)
	@JoinColumn(name = "restricts")
	private String restricts;

	public int compareTo(final UserGroupRole other) {
		return new CompareToBuilder().append(role, other.role).toComparison();
	}

	@Override
	public String toString() {
		return "UserGroupRole [group=" + group + ", role=" + role
				+ ", restricts=" + restricts + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserGroup getGroup() {
		return group;
	}

	public void setGroup(UserGroup group) {
		this.group = group;
	}

	public String getRestricts() {
		return restricts;
	}

	public void setRestricts(String restricts) {
		this.restricts = restricts;
	}

	public UserGroupRole(UserGroup group, Role role, String restricts) {
		super();
		this.group = group;
		this.role = role;
		this.restricts = restricts;
	}

	public UserGroupRole() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
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
		UserGroupRole other = (UserGroupRole) target;
		if (getClass() != other.getClass())
			return false;
		if (getRole() == null) {
			if (other.getRole() != null)
				return false;
		} else if (!getRole().equals(other.getRole()))
			return false;
		if (getGroup() == null) {
			if (other.getGroup() != null)
				return false;
		} else if (!getGroup().equals(other.getGroup()))
			return false;
		return true;
	}

	public UserGroupRole(Role role, String restricts) {
		super();
		this.role = role;
		this.restricts = restricts;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
