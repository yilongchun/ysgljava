package com.vieking.functions.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.basicdata.model.Dictionary;
import com.vieking.role.model.User;
import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.util.GB2Alpha;

/**
 * Contact <br>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Contact")
@BatchSize(size = 50)
public class Contact extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8794528250055089460L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 创建用户 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = true)
	@BatchSize(size = 50)
	private User user;

	/** 联系人姓名 */
	@Column(length = 20)
	private String name;

	/** 联系人助记符 */
	@Column(length = 20)
	private String lxrzjf;

	/** 性别 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sex", nullable = true)
	@BatchSize(size = 50)
	private Dictionary sex;

	/** 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cjsj", nullable = true, unique = false)
	private Calendar cjsj;

	/** 联系人电话 */
	@Column(length = 20, unique = true)
	private String phone;

	/** 邮件 */
	@Column(length = 40)
	private String email;

	/** 简介 */
	@Column(length = 120)
	private String jianjie;
	
	/** 级别  */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level", nullable = true)
	@BatchSize(size = 50)
	private Dictionary level;
	
 

	/** 是否注册 */
	@Column()
	private int reg = 0;

	/** 联系人部门职务 */
	@OneToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "contact")
	@BatchSize(size = 100)
	private List<ContactPost> contactPosts = new ArrayList<ContactPost>();
	
	@Column(length = 120)
	private String post;
	
	/*固定电话*/
	@Column(length = 20)
	private String telephone;

	@Override
	public String toString() {
		return "TongXunLu [user=" + user + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLxrzjf() {
		return lxrzjf;
	}

	public void setLxrzjf(String lxrzjf) {
		this.lxrzjf = lxrzjf;
		if (lxrzjf != null) {
			setZjf(GB2Alpha.getAlpha(lxrzjf.replaceAll("_", "")));
		} else {
			setZjf(null);
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getCjsj() {
		return cjsj;
	}

	public void setCjsj(Calendar cjsj) {
		this.cjsj = cjsj;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getReg() {
		return reg;
	}

	public void setReg(int reg) {
		this.reg = reg;
	}

	public Dictionary getSex() {
		return sex;
	}

	public void setSex(Dictionary sex) {
		this.sex = sex;
	}

	public List<ContactPost> getContactPosts() {
		return contactPosts;
	}

	public void setContactPosts(List<ContactPost> contactPosts) {
		this.contactPosts = contactPosts;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJianjie() {
		return jianjie;
	}

	public void setJianjie(String jianjie) {
		this.jianjie = jianjie;
	}

	public Dictionary getLevel() {
		return level;
	}

	public void setLevel(Dictionary level) {
		this.level = level;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	
	
}
