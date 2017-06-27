package com.vieking.resource.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;

import com.vieking.basicdata.model.Department;
import com.vieking.functions.model.Contact;
import com.vieking.sys.base.BaseDaoHibernate;

@Name("contactDao")
@AutoCreate
public class ContactDao extends BaseDaoHibernate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7086520284887656617L;

	@SuppressWarnings("unchecked")
	public List<Contact> getContactByName(String name) {
		return entityManager
				.createQuery(
						" from Contact o where o.name  like concat('%',concat(lower(:name)),'%') ")
				.setParameter("name", name).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Contact> getContacts() {
		return entityManager.createQuery(" from Contact o  ").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Contact> getContacts(String phone) {
		return entityManager
				.createQuery(" from Contact o  where o.phone !=:phone")
				.setParameter("phone", phone).getResultList();
	}

	public Contact getContact(String phone) {
		Contact c = (Contact) entityManager
				.createQuery(" from Contact o  where o.phone =:phone")
				.setParameter("phone", phone).getResultList().get(0);
		return c;
	}

	@SuppressWarnings("unchecked")
	public List<Department> getDepartments(String superCode) {
		return entityManager
				.createQuery(
						" from Department o where o.superior.code = :superCode")
				.setParameter("superCode", superCode).getResultList();
	}

	public List<Department> getDepartmentsSj(String code) {
		List<Department> dlist = new ArrayList<Department>();
		String codes[] = getDepartmentsCode(code).split(",");
		for (int i = 0; i < codes.length; i++) {
			if (!"".equals(codes[i])) {
				Department department = entityManager.find(Department.class,
						codes[i]);
				dlist.add(department);
			}
		}
		return dlist;
	}

	public String getDepartmentsCode(String code) {
		Department department = entityManager.find(Department.class, code);
		if (department != null) {
			String dcode = department.getCode() + ",";
			String supeCode = "";
			if (department.getSuperior() != null)
				supeCode = getDepartmentsCode(department.getSuperior()
						.getCode());
			return supeCode + dcode;
		} else {
			return "";
		}
	}

	@SuppressWarnings("unchecked")
	public List<Department> getDepartments() {
		return entityManager.createQuery(" from Department o ").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Contact> getContactByBm(String superCode) {
		return entityManager
				.createQuery(
						" from Contact o where o.id in  (select cp.contact.id  from ContactPost  cp where cp.lxrbm.code = :superCode)")
				.setParameter("superCode", superCode).getResultList();
	}

	public Department findDepartment(String code) {
		return entityManager.find(Department.class, code);
	}

	@SuppressWarnings({ "unchecked" })
	public int[] getBmZrs(String superCode) {
		int result[] = new int[2];
		List<Department> dlist = entityManager
				.createQuery(
						" from Department o where o.code like concat(concat(lower(:superCode)),'%')) ")
				.setParameter("superCode", superCode).getResultList();
		int i = 0;
		int j = 0;
		for (Iterator<Department> iterator = dlist.iterator(); iterator
				.hasNext();) {
			Department department = iterator.next();
			List<Contact> list = new ArrayList<Contact>();
			list = entityManager
					.createQuery(
							" from Contact o where o.id in  (select cp.contact.id  from ContactPost  cp where cp.lxrbm.code = :code)")
					.setParameter("code", department.getCode()).getResultList();
			if (!list.isEmpty())
				i = i + list.size();
			// result[0] = list.size();
			list = entityManager
					.createQuery(
							" from Contact o where  o.reg=1 and o.id in  (select cp.contact.id  from ContactPost  cp where cp.lxrbm.code = :code)  ")
					.setParameter("code", department.getCode()).getResultList();
			if (!list.isEmpty())
				j = j + list.size();
			// result[1] = list.size();
		}
		result[0] = i;
		result[1] = j;

		return result;
	}

	@SuppressWarnings("unchecked")
	public int[] getZrs(String code) {
		int[] result = new int[2];
		List<Department> dlist = entityManager.createQuery(
				" from Department o  ").getResultList();
		int i = 0;
		int j = 0;
		for (Iterator<Department> iterator = dlist.iterator(); iterator
				.hasNext();) {
			Department department = iterator.next();
			List<Contact> list = new ArrayList<Contact>();
			list = entityManager
					.createQuery(
							" from Contact o where o.id in  (select cp.contact.id  from ContactPost  cp where cp.lxrbm.code = :code)")
					.setParameter("code", department.getCode()).getResultList();
			if (!list.isEmpty())
				i = i + list.size();
			list = entityManager
					.createQuery(
							" from Contact o where o.reg=1 and o.id in  (select cp.contact.id  from ContactPost  cp where cp.lxrbm.code = :code)")
					.setParameter("code", department.getCode()).getResultList();
			if (!list.isEmpty())
				j = j + list.size();
		}
		result[0] = i;
		result[1] = j;
		return result;
	}

	@SuppressWarnings("unchecked")
	public int getCountByEmail(String email) {
		List<Contact> list = entityManager
				.createQuery("from Contact o  where o.email=:email")
				.setParameter("email",
						StringUtils.lowerCase(StringUtils.trim(email)))
				.getResultList();
		return list.size();
	}
}
