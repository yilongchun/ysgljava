package com.vieking.sys.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * <p>
 * 模块Bean 用于菜单显示 用于改善查询性能 <a href="ModuleAppBean.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
public class ModuleBean implements Serializable, Comparable<ModuleBean> {

	private static final long serialVersionUID = -3094724350144589687L;

	private String name;

	/** 描述 */
	private String des;

	/** 菜单位置 */
	private int menuPos = 0;

	/** 显示图标 */
	private String ico;

	/** 是否在菜单上显示 */
	private boolean showInMenu = true;

	/** 是否在桌面显示 */
	private boolean showInDesktop = true;

	/** 模块应用 */
	private List<ModuleAppBean> mas = new ArrayList<ModuleAppBean>();

	public int compareTo(final ModuleBean other) {
		return new CompareToBuilder().append(menuPos, other.menuPos)
				.toComparison();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getMenuPos() {
		return menuPos;
	}

	public void setMenuPos(int menuPos) {
		this.menuPos = menuPos;
	}

	public ModuleBean() {
		super();
	}

	public List<ModuleAppBean> getMas() {
		return mas;
	}

	public void setMas(List<ModuleAppBean> mas) {
		this.mas = mas;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public ModuleBean(String name, String des, int menuPos, String ico) {
		super();
		this.name = name;
		this.des = des;
		this.menuPos = menuPos;
		this.ico = ico;
	}

	public boolean isShowInMenu() {
		return showInMenu;
	}

	public void setShowInMenu(boolean showInMenu) {
		this.showInMenu = showInMenu;
	}

	public boolean isShowInDesktop() {
		return showInDesktop;
	}

	public void setShowInDesktop(boolean showInDesktop) {
		this.showInDesktop = showInDesktop;
	}

}
