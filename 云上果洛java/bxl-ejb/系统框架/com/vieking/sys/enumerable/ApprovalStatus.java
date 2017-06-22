package com.vieking.sys.enumerable;

/**
 * 数据审核状态 <br>
 * *
 * 
 * <p>
 * <a href="ApprovalStatus.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 * 
 */
public enum ApprovalStatus {

	AS001("待审核", DataState.锁定, RegistrationState.待审核, ApprovalStage.申请审核),

	AS100("审核通过", DataState.锁定, RegistrationState.已审核, ApprovalStage.审核通过);

	private final String desc;

	private final DataState ds;

	private final RegistrationState rs;

	private final ApprovalStage as;

	private ApprovalStatus(String desc, DataState ds, RegistrationState rs,
			ApprovalStage as) {
		this.desc = desc;
		this.ds = ds;
		this.rs = rs;
		this.as = as;
	}

	public String getDesc() {
		return desc;
	}

	public DataState getDs() {
		return ds;
	}

	public RegistrationState getRs() {
		return rs;
	}

	public ApprovalStage getAs() {
		return as;
	}

}
