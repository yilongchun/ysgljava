package com.vieking.basicdata.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.vieking.sys.enumerable.ApprovalStatus;
import com.vieking.sys.enumerable.DataState;

/**
 * <p>
 * 数据关键状态 变化记录 <a href="EntityStateChageLog.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Entity
@Table(name = "Sys_EntityStateLog")
@BatchSize(size = 50)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class EntityStateChageLog implements Serializable {

	private static final long serialVersionUID = 518390758849288729L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 类名 */
	@Column(length = 100)
	private String ebcn;

	@Column(length = 100)
	private String entId;

	/** 修改人所在机构 */
	@Column(length = 100)
	private String organId;

	/** 修改用户Id */
	@Column(length = 100)
	private String userId;

	/** 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "logTime", nullable = true, unique = false)
	@XmlTransient
	private Calendar logTime;

	/** 数据状态 */
	@Enumerated(EnumType.STRING)
	@Column(name = "BE_state", nullable = false, unique = false)
	private DataState state = DataState.正常;

	/** 数据审核状态 */
	@Enumerated(EnumType.STRING)
	@Column(length = 30, name = "BE_shzt", nullable = true, unique = false)
	@XmlTransient
	private ApprovalStatus shzt;

	/** 审核申请用户 */
	@Column(name = "BE_shsqyhId", length = 50)
	private String shsqyhId;

	@Column(name = "BE_shsqyhName", length = 20)
	private String shsqyhName;

	/** 审核申请时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BE_shsqsj", nullable = true, unique = false)
	@XmlTransient
	private Calendar shsqsj;

	/** 审核申请人 */
	@Column(name = "BE_shsqrId", length = 50)
	private String shsqrId;

	@Column(name = "BE_shsqrName", length = 20)
	private String shsqrName;

	/** 审核申请机构 */
	@Column(name = "BE_shsqjgId", length = 50)
	private String shsqjgId;

	@Column(name = "BE_shsqjgName", length = 100)
	private String shsqjgName;

	/** 初审人 */
	@Column(name = "BE_shcsrId", length = 50)
	private String shcsrId;

	@Column(name = "BE_shcsrName", length = 20)
	private String shcsrName;

	/** 初审时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BE_shcssj", nullable = true, unique = false)
	@XmlTransient
	private Calendar shcssj;

	/** 复审人 */
	@Column(name = "BE_shfsrId", length = 50)
	private String shfsrId;

	@Column(name = "BE_shfsrName", length = 20)
	private String shfsrName;

	/** 复审时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BE_shfssj", nullable = true, unique = false)
	@XmlTransient
	private Calendar shfssj;

	/** 签发人 */
	@Column(name = "BE_shqfrId", length = 50)
	private String shqfrId;

	@Column(name = "BE_shqfrName", length = 20)
	private String shqfrName;

	/** 签发时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BE_shqfsj", nullable = true, unique = false)
	@XmlTransient
	private Calendar shqfsj;

	@Lob
	@Column(name = "stateChageLog")
	@Basic(fetch = FetchType.LAZY)
	@XmlTransient
	private String stateChageLog;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEbcn() {
		return ebcn;
	}

	public void setEbcn(String ebcn) {
		this.ebcn = ebcn;
	}

	public String getEntId() {
		return entId;
	}

	public void setEntId(String entId) {
		this.entId = entId;
	}

	public DataState getState() {
		return state;
	}

	public void setState(DataState state) {
		this.state = state;
	}



	public ApprovalStatus getShzt() {
		return shzt;
	}

	public void setShzt(ApprovalStatus shzt) {
		this.shzt = shzt;
	}

	public String getShsqyhId() {
		return shsqyhId;
	}

	public void setShsqyhId(String shsqyhId) {
		this.shsqyhId = shsqyhId;
	}

	public String getShsqyhName() {
		return shsqyhName;
	}

	public void setShsqyhName(String shsqyhName) {
		this.shsqyhName = shsqyhName;
	}

	public Calendar getShsqsj() {
		return shsqsj;
	}

	public void setShsqsj(Calendar shsqsj) {
		this.shsqsj = shsqsj;
	}

	public String getShsqrId() {
		return shsqrId;
	}

	public void setShsqrId(String shsqrId) {
		this.shsqrId = shsqrId;
	}

	public String getShsqrName() {
		return shsqrName;
	}

	public void setShsqrName(String shsqrName) {
		this.shsqrName = shsqrName;
	}

	public String getShsqjgId() {
		return shsqjgId;
	}

	public void setShsqjgId(String shsqjgId) {
		this.shsqjgId = shsqjgId;
	}

	public String getShsqjgName() {
		return shsqjgName;
	}

	public void setShsqjgName(String shsqjgName) {
		this.shsqjgName = shsqjgName;
	}

	public String getShcsrId() {
		return shcsrId;
	}

	public void setShcsrId(String shcsrId) {
		this.shcsrId = shcsrId;
	}

	public String getShcsrName() {
		return shcsrName;
	}

	public void setShcsrName(String shcsrName) {
		this.shcsrName = shcsrName;
	}

	public Calendar getShcssj() {
		return shcssj;
	}

	public void setShcssj(Calendar shcssj) {
		this.shcssj = shcssj;
	}

	public String getShfsrId() {
		return shfsrId;
	}

	public void setShfsrId(String shfsrId) {
		this.shfsrId = shfsrId;
	}

	public String getShfsrName() {
		return shfsrName;
	}

	public void setShfsrName(String shfsrName) {
		this.shfsrName = shfsrName;
	}

	public Calendar getShfssj() {
		return shfssj;
	}

	public void setShfssj(Calendar shfssj) {
		this.shfssj = shfssj;
	}

	public String getShqfrId() {
		return shqfrId;
	}

	public void setShqfrId(String shqfrId) {
		this.shqfrId = shqfrId;
	}

	public String getShqfrName() {
		return shqfrName;
	}

	public void setShqfrName(String shqfrName) {
		this.shqfrName = shqfrName;
	}

	public Calendar getShqfsj() {
		return shqfsj;
	}

	public void setShqfsj(Calendar shqfsj) {
		this.shqfsj = shqfsj;
	}

	public Calendar getLogTime() {
		return logTime;
	}

	public void setLogTime(Calendar logTime) {
		this.logTime = logTime;
	}

	public EntityStateChageLog(String ebcn, String entId, String organId,
			String userId, Calendar logTime, DataState state,
			String stateChageLog) {
		super();
		this.ebcn = ebcn;
		this.entId = entId;
		this.organId = organId;
		this.userId = userId;
		this.logTime = logTime;
		this.state = state;
		this.stateChageLog = stateChageLog;
	}

	public EntityStateChageLog() {
		super();
	}

	public String getOrganId() {
		return organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public String getStateChageLog() {
		return stateChageLog;
	}

	public void setStateChageLog(String stateChageLog) {
		this.stateChageLog = stateChageLog;
	}

}
