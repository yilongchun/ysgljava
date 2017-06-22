package com.vieking.basicdata.enumerable;

/**
 * 规则运行方式 <br>
 * *
 * 
 * <p>
 * <a href="RuleRunType.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 * 
 */
public enum RuleRunType {
	RRT001("EL表达式"), RRT002("Groovy"), RRT003("命名查询");

	private final String des;

	private RuleRunType(String des) {
		this.des = des;
	}

	public String getDes() {
		return des;
	}

}
