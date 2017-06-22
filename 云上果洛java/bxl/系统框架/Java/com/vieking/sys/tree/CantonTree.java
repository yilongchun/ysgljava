package com.vieking.sys.tree;

import com.vieking.base.BaseTree;
import com.vieking.base.TreeNodeImplExt;
import com.vieking.basicdata.model.Canton;

public class CantonTree extends BaseTree<Canton> {

	private static final long serialVersionUID = 320051008881203959L;

	@Override
	protected String getIconPath() {
		return "/img/tree/canton/";
	}

	@Override
	protected String getSql() {
		return "from Canton t where t.superior.code =:supCode order by t.code";
	}

	@Override
	protected void initNode(TreeNodeImplExt<Canton> node) {
		Canton o = node.getData();
		node.setNodeId(o.getCode());
		node.setType(o.isLeaf() ? TreeNodeImplExt.leaf : TreeNodeImplExt.node);
		node.setIcon(getIconPath() + o.getLevel().getIcon());
	}

	@Override
	protected String getDefaultRootId() {
		return "01";
	}
}
