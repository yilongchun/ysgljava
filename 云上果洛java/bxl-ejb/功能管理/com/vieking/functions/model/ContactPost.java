package com.vieking.functions.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.basicdata.model.Department;
import com.vieking.sys.model.BaseEntity;

/**
 * ContactPost <br>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "ContactPost")
@BatchSize(size = 50)
public class ContactPost extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7293584952984392553L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 联系人 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contact", nullable = true)
	@BatchSize(size = 50)
	private Contact contact;

	/** 联系人职务 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lxrzw", nullable = true)
	@BatchSize(size = 50)
	private DepartmentPost lxrzw;

	/** 联系人 部门 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lxrbm", nullable = true)
	@BatchSize(size = 50)
	private Department lxrbm;

	@Override
	public String toString() {
		return "ContactPost [contact=" + contact + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public DepartmentPost getLxrzw() {
		return lxrzw;
	}

	public void setLxrzw(DepartmentPost lxrzw) {
		this.lxrzw = lxrzw;
	}

	public Department getLxrbm() {
		return lxrbm;
	}

	public void setLxrbm(Department lxrbm) {
		this.lxrbm = lxrbm;
	}

}
