package com.vieking.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.framework.Controller;
import org.jboss.seam.international.StatusMessage.Severity;
import org.richfaces.component.html.HtmlTreeNode;
import org.richfaces.event.NodeExpandedEvent;

import com.vieking.basicdata.dao.TreeDao;
import com.vieking.sys.base.BaseQueryHelp;
import com.vieking.sys.exception.DaoException;

public abstract class BaseTreeEnh<T> extends Controller {

	private static final long serialVersionUID = -1825212264339686604L;

	private Class<T> entityClass;

	private TreeDao dao;

	private String rootCode;

	private String nqn;

	private String orderBy;

	private boolean showRoot = true;

	private TreeNodeImplExt<T> rootNode;

	private TreeNodeImplExt<T> firstNode;

	protected Map<String, TreeNodeImplExt<T>> nodeMap = new HashMap<String, TreeNodeImplExt<T>>();

	private boolean onlySelLeaf = true;

	private boolean multSel = true;

	/** 获取 图标途径 */
	public abstract String getIconPath();

	/** 获取 查询参数Bean */
	public abstract BaseQueryHelp addNodeQhb(TreeNodeImplExt<T> node);

	/** 获取 节点查询Sql */
	public abstract String getDefaultRootId();

	public abstract void initNode(TreeNodeImplExt<T> node);

	/** 初始化子节点 是否可以展开 */
	public abstract void initChildNodeType(TreeNodeImplExt<T> parentNode);

	/**
	 * 为节点node加载第一层子节点
	 */
	@SuppressWarnings("unchecked")
	private void addNodes(TreeNodeImplExt<T> node) {
		if (node.getNodeId() == null)
			node.setNodeId(getDefaultRootId());
		List<T> list = null;
		try {
			list = getDao().query(nqn, orderBy, addNodeQhb(node));
		} catch (DaoException e) {
			getStatusMessages()
					.add(Severity.ERROR, "添加节点错误{0}", e.getMessage());
			return;
		}
		node.removeChild("fakeNode");
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				T o = (T) list.get(i);
				TreeNodeImplExt<T> tmpNode = new TreeNodeImplExt<T>();
				tmpNode.setData(o);
				initNode(tmpNode);
				tmpNode.setType(0);
				node.addChild(tmpNode.getNodeId(), tmpNode);
				nodeMap.put(tmpNode.getNodeId(), tmpNode);
			}
			initChildNodeType(node);

		} else
			node.setType(TreeNodeImplExt.leaf);

	}

	/**
	 * 处理展开与折叠操作
	 * 
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public void processExpansion(NodeExpandedEvent e) throws DaoException {
		HtmlTreeNode tNode = (HtmlTreeNode) e.getComponent();
		TreeNodeImplExt<T> o = (TreeNodeImplExt<T>) tNode.getData();
		if (o.getStatus() == 0) {// 当该结点处于折叠状态下，则加载该结点的第一次子结点
			addNodes(o);
			o.setStatus(1);
		}
	}

	@SuppressWarnings("unchecked")
	public TreeNodeImplExt<T> getTreeNode() {
		if (rootCode == null)
			return null;
		if (rootNode != null && !rootCode.equals(rootNode.getNodeId())) {
			rootNode = null;
		}
		if (rootNode == null) {
			rootNode = new TreeNodeImplExt<T>();
			if (showRoot) {
				T o = (T) getDao().load(getEntityClass(), rootCode);
				if (o != null) {
					firstNode = new TreeNodeImplExt<T>();
					firstNode.setData(o);
					rootNode.addChild(rootCode, firstNode);
					rootNode.setNodeId(rootCode);
					initNode(firstNode);
					addNodes(firstNode);
				}
			} else {
				addNodes(rootNode);
			}
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

	public BaseTreeEnh() {
		super();
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

	public BaseTreeEnh(EntityManager em, String rootCode, boolean onlySelLeaf,
			boolean multSel) {
		super();
		this.rootCode = rootCode;
		this.onlySelLeaf = onlySelLeaf;
		this.multSel = multSel;
	}

	public TreeNodeImplExt<T> getRootNode() {
		return rootNode;
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

	/**
	 * Get the class of the entity being managed. <br />
	 * If not explicitly specified, the generic type of implementation is used.
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getEntityClass() {
		if (entityClass == null) {
			Type type = getClass().getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				ParameterizedType paramType = (ParameterizedType) type;
				entityClass = (Class<T>) paramType.getActualTypeArguments()[0];
			} else {
				throw new IllegalArgumentException(
						"Could not guess entity class by reflection");
			}
		}
		return entityClass;
	}

	public BaseTreeEnh(String nqn, String orderBy) {
		super();
		this.nqn = nqn;
		this.orderBy = orderBy;
	}

	public BaseTreeEnh(String nqn, String orderBy, boolean showRoot,
			TreeNodeImplExt<T> rootNode) {
		super();
		this.nqn = nqn;
		this.orderBy = orderBy;
		this.showRoot = showRoot;
		this.rootNode = rootNode;
	}
}
