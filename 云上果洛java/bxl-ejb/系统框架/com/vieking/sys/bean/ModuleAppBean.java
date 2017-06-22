package com.vieking.sys.bean;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.vieking.role.model.Application;

/**
 * <p>
 * 模块应用用于菜单显示 用于改善查询性能 <a href="ModuleAppBean.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
public class ModuleAppBean implements Serializable, Comparable<ModuleAppBean> {

	private static final long serialVersionUID = -3094724350144589687L;

	/** 应用 */
	private Application app;

	/** 应用别名 */
	private String appAlias;

	/** 路径定义为为页面直接请求 */
	private boolean isPageUrl = false;

	/** 路径定义为为页面直接请求 */
	private boolean isFullUrl = false;

	/** 菜单位置 */
	private int menuPos = 0;

	/** 应用访问路径 */
	private String url;

	public int compareTo(final ModuleAppBean other) {
		return new CompareToBuilder().append(menuPos, other.menuPos)
				.toComparison();
	}

	public ModuleAppBean() {
		super();
	}

	public Application getApp() {
		return app;
	}

	public void setApp(Application app) {
		this.app = app;
	}

	public int getMenuPos() {
		return menuPos;
	}

	public void setMenuPos(int menuPos) {
		this.menuPos = menuPos;
	}

	public ModuleAppBean(Application app, int menuPos) {
		super();
		this.app = app;
		this.menuPos = menuPos;
	}

	public String getAppAlias() {
		return appAlias;
	}

	public void setAppAlias(String appAlias) {
		this.appAlias = appAlias;
	}

	public ModuleAppBean(Application app, String appAlias, int menuPos) {
		super();
		this.app = app;
		this.appAlias = appAlias;
		this.menuPos = menuPos;
	}

	public boolean isPageUrl() {
		return isPageUrl;
	}

	public void setPageUrl(boolean isPageUrl) {
		this.isPageUrl = isPageUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isFullUrl() {
		return isFullUrl;
	}

	public void setFullUrl(boolean isFullUrl) {
		this.isFullUrl = isFullUrl;
	}

}
