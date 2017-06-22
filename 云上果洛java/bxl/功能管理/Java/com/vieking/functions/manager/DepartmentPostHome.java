package com.vieking.functions.manager;

import java.util.List;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.basicdata.model.Department;
import com.vieking.functions.model.DepartmentPost;
import com.vieking.sys.tree.ContactTree;
import com.vieking.sys.util.FacesUtil;
import com.vieking.sys.util.Message;

/**
 * 职务管理 <br>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("fun.departmentPostHome")
@AutoCreate
public class DepartmentPostHome extends BaseHome<DepartmentPost> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2574922872804105610L;

	/** 部门树 */
	protected ContactTree bumenTree = new ContactTree();

	/** 部门code */
	public String bmCode;

	/** 选择部门 */
	public void selBuMen() {
		if (bmCode != null) {
			getInstance().setBm(entityManager.find(Department.class, bmCode));
		}
		bmCode = null;
	}

	public boolean isWired() {
		return true;
	}

	/***
	 * 页面初始化方法
	 */
	public void wire() {

		if (isIdDefined()) {
			getInstance();

		} else {
			clearInstance();
			getInstance();
		}
	}

	@Override
	public void create() {
		super.create();
		bumenTree.setRootCode("01");
	}

	@SuppressWarnings("unchecked")
	public boolean validation() {
		boolean result = true;
		List<DepartmentPost> dlist = entityManager
				.createQuery(" from DepartmentPost o where o.code=:code")
				.setParameter("code", getInstance().getCode()).getResultList();
		if (!dlist.isEmpty()) {
			FacesUtil.getExternalContext().getRequestMap()
					.put("_pops", new Message("提示消息", " 部门编号重复。"));
			return false;
		}
		return result;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String persist() {
		if (!validation()) {
			return null;
		}
		DepartmentPost o = getInstance();
		entityManager.persist(o);
		entityManager.flush();

		lastStateChange = super.persist();
		return lastStateChange;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String update() {
		if (!validation()) {
			return null;
		}
		DepartmentPost o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		lastStateChange = super.update();
		return lastStateChange;
	}

	public ContactTree getBumenTree() {
		return bumenTree;
	}

	public void setBumenTree(ContactTree bumenTree) {
		this.bumenTree = bumenTree;
	}

	@Override
	public String appName() {
		return "职务管理";
	}

	@Override
	public String instanceInfo() {
		return "职务管理";
	}

	public String getBmCode() {
		return bmCode;
	}

	public void setBmCode(String bmCode) {
		this.bmCode = bmCode;
	}

}
