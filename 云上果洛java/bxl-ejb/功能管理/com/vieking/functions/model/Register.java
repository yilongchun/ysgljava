package com.vieking.functions.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.sys.model.BaseEntity;

/**
 * Register <br>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Register")
@BatchSize(size = 50)
public class Register extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2551109617880683957L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 注册姓名 */
	@Column(length = 20)
	private String name;

	/** 注册时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "zcsj", nullable = true, unique = false)
	private Calendar zcsj;

	/** 注册电话 */
	@Column(length = 20, unique = true)
	private String phone;

	/** 注册密码 */
	@Column(length = 20, unique = true)
	private String pwd;

	/** 验证码 */
	@Column(length = 20, unique = true)
	private String yzm;

	/** 注册成功 */
	@Column()
	private int reg = 0;

	@Override
	public String toString() {
		return "Register [phone=" + phone + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getZcsj() {
		return zcsj;
	}

	public void setZcsj(Calendar zcsj) {
		this.zcsj = zcsj;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getYzm() {
		return yzm;
	}

	public void setYzm(String yzm) {
		this.yzm = yzm;
	}

	public int getReg() {
		return reg;
	}

	public void setReg(int reg) {
		this.reg = reg;
	}

}
