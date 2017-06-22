package com.vieking.sys.enumerable;

/**
 * 提示级别 <br>
 * *
 * 
 * <p>
 * <a href="HintLevel.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 * 
 */
public enum HintLevel {
	正常("nor"), 一级警告("alOne"), 二级警告("alTwo"), 三级警告("alThree"), 报警("alOver");

	private final String css;

	private HintLevel(String css) {
		this.css = css;
	}

	public String getCss() {
		return css;
	}

}
