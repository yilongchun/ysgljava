package com.vieking.sys.exception;

public interface Const {

	public static final String SUCCEED = "SUCCEED";// 返回成功
	public static final String FAIL = "FAIL";// 返回失败

	public static final String CURRUSER = "currentUser";

	public static final String CURRUSERUSERAPPS = "bxl.user.apps";

	public static final String APIKEY = "b8fdb40ed5ae3a4e6b36a4201c4ee084";

	public static final String CLEAR_FILE_UPLOAD_EVENT = "clearFileUpload";
	public static final String IMAGE_DRAGGED_EVENT = "imageDraggedEvent";
	public static final String 文档上传完毕 = "uploadcomplete";
	public static final String 文档上传完毕V2 = "uploadcompleteV2";
	public static final String 文档链接发生变化 = "doclinkschanged";
	public static final String 文档链接发生变化Ebcn = "doclinkschangedEbcn";
	public static final String 清理文档链接界面数据 = "clearDocLinkUi";
	public static final String 添加已上传文件文档链接V2 = "addDocLinkForDocInfosV2";
	public static final String 添加已上传文件文档链接 = "addDocLinkForDocInfos";
	public static final String 添加应用文件类型 = "addAppDocTypeForDocTypes";

	public static final String CONFIGCHANGED = "configChanged";

	public static final String NAMEQUERYCHANGED = "nameQueryChanged";

	public static final String DICTIONARYCHANGED = "dictionaryChanged";

	public static final String USERTODODEFINECHANGED = "userToDoDefineChanged";
	public static final String SYSVARCHANGED = "sysVarChanged";
	// 外部配置文件中的Key
	public static final String KEY_UPLOADFILE_ROOT = "uploadFile.root";
	public static final String KEY_XLSTEMPLATE = "xlsTemplate";
	public static final String KEY_TEMPLATE_ROOT = "template.root";
	public static final String KEY_PROVINCE = "province";
	public static final String KEY_CITY = "city";
	public static final String KEY_LOCALCANTONCODE = "cantonCode";

	public static final String KEY_IDCOM = "COM,0,(,1,0,),1,0,0,1,1,6,2,COM1;9600;n;8;1";
	public static final String KEY_IDNET = "NET,1,02,1,1,0D0A03,3,0,0,0,0,8,1,1001";
	public static final String KEY_IDSETUP = "1";

	// 服务器信息
	public static final String KEY_SRVNAME = "server.name";
	public static final String KEY_SRVPWD = "server.password";
	public static final String KEY_SRVMASK = "server.mask";
	public static final String KEY_DATASYNCURL = "server.dataSyncUrl";
	public static final String KEY_LOGINUSERSESSIONMAXINACTIVEINTERVAL = "server.loginUser.session.MaxInactiveInterval";

	// openOffice模板中图片存放位置定义
	public static final String OOOTEMPIMAGE = "oooTempImage";

	// openOffice模板文件 文件夹
	public static final String OOOTEMPLATE = "oooTemplate";
	// openOffice应用程序 文件夹
	public static final String OOOAPPPATH = "oooAppPath";
	// 应用临时文件夹
	public static final String TMPFILE_OOO = "tmpFile.ooo";
	public static final String TMPFILE_IMG = "tmpFile.img";

}
