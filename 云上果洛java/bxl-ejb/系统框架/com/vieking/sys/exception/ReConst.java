package com.vieking.sys.exception;



public interface ReConst {

	public static final String PASSWORDNULL = "bxl_null_password";// 客户端交密码为空时，提交此字符串
	public static final String REDOSTRIKEABALANCEPASSWORD = "test@vieking.com";// 测试密码

	public static final int STATE_INSERT = 1;
	public static final int STATE_UPDATE = 2;
	public static final int STATE_DELETE = 4;

	public static final int DATA_OP_INSERT = 1;
	public static final int DATA_OP_UPDATE = 2;
	public static final int DATA_OP_DELETE = 4;

	public static final int ERROR_LEVEL_NORMAL = 10;
	public static final int ERROR_LEVEL_THROW = 20;
	public static final int ERROR_LEVEL_STOP = 30;


	public static final String OS_OK = "200";// 操作成功

	public static final String OS_OK_PASSWORDNULL = "200.1";// 登录成功，密码为空
	public static final String OS_NOT = "401";// 用户名已注册
	public static final String OS_FAILED = "403";// 操作失败
	public static final String OS_ERROR = "403.1";// 身份证验证失败

	public static final String QN_CustomException = "kec";
	public static final String QN_DaoCustomException = "dce";
	public static final String QN_P_Info = "i";
	public static final String QN_P_ErrorGroup = "eg";
	public static final String QN_P_ErrorKey = "ek";
	public static final String QN_P_ErrorLevel = "l";
	public static final String QN_ReturnMessage = "remsg";
	public static final String QN_P_Coding = "coding"; 
	public static final String QN_P_Message = "msg";

	

}
