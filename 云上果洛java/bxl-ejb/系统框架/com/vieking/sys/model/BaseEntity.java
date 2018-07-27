package com.vieking.sys.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.Length;

import com.vieking.sys.enumerable.DataState;
import com.vieking.sys.enumerable.HintLevel;
import com.vieking.sys.enumerable.RegistrationState;
import com.vieking.sys.util.CalendarUtil;

/**
 * <p>
 * 实体基类 <a href="BaseEntity.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable, Cloneable {

	private static final long serialVersionUID = -4664798927436013244L;

	@Version
	@Column(name = "BE_ver")
	private Integer version;

	/** 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BE_ct", nullable = false, unique = false)
	private Calendar ct = Calendar.getInstance();

	/** 创建者编码 */
	@Column(name = "BE_crtUid", length = 40, nullable = false, unique = false)
	private String crtUid = "System";

	/** 创建机构编码 */
	@Column(name = "BE_crtOid", length = 40, nullable = true, unique = false)
	private String crtOid = "System";

	/** 创建机构名称 */
	@Column(name = "BE_crtOname", length = 40, nullable = true, unique = false)
	private String crtOname = "System";

	/** 最后一次修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BE_lmt", nullable = true, unique = false)
	private Calendar lmt;

	/** 最后一次修改者编码 */
	@Column(name = "BE_lmUid", length = 40, nullable = true, unique = false)
	private String lmUid;

	/** 最后一次修改机构编码 */
	@Column(name = "BE_lmOid", length = 40, nullable = true, unique = false)
	private String lmOid;

	/** 最后一次修改机构名称 */
	@Column(name = "BE_lmOname", length = 40, nullable = true, unique = false)
	private String lmOname = "";

	/** 系统log */
	@Column(name = "BE_syslog", length = 500)
	@Basic(fetch = FetchType.LAZY)
	private String syslog;

	/** 用户备注说明 */
	@Column(name = "BE_remark", length = 5000)
	@Length(max = 5000)
	private String remark;

	/** 助记符 */
	@Column(name = "BE_zjf", length = 40, nullable = true, unique = false)
	private String zjf;

	/** 升级前数据ID */
	@Column(name = "BE_oid", length = 40, nullable = true, unique = false)
	private String oid;

	/** 数据同步标志 用于按位方式记录哪些服务器同步过数据 */
	@Column(name = "BE_syncSign", nullable = false, unique = false)
	private int syncSign = 0;

	/** 二次数据同步标志 */
	@Column(name = "BE_syncSos", nullable = false, unique = false)
	private int syncSos = 0;

	/** 数据状态 */
	@Enumerated(EnumType.STRING)
	@Column(name = "BE_state", nullable = false, unique = false)
	private DataState state = DataState.正常;

	/** 数据状态 */
	@Transient
	@XmlTransient
	private Calendar stateChageTime;

	/** 数据状态 */
	@Transient
	@XmlTransient
	private String stateChageLog;

	/** 数据填报状态 */
	@Enumerated(EnumType.STRING)
	@Column(length = 30, name = "BE_regState", nullable = true, unique = false)
	private RegistrationState regState;

	/** 审核时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BE_examine")
	private Calendar examine;

	/** 审核人名称 */
	@Column(name = "BE_examineUser", length = 40)
	private String examineUser = "";

	/** 是否选中 */
	@Transient
	private boolean selected = false;

	/** 是否备份数据 */
	@Transient
	private boolean backupData = false;

	/** 默认值 */
	@Transient
	private boolean defaultValue = false;

	/** 提示信息 */
	@Transient
	private String hint = "";

	/** tmpId */
	@Transient
	private String tmpId = UUID.randomUUID().toString();

	/** tmpId */
	@Transient
	private String tmpUiId;

	/** 提示级别 */
	@Transient
	private HintLevel hintLevel = HintLevel.正常;

	public void changeLog(String log) {
		if (!StringUtils.isEmpty(log)) {
			if (this.stateChageLog == null) {
				this.stateChageTime = Calendar.getInstance();
				this.stateChageLog = "";
			}
			this.stateChageLog = this.stateChageLog
					+ CalendarUtil.getDateTime() + ":" + log + "\r\n";
		}
	}

	public BaseEntity() {
		state = DataState.正常;
		regState = RegistrationState.填报中;
	}

	public boolean canRollBack() {
		if (regState == null) {
			return false;
		}
		if (regState == RegistrationState.已审核
				|| regState == RegistrationState.待审核
				|| regState == RegistrationState.填报中) {
			return true;
		}
		return false;
	}

	public boolean canSqsh() {
		if (regState == null) {
			return false;
		}
		if (regState == RegistrationState.填报中) {
			return true;
		}
		return false;
	}

	/** 是否可以保存(节能) */
	public boolean canSavFJN() {
		if (ct == null) {
			return false;
		}
		Calendar tmp = Calendar.getInstance();
		tmp.add(Calendar.DAY_OF_MONTH, -1);
		if (tmp.after(ct)) {
			return false;
		} else {
			return true;
		}
	}

	@XmlTransient
	public String getCrtOid() {
		return crtOid;
	}

	@XmlTransient
	public String getCrtUid() {
		return crtUid;
	}

	@XmlTransient
	public Calendar getCt() {
		return ct;
	}

	@XmlTransient
	public String getHint() {
		return hint;
	}

	@XmlTransient
	public HintLevel getHintLevel() {
		return hintLevel;
	}

	@XmlTransient
	public abstract String getId();

	@XmlTransient
	public String getLmOid() {
		return lmOid;
	}

	@XmlTransient
	public Calendar getLmt() {
		return lmt;
	}

	@XmlTransient
	public String getLmUid() {
		return lmUid;
	}

	@XmlTransient
	public String getOid() {
		return oid;
	}

	@XmlTransient
	public RegistrationState getRegState() {
		return regState;
	}

	@XmlTransient
	public String getRemark() {
		return remark;
	}

	@XmlTransient
	public DataState getState() {
		return state;
	}

	@XmlTransient
	public int getSyncSign() {
		return syncSign;
	}

	@XmlTransient
	public int getSyncSos() {
		return syncSos;
	}

	@XmlTransient
	public String getSyslog() {
		return syslog;
	}

	@XmlTransient
	public String getTmpId() {
		return tmpId;
	}

	@XmlTransient
	public String getTmpUiId() {
		if (tmpUiId == null) {
			tmpUiId = tmpId.substring(24, 36);
		}
		return tmpUiId;
	}

	@XmlTransient
	public Integer getVersion() {
		return version;
	}

	@XmlTransient
	public String getZjf() {
		return zjf;
	}

	@XmlTransient
	public boolean isBackupData() {
		return backupData;
	}

	@XmlTransient
	public boolean isSelected() {
		return selected;
	}

	public Calendar mt() {
		if (lmt == null) {
			return ct;
		}
		return lmt;
	}

	public boolean readOnly() {
		if (regState == null && version != null) {
			return true;
		}
		if (regState == null && version == null) {
			return state.readOnly();
		} else {
			return state.readOnly() || regState.readOnly();
		}
	}

	public void reset() {
		version = null;
		ct = Calendar.getInstance();
		crtUid = "System";
		crtOid = "System";
		crtOname = "System";
		lmt = null;
		lmUid = null;
		lmOname = null;
		lmOid = null;
		syslog = null;
		oid = null;
		syncSign = 0;
		syncSos = 0;
		state = DataState.正常;
		selected = false;
		regState = null;
		hint = "";
		hintLevel = HintLevel.正常;
		regState = RegistrationState.填报中;
	}

	public void setBackupData(boolean backupData) {
		this.backupData = backupData;
	}

	public void setCrtOid(String crtOid) {
		this.crtOid = crtOid;
	}

	public void setCrtUid(String crtUid) {
		this.crtUid = crtUid;
	}

	public void setCt(Calendar ct) {
		this.ct = ct;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public void setHintLevel(HintLevel hintLevel) {
		this.hintLevel = hintLevel;
	}

	public abstract void setId(String id);

	public void setLmOid(String lmOid) {
		this.lmOid = lmOid;
	}

	public void setLmt(Calendar lmt) {
		this.lmt = lmt;
	}

	public void setLmUid(String lmUid) {
		this.lmUid = lmUid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public void setRegState(RegistrationState regState) {
		this.regState = regState;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setState(DataState state) {
		this.state = state;
	}

	public void setSyncSign(int syncSign) {
		this.syncSign = syncSign;
	}

	public void setSyncSos(int syncSos) {
		this.syncSos = syncSos;
	}

	public void setSyslog(String syslog) {
		this.syslog = syslog;
	}

	public void setTmpId(String tmpId) {
		this.tmpId = tmpId;
	}

	public void setTmpUiId(String tmpUiId) {
		this.tmpUiId = tmpUiId;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public void setZjf(String zjf) {
		if (StringUtils.isNotEmpty(zjf) && zjf.length() > 40) {
			zjf = zjf.substring(0, 39);
		}
		this.zjf = zjf;
	}

	public abstract String toString();

	@XmlTransient
	public Calendar getStateChageTime() {
		return stateChageTime;
	}

	public void setStateChageTime(Calendar stateChageTime) {
		this.stateChageTime = stateChageTime;
	}

	@XmlTransient
	public String getStateChageLog() {
		return stateChageLog;
	}

	public void setStateChageLog(String stateChageLog) {
		this.stateChageLog = stateChageLog;
	}

	public String getCrtOname() {
		return crtOname;
	}

	public void setCrtOname(String crtOname) {
		this.crtOname = crtOname;
	}

	public String getLmOname() {
		return lmOname;
	}

	public void setLmOname(String lmOname) {
		this.lmOname = lmOname;
	}

	public boolean isDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Calendar getExamine() {
		return examine;
	}

	public void setExamine(Calendar examine) {
		this.examine = examine;
	}

	public String getExamineUser() {
		return examineUser;
	}

	public void setExamineUser(String examineUser) {
		this.examineUser = examineUser;
	}

}
