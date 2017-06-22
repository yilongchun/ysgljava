package com.vieking.role.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.Length;

import com.vieking.basicdata.model.Dictionary;
import com.vieking.role.model.Role;
import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.util.GB2Alpha;

/**
 * 用户 <br>
 * <br>
 * 
 * <p>
 * <a href="User.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Users")
@BatchSize(size = 50)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class User extends BaseEntity {

	private static final long serialVersionUID = -2528506864664633968L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 用户登陆名/昵称 */
	@Column(length = 15, unique = true, nullable = false)
	@Length(max = 15)
	private String loginName;

	/** 用户角色ID 随机数 */
	@Column(length = 15, nullable = true)
	@Length(max = 15)
	private String actorId = getTmpUiId();

	/** 姓名 */
	@Column(length = 100, nullable = true)
	private String name;

	/** 性别 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sex", nullable = true)
	@BatchSize(size = 50)
	private Dictionary sex;

	/** 学校、学院、系名称 */
	@Column(length = 120, nullable = true)
	private String xxmc;

	/** 班级 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bj", nullable = true)
	@BatchSize(size = 50)
	private Dictionary bj;

	/** 生日 */
	@Temporal(TemporalType.DATE)
	@Column(name = "birthday", nullable = true, unique = false)
	private Calendar birthday;

	/** QQ */
	@Column(length = 40, nullable = true)
	private String qq;

	/** 微信号 */
	@Column(length = 40, nullable = true)
	private String wx;

	/** 微博 */
	@Column(length = 40, nullable = true)
	private String wb;

	/** 邮箱 */
	@Column(length = 40, nullable = true)
	private String yx;

	/** 用户锁时的登陆密码 */
	@Column(length = 400, nullable = true)
	@Length(max = 30)
	private String pwd;

	/** 不使用用户锁时的登陆密码 */
	@Column(length = 400, nullable = true)
	@Length(max = 400)
	private String noLockpwd;

	/** 手机号 */
	@Column(length = 15, unique = true, nullable = true)
	@Length(max = 15)
	private String phone;

	/** 注册日期 */
	@Temporal(TemporalType.DATE)
	@Column(name = "zcrq", nullable = true, unique = false)
	private Calendar zcrq;

	/** 用户组 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groupId", nullable = false)
	@BatchSize(size = 50)
	private UserGroup group;

	/** 用户角色(用于安全控制) */
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "Sys_Role_Users", joinColumns = { @JoinColumn(name = "User_ID") }, inverseJoinColumns = { @JoinColumn(name = "Role_ID") })
	@OrderBy("name")
	@BatchSize(size = 50)
	private Set<Role> roles = new HashSet<Role>();

	@Transient
	private Map<String, List<String>> uroMap;

	/** 当前用户岗位桌面 */
	@Transient
	private String desktop;

	/** 用户角色 名值对 */
	@Transient
	private Map<String, String> rolesMap = new HashMap<String, String>();

	public Map<String, String> getRolesMap() {
		return rolesMap;
	}

	public void setRolesMap(Map<String, String> rolesMap) {
		this.rolesMap = rolesMap;
	}

	@Override
	public String toString() {
		return "User [loginName=" + loginName + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = StringUtils.lowerCase(StringUtils.trim(loginName));
	}

	public String getName() {
		return name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getNoLockpwd() {
		return noLockpwd;
	}

	public void setNoLockpwd(String noLockpwd) {
		this.noLockpwd = noLockpwd;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void setName(String name) {
		this.name = name;
		setZjf(GB2Alpha.getAlpha(name));
	}

	public UserGroup getGroup() {
		return group;
	}

	public void setGroup(UserGroup group) {
		this.group = group;
	}

	public String getDesktop() {
		return desktop;
	}

	public void setDesktop(String desktop) {
		this.desktop = desktop;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Calendar getZcrq() {
		return zcrq;
	}

	public void setZcrq(Calendar zcrq) {
		this.zcrq = zcrq;
	}

	public Map<String, List<String>> getUroMap() {
		return uroMap;
	}

	public void setUroMap(Map<String, List<String>> uroMap) {
		this.uroMap = uroMap;
	}

	public Dictionary getSex() {
		return sex;
	}

	public void setSex(Dictionary sex) {
		this.sex = sex;
	}

	public Calendar getBirthday() {
		return birthday;
	}

	public void setBirthday(Calendar birthday) {
		this.birthday = birthday;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWx() {
		return wx;
	}

	public void setWx(String wx) {
		this.wx = wx;
	}

	public String getWb() {
		return wb;
	}

	public void setWb(String wb) {
		this.wb = wb;
	}

	public String getYx() {
		return yx;
	}

	public void setYx(String yx) {
		this.yx = yx;
	}

	public String getXxmc() {
		return xxmc;
	}

	public void setXxmc(String xxmc) {
		this.xxmc = xxmc;
	}

	public Dictionary getBj() {
		return bj;
	}

	public void setBj(Dictionary bj) {
		this.bj = bj;
	}

}
