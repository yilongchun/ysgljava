package com.vieking.sys.tree;


import org.jboss.seam.Component;

import com.vieking.base.BaseTree;
import com.vieking.base.TreeNodeImplExt;
import com.vieking.basicdata.dao.DictionaryDao;
import com.vieking.basicdata.dao.TreeDao;
import com.vieking.basicdata.model.Department;

public class ContactTree extends BaseTree<Department> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2023938694802351896L;
	
	
	private DictionaryDao dictionaryDao;
	

	public DictionaryDao getDictionaryDao() {
		if (dictionaryDao == null) {
			dictionaryDao = (DictionaryDao) Component.getInstance("dictionaryDao");
		}
		return dictionaryDao;
	}

	public void setDictionaryDao(DictionaryDao dictionaryDao) {
		this.dictionaryDao = dictionaryDao;
	}

	@Override
	protected String getIconPath() {
		return "/img/tree/canton/";
	}

	@Override
	protected String getSql() {
		return "from Department t where t.superior.code =:supCode order by t.code";
	}

	@Override
	protected void initNode(TreeNodeImplExt<Department> node) {
		Department o = node.getData();
		node.setNodeId(o.getCode());
		
//		if (o != null) {
//			boolean flag = getDictionaryDao().getDepartmentById(o.getCode());
//			if (flag) {
//				node.setType(TreeNodeImplExt.leaf);
//			}else{
//				node.setType(TreeNodeImplExt.node);
//			}
//		}
		
		
		node.setType(o.isLeaf() ? TreeNodeImplExt.leaf : TreeNodeImplExt.node);
		node.setIcon(getIconPath() + "002.jpg");
	}

	@Override
	protected String getDefaultRootId() {
		return "01";
	}
}
