package com.vieking.base;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.richfaces.component.html.HtmlTreeNode;
import org.richfaces.event.NodeExpandedEvent;

import com.vieking.basicdata.dao.TreeDao;

public abstract class BaseTree<T> implements Serializable {

	private static final long serialVersionUID = -5146967802447992427L;

	private EntityManager em;

	private TreeDao dao;

	private String rootCode;

	private TreeNodeImplExt<T> rootNode = null;

	private boolean onlySelLeaf = true;

	private boolean multSel = true;

	/** 获取 图标途径 */
	protected abstract String getIconPath();

	/** 获取 节点查询Sql */
	protected abstract String getSql();

	/** 获取 节点查询Sql */
	protected abstract String getDefaultRootId();

	protected abstract void initNode(TreeNodeImplExt<T> node);

	/**
	 * 为节点node加载第一层子节点
	 */
	@SuppressWarnings("unchecked")
	private void addNodes(TreeNodeImplExt<T> node) {
		if (node.getNodeId() == null)
			node.setNodeId(getDefaultRootId());
		List<T> list = null;
		if (em != null && em.isOpen()) {
			list = em.createQuery(getSql())
					.setParameter("supCode", node.getNodeId()).getResultList();
		} else {
			list = getDao().loadNodes(getSql(), node.getNodeId());
		}
		node.removeChild("fakeNode");
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				T o = (T) list.get(i);
				TreeNodeImplExt<T> tmpNode = new TreeNodeImplExt<T>();
				tmpNode.setData(o);
				initNode(tmpNode);
				tmpNode.setParent(node);
				node.addChild(new Integer(tmpNode.getNodeId()), tmpNode);
				if (tmpNode.getType() == TreeNodeImplExt.node) {
					// 构造一个假的的子结点
					TreeNodeImplExt<T> fakeNode = new TreeNodeImplExt<T>();
					// TreeNodeVO vo = new TreeNodeVO();
					fakeNode.setNodeName("loading...");
					fakeNode.setData(null);
					fakeNode.setIcon(getIconPath() + "loading.gif");
					fakeNode.setLeafIcon(getIconPath() + "loading.gif");
					fakeNode.setType(1);
					tmpNode.addChild("fakeNode", fakeNode);
				}
			}
		}

	}

	/**
	 * 加载节点id为nodeId的树
	 */
	@SuppressWarnings("unchecked")
	public void loadTree() {
		List<T> list = null;
		if (em != null && em.isOpen()) {
			list = em.createQuery(getSql()).setParameter("supCode", rootCode)
					.getResultList();
		} else {
			list = getDao().loadNodes(getSql(), rootCode);
		}
		for (Iterator<T> iterator = list.iterator(); iterator.hasNext();) {
			T eql = (T) iterator.next();
			rootNode = new TreeNodeImplExt<T>();
			rootNode.setData(eql);
			initNode(rootNode);
			rootNode.setParent(null);
			addNodes(rootNode);
		}
	}

	/**
	 * 处理展开与折叠操作
	 */
	@SuppressWarnings("unchecked")
	public void processExpansion(NodeExpandedEvent e) {
		HtmlTreeNode tNode = (HtmlTreeNode) e.getComponent();

		TreeNodeImplExt<T> o = (TreeNodeImplExt<T>) tNode.getData();
		if (o.getStatus() == 0) {// 当该结点处于折叠状态下，则加载该结点的第一次子结点
			addNodes(o);
			o.setStatus(1);
		}
	}

	public TreeNodeImplExt<T> getTreeNode() {
		if (rootCode == null)
			return null;
		if (rootNode != null && rootCode != null
				&& !rootCode.equals(rootNode.getNodeId())) {
			rootNode = null;
		}
		if (rootNode == null) {
			rootNode = new TreeNodeImplExt<T>();
			rootNode.setType(1);
			rootNode.setNodeId(rootCode);
			rootNode.setNodeName("xxxx");
			addNodes(rootNode);
		}
		return rootNode;
	}

	public void setTreeNode(TreeNodeImplExt<T> rootNode) {
		this.rootNode = rootNode;
	}

	public String getRootCode() {
		return rootCode;
	}

	public void setRootCode(String rootCode) {
		this.rootCode = rootCode;
	}

	public BaseTree(EntityManager em) {
		super();
		this.em = em;
	}

	public BaseTree() {
		super();
	}

	public BaseTree(EntityManager em, String rootCode) {
		super();
		this.em = em;
		this.rootCode = rootCode;
	}

	public boolean isOnlySelLeaf() {
		return onlySelLeaf;
	}

	public void setOnlySelLeaf(boolean onlySelLeaf) {
		this.onlySelLeaf = onlySelLeaf;
	}

	public boolean isMultSel() {
		return multSel;
	}

	public void setMultSel(boolean multSel) {
		this.multSel = multSel;
	}

	public BaseTree(EntityManager em, String rootCode, boolean onlySelLeaf,
			boolean multSel) {
		super();
		this.em = em;
		this.rootCode = rootCode;
		this.onlySelLeaf = onlySelLeaf;
		this.multSel = multSel;
	}

	public TreeNodeImplExt<T> getRootNode() {
		return rootNode;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public TreeDao getDao() {

		if (dao == null) {
			dao = (TreeDao) Component.getInstance("sys.dao.tree");
		}
		return dao;
	}

	public void setDao(TreeDao dao) {
		this.dao = dao;
	}

}
