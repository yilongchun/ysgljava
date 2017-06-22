package com.vieking.base;

import org.richfaces.model.TreeNodeImpl;

public class TreeNodeImplExt<T> extends TreeNodeImpl<T> {

	private static final long serialVersionUID = 9128638395745665632L;

	public static final int node = 1;

	public static final int leaf = 0;

	private int id;

	/** 节点名 */
	private String nodeName;

	/** 节点图标 */
	private String icon;

	/** 节点展开图标 */
	private String iconExp;

	/** 节点关闭时图标 */
	private String iconColl;

	/** 末级节点图标 */
	private String leafIcon;

	/** 节点id */
	private String nodeId;

	/** 节点类型：1-节点 0-叶子节点 */
	private int type;

	/** 节点的状态： 1-展开状态 0-折叠状态 */
	private int status;//

	private boolean selected = false;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getLeafIcon() {
		return leafIcon;
	}

	public void setLeafIcon(String leafIcon) {
		this.leafIcon = leafIcon;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getIconExp() {
		return iconExp;
	}

	public void setIconExp(String iconExp) {
		this.iconExp = iconExp;
	}

	public String getIconColl() {
		return iconColl;
	}

	public void setIconColl(String iconColl) {
		this.iconColl = iconColl;
	}

}
