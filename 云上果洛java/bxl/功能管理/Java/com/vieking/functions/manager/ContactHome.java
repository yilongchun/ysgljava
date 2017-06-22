package com.vieking.functions.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.basicdata.model.Department;
import com.vieking.functions.model.Contact;
import com.vieking.functions.model.ContactPost;
import com.vieking.functions.model.DepartmentPost;
import com.vieking.resource.dao.ContactDao;
import com.vieking.resource.dao.FileDao;
import com.vieking.sys.tree.ContactTree;

/**
 * 通讯录管理 <br>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("fun.contactHome")
@AutoCreate
public class ContactHome extends BaseHome<Contact> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2574922872804105610L;
	
	@In(value = "contactDao")
	private ContactDao contactDao;

	/** 部门树 */
	protected ContactTree bumenTree = new ContactTree();

	public ContactPost cp;

	// 部门
	public Department dict;

	// 职务
	public DepartmentPost zhiwu;

	/** 部门code */
	public String bmCode;

	/** 选择部门 */
	public void selBuMen() {
		if (bmCode != null) {
			dict = entityManager.find(Department.class, bmCode);
		}
		bmCode = null;
	}

	@SuppressWarnings("unchecked")
	public List<DepartmentPost> getDp(String code) {
		return entityManager
				.createQuery(" from DepartmentPost o where o.bm.code =:code")
				.setParameter("code", code).getResultList();

	}

	/** 添加部门职务 */
	public void addBmzw() {
		if (dict != null) {
			boolean yes = true;
			for (Iterator<ContactPost> iterator = getInstance()
					.getContactPosts().iterator(); iterator.hasNext();) {
				ContactPost contactPost = iterator.next();
				if (contactPost.getLxrbm().getCode().equals(dict.getCode())
						) {
					yes = false;
					break;
				}
			}
			if (yes) {
				ContactPost cp = new ContactPost();
				cp.setLxrbm(dict);
//				cp.setLxrzw(zhiwu);
				getInstance().getContactPosts().add(cp);
			}
		} else {
			facesMessages.add("请先选择部门。");
		}
		
		
//		if (dict != null && zhiwu != null) {
//			boolean yes = true;
//			for (Iterator<ContactPost> iterator = getInstance()
//					.getContactPosts().iterator(); iterator.hasNext();) {
//				ContactPost contactPost = iterator.next();
//				if (contactPost.getLxrbm().getCode().equals(dict.getCode())
//						&& contactPost.getLxrzw().getCode()
//								.equals(zhiwu.getCode())) {
//					yes = false;
//					break;
//				}
//			}
//			if (yes) {
//				ContactPost cp = new ContactPost();
//				cp.setLxrbm(dict);
//				cp.setLxrzw(zhiwu);
//				getInstance().getContactPosts().add(cp);
//			}
//		} else {
//			facesMessages.add("请先选择部门职务。");
//		}
	}

	/** 删除部门职务 */
	public void delCp(ContactPost obj) {
		getInstance().getContactPosts().remove(obj);
	}

	public boolean isWired() {
		return true;
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public void saveBm() {
		

		List<String> exIds = new ArrayList<String>();
		for (Iterator<ContactPost> iterator = getInstance().getContactPosts()
				.iterator(); iterator.hasNext();) {
			ContactPost cp = iterator.next();
			cp.setContact(getInstance());
			entityManager.persist(cp);
			entityManager.flush();
			exIds.add(cp.getId());

		}
		if (exIds.isEmpty()) {
			exIds.add("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		}
		entityManager
				.createQuery(
						"delete ContactPost o where o.contact.id =:eId and o.id not in (:exIds)")
				.setParameter("eId", getInstance().getId())
				.setParameter("exIds", exIds).executeUpdate();

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

	public boolean validation() {
		boolean result = true;
		
//		if (isIdDefined()) {
//			
//
//		} else {
//			Contact o = getInstance();
//			int count = contactDao.getCountByEmail(o.getEmail());
//			
//			if (count > 0) {
//				facesMessages.add("邮箱已经存在，请重新输入");
//				result = false;
//			}
//		}
		
		return result;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String persist() {
		if (!validation()) {
			return null;
		}
		Contact o = getInstance();
		o.setUser(currUser);
		o.setCjsj(Calendar.getInstance());
		entityManager.persist(o);
		saveBm();
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
		Contact o = getInstance();
		entityManager.persist(o);
		entityManager.flush();
		saveBm();
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
		return "通讯录管理";
	}

	@Override
	public String instanceInfo() {
		return "通讯录管理";
	}

	public ContactPost getCp() {
		return cp;
	}

	public void setCp(ContactPost cp) {
		this.cp = cp;
	}

	public Department getDict() {
		return dict;
	}

	public void setDict(Department dict) {
		this.dict = dict;
	}

	public String getBmCode() {
		return bmCode;
	}

	public void setBmCode(String bmCode) {
		this.bmCode = bmCode;
	}

	public DepartmentPost getZhiwu() {
		return zhiwu;
	}

	public void setZhiwu(DepartmentPost zhiwu) {
		this.zhiwu = zhiwu;
	}

}
