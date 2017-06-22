package com.vieking.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.vieking.basicdata.model.Department;
import com.vieking.basicdata.model.Dictionary;
import com.vieking.file.model.DocInfo;
import com.vieking.file.model.DocLink;
import com.vieking.functions.model.Contact;
import com.vieking.functions.model.ContactPost;
import com.vieking.resource.adapter.SysBeanTrans;
import com.vieking.resource.bean.BmLxrBean;
import com.vieking.resource.bean.BuMenBean;
import com.vieking.resource.bean.ContactBean;
import com.vieking.resource.dao.ContactDao;
import com.vieking.resource.dao.FileDao;
import com.vieking.resource.dao.LoginDao;
import com.vieking.role.model.User;
import com.vieking.sys.exception.KeException;
import com.vieking.sys.exception.ReConst;
import com.vieking.sys.util.Config;

@Path("contact")
@Name("contactResource")
public class ContactResource implements ReConst {

	@Logger
	private Log log;
	@In
	private LoginDao loginDao;
	@In
	private ContactDao contactDao;

	@In(value = "fileDao")
	private FileDao fileDao;
	
	@In(value = "app.config")
	private Config config;

	// 获取通讯录信息
	@GET
	@Path("getContacts/{phone}")
	@Produces("application/json;charset=UTF-8")
	public Collection<ContactBean> getContacts(@PathParam("phone") String phone) {
		List<ContactBean> list = new ArrayList<ContactBean>();
		for (Iterator<Contact> iterator = contactDao.getContacts(phone)
				.iterator(); iterator.hasNext();) {
			Contact dac = iterator.next();
			ContactBean c = new ContactBean();
			c = SysBeanTrans.toBean(dac);
//			c.setImgUrl(fileDao
//					.getFileUrl(fileDao.getFileLink(c.getId(), "TX") == null ? ""
//							: fileDao.getFileLink(c.getId(), "TX")
//									.getDocument().getId()));
			DocLink link = fileDao.getFileLink(
					c.getId(), "TX");
			if (link == null) {
				c.setImgUrl("");
			}else{
				DocInfo info = link.getDocument();
				
				String webUrl = "";
				try {
					webUrl =  config.get("webUrl");
				} catch (KeException e) {
					e.printStackTrace();
				}
				
				String path = webUrl + info.getUrlName() + "/" + info.getOriginalName();
				c.setImgUrl(path);
			}
			list.add(c);
		}
		return list;
	}

	// 获取联系人
	@GET
	@Path("getContact/{phone}")
	@Produces("application/json;charset=UTF-8")
	public ContactBean getContact(@PathParam("phone") String phone) {
		Contact contact = contactDao.getContact(phone);
		ContactBean c = new ContactBean();
		c = SysBeanTrans.toBean(contact);
//		c.setImgUrl(fileDao
//				.getFileUrl(fileDao.getFileLink(c.getId(), "TX") == null ? ""
//						: fileDao.getFileLink(c.getId(), "TX").getDocument()
//								.getId()));
		DocLink link = fileDao.getFileLink(
				contact.getId(), "TX");
		if (link == null) {
			c.setImgUrl("");
		}else{
			DocInfo info = link.getDocument();
			
			String webUrl = "";
			try {
				webUrl =  config.get("webUrl");
			} catch (KeException e) {
				e.printStackTrace();
			}
			
			String path = webUrl + info.getUrlName() + "/" + info.getOriginalName();
			c.setImgUrl(path);
		}
		return c;
	}
	
	// 获取联系人 添加userid 用于权限判断
		@GET
		@Path("getContact/{userId}/{phone}")
		@Produces("application/json;charset=UTF-8")
		public ContactBean getContact(@PathParam("userId") String userId,@PathParam("phone") String phone) {
			String myLevel = "";
			User user = loginDao.getUserById(userId);
			if (user != null) {
				Contact userContact = contactDao.getContact(user.getPhone());
				myLevel = userContact.getLevel().getCode();
			}
			
			Contact contact = contactDao.getContact(phone);
			Dictionary dic = contact.getLevel();
			if(dic == null){
				return new ContactBean();
			}else{
				String userLevel = contact.getLevel().getCode();
				if (myLevel.equals("A") || myLevel.equals("B")) {
					ContactBean c = new ContactBean();
					c = SysBeanTrans.toBean(contact);
//					c.setImgUrl(fileDao
//							.getFileUrl(fileDao.getFileLink(c.getId(), "TX") == null ? ""
//									: fileDao.getFileLink(c.getId(), "TX").getDocument()
//											.getId()));
					
					DocLink link = fileDao.getFileLink(
							contact.getId(), "TX");
					if (link == null) {
						c.setImgUrl("");
					}else{
						DocInfo info = link.getDocument();
						
						String webUrl = "";
						try {
							webUrl =  config.get("webUrl");
						} catch (KeException e) {
							e.printStackTrace();
						}
						
						String path = webUrl + info.getUrlName() + "/" + info.getOriginalName();
						c.setImgUrl(path);
					}
					
					
					return c;
				}
				if (myLevel.equals("C") || myLevel.equals("D")) {
					if (userLevel.equals("A")) {
						return new ContactBean();
					}else{
						ContactBean c = new ContactBean();
						c = SysBeanTrans.toBean(contact);
//						c.setImgUrl(fileDao
//								.getFileUrl(fileDao.getFileLink(c.getId(), "TX") == null ? ""
//										: fileDao.getFileLink(c.getId(), "TX").getDocument()
//												.getId()));
						DocLink link = fileDao.getFileLink(
								contact.getId(), "TX");
						if (link == null) {
							c.setImgUrl("");
						}else{
							DocInfo info = link.getDocument();
							
							String webUrl = "";
							try {
								webUrl =  config.get("webUrl");
							} catch (KeException e) {
								e.printStackTrace();
							}
							
							String path = webUrl + info.getUrlName() + "/" + info.getOriginalName();
							c.setImgUrl(path);
						}
						return c;
					}
				}
				return new ContactBean();
			}
		}

	// 获取联系人
	@GET
	@Path("getContactByName/{name}")
	@Produces("application/json;charset=UTF-8")
	public Collection<ContactBean> getContactByName(
			@PathParam("name") String name) {
		List<ContactBean> list = new ArrayList<ContactBean>();
		for (Iterator<Contact> iterator = contactDao.getContactByName(name)
				.iterator(); iterator.hasNext();) {
			Contact dac = iterator.next();
			ContactBean c = new ContactBean();
			c = SysBeanTrans.toBean(dac);
//			c.setImgUrl(fileDao
//					.getFileUrl(fileDao.getFileLink(c.getId(), "TX") == null ? ""
//							: fileDao.getFileLink(c.getId(), "TX")
//									.getDocument().getId()));
			DocLink link = fileDao.getFileLink(
					c.getId(), "TX");
			if (link == null) {
				c.setImgUrl("");
			}else{
				DocInfo info = link.getDocument();
				
				String webUrl = "";
				try {
					webUrl =  config.get("webUrl");
				} catch (KeException e) {
					e.printStackTrace();
				}
				
				String path = webUrl + info.getUrlName() + "/" + info.getOriginalName();
				c.setImgUrl(path);
			}
			list.add(c);
		}
		return list;
	}

	// 获取通讯录信息
	@GET
	@Path("getContacts/")
	@Produces("application/json;charset=UTF-8")
	public Collection<ContactBean> getContacts() {
		List<ContactBean> list = new ArrayList<ContactBean>();
		for (Iterator<Contact> iterator = contactDao.getContacts().iterator(); iterator
				.hasNext();) {
			Contact dac = iterator.next();
			ContactBean c = new ContactBean();
			c = SysBeanTrans.toBean(dac);
//			c.setImgUrl(fileDao
//					.getFileUrl(fileDao.getFileLink(c.getId(), "TX") == null ? ""
//							: fileDao.getFileLink(c.getId(), "TX")
//									.getDocument().getId()));
			DocLink link = fileDao.getFileLink(
					c.getId(), "TX");
			if (link == null) {
				c.setImgUrl("");
			}else{
				DocInfo info = link.getDocument();
				
				String webUrl = "";
				try {
					webUrl =  config.get("webUrl");
				} catch (KeException e) {
					e.printStackTrace();
				}
				
				String path = webUrl + info.getUrlName() + "/" + info.getOriginalName();
				c.setImgUrl(path);
			}
			list.add(c);
		}
		return list;
	}

	// 获取部门信息
	@GET
	@Path("getBuMens")
	@Produces("application/json;charset=UTF-8")
	public Collection<BuMenBean> getBuMens() {
		List<BuMenBean> list = new ArrayList<BuMenBean>();
		for (Iterator<Department> iterator = contactDao.getDepartments()
				.iterator(); iterator.hasNext();) {
			Department d = iterator.next();
			BuMenBean b = new BuMenBean();
			b.setBmcode(d.getCode());
			b.setBumen(d.getName());
			b.setSuperCode(d.getSuperior() == null ? "" : d.getSuperior()
					.getCode());
			b.setWn(d.getWn());
			list.add(b);
		}
		return list;
	}

	// 获取部门信息
	@GET
	@Path("getBuMens/{superCode}")
	@Produces("application/json;charset=UTF-8")
	public Collection<BuMenBean> getBuMens(
			@PathParam("superCode") String superCode) {
		List<BuMenBean> list = new ArrayList<BuMenBean>();
		for (Iterator<Department> iterator = contactDao.getDepartments(
				superCode).iterator(); iterator.hasNext();) {
			Department d = iterator.next();
			BuMenBean b = new BuMenBean();
			b.setBmcode(d.getCode());
			b.setBumen(d.getName());
			b.setSuperCode(d.getSuperior().getCode());
			b.setWn(d.getWn());
			list.add(b);
		}
		return list;
	}

	// 获取部门信息
	@GET
	@Path("getBuMen/{code}")
	@Produces("application/json;charset=UTF-8")
	public BuMenBean getBuMen(@PathParam("code") String code) {
		Department d = contactDao.findDepartment(code);
		BuMenBean b = new BuMenBean();
		b.setBmcode(d.getCode());
		b.setBumen(d.getName());
		b.setSuperCode(d.getSuperior().getCode());
		b.setWn(d.getWn());

		return b;
	}

	// 获取下属部门和联系人
	@GET
	@Path("getBmLxr/{superCode}")
	// 部门ID编码
	@Produces("application/json;charset=UTF-8")
	public BmLxrBean getBmLxr(@PathParam("superCode") String superCode) {
		BmLxrBean bmlxr = new BmLxrBean();
		List<Department> dlist = contactDao.getDepartments(superCode);
		List<Contact> clist = contactDao.getContactByBm(superCode);
		Department d = contactDao.findDepartment(superCode);
		bmlxr.setBmcode(d.getCode());
		bmlxr.setBumen(d.getName());
		int[] rs = new int[2];
		if ("01".equals(superCode))
			rs = contactDao.getZrs(superCode);
		else
			rs = contactDao.getBmZrs(superCode);
		bmlxr.setZrs(rs[0]);
		bmlxr.setZcrs(rs[1]);
		for (Iterator<Department> iterator = dlist.iterator(); iterator
				.hasNext();) {
			Department cepartment = iterator.next();
			BuMenBean bm = new BuMenBean();
			bm.setBmcode(cepartment.getCode());
			bm.setBumen(cepartment.getName());
			bm.setSuperCode(cepartment.getSuperior().getCode());
			bm.setWn(cepartment.getWn());
			int[] bmrs = contactDao.getBmZrs(cepartment.getCode());
			bm.setZrs(bmrs[0]);
			bm.setZcrs(bmrs[1]);
			bmlxr.getBmlist().add(bm);
		}
		for (Iterator<Contact> iterator = clist.iterator(); iterator.hasNext();) {
			Contact contact = iterator.next();
			ContactBean lxr = new ContactBean();
			lxr.setId(contact.getId());
			lxr.setName(contact.getName());
			lxr.setPhone(contact.getPhone());
			lxr.setZhuce(contact.getReg());
//			for (Iterator<ContactPost> iterator2 = contact.getContactPosts()
//					.iterator(); iterator2.hasNext();) {
//				ContactPost cp = iterator2.next();
//				if (cp.getLxrbm().getCode().equals(superCode)) {
//					lxr.setZhiwustr(cp.getLxrzw().getName());
//					break;
//				}
//			}
			lxr.setZhiwustr(contact.getPost());
//			lxr.setImgUrl(fileDao.getFileUrl(fileDao.getFileLink(lxr.getId(),
//					"TX") == null ? "" : fileDao.getFileLink(lxr.getId(), "TX")
//					.getDocument().getId()));
			DocLink link = fileDao.getFileLink(
					lxr.getId(), "TX");
			if (link == null) {
				lxr.setImgUrl("");
			}else{
				DocInfo info = link.getDocument();
				
				String webUrl = "";
				try {
					webUrl =  config.get("webUrl");
				} catch (KeException e) {
					e.printStackTrace();
				}
				
				String path = webUrl + info.getUrlName() + "/" + info.getOriginalName();
				lxr.setImgUrl(path);
			}
			bmlxr.getLxrlist().add(lxr);
		}

		return bmlxr;
	}

	// 获取上级部门、下级部门和联系人
	@GET
	@Path("getBmLxrSj/{code}")
	// 部门ID编码
	@Produces("application/json;charset=UTF-8")
	public BmLxrBean getBmLxrSj(@PathParam("code") String code) {

		BmLxrBean bmlxr = new BmLxrBean();
		List<Department> sjlist = contactDao.getDepartmentsSj(code);
		List<Department> dlist = contactDao.getDepartments(code);
		List<Contact> clist = contactDao.getContactByBm(code);
		Department d = contactDao.findDepartment(code);
		bmlxr.setBmcode(d.getCode());
		bmlxr.setBumen(d.getName());
		int[] rs = new int[2];
		if ("01".equals(code))
			rs = contactDao.getZrs(code);
		else
			rs = contactDao.getBmZrs(code);
		bmlxr.setZrs(rs[0]);
		bmlxr.setZcrs(rs[1]);
		for (Iterator<Department> iterator = sjlist.iterator(); iterator
				.hasNext();) {
			Department cepartment = iterator.next();
			BuMenBean bm = new BuMenBean();
			bm.setBmcode(cepartment.getCode());
			bm.setBumen(cepartment.getName());
			bm.setSuperCode(cepartment.getSuperior() == null ? "" : cepartment
					.getSuperior().getCode());
			bm.setWn(cepartment.getWn());
			int[] bmrs = contactDao.getBmZrs(cepartment.getCode());
			bm.setZrs(bmrs[0]);
			bm.setZcrs(bmrs[1]);
			bmlxr.getSjbmlist().add(bm);
		}
		for (Iterator<Department> iterator = dlist.iterator(); iterator
				.hasNext();) {
			Department cepartment = iterator.next();
			BuMenBean bm = new BuMenBean();
			bm.setBmcode(cepartment.getCode());
			bm.setBumen(cepartment.getName());
			bm.setSuperCode(cepartment.getSuperior().getCode());
			bm.setWn(cepartment.getWn());
			int[] bmrs = contactDao.getBmZrs(cepartment.getCode());
			bm.setZrs(bmrs[0]);
			bm.setZcrs(bmrs[1]);
			bmlxr.getBmlist().add(bm);
		}
		for (Iterator<Contact> iterator = clist.iterator(); iterator.hasNext();) {
			Contact contact = iterator.next();
			ContactBean lxr = new ContactBean();
			lxr.setId(contact.getId());
			lxr.setName(contact.getName());
			lxr.setPhone(contact.getPhone());
			lxr.setZhuce(contact.getReg());
//			for (Iterator<ContactPost> iterator2 = contact.getContactPosts()
//					.iterator(); iterator2.hasNext();) {
//				ContactPost cp = iterator2.next();
//				if (cp.getLxrbm().getCode().equals(code)) {
//					lxr.setZhiwustr(cp.getLxrzw().getName());
//					break;
//				}
//			}
			lxr.setZhiwustr(contact.getPost());
//			lxr.setImgUrl(fileDao.getFileUrl(fileDao.getFileLink(lxr.getId(),
//					"TX") == null ? "" : fileDao.getFileLink(lxr.getId(), "TX")
//					.getDocument().getId()));
			DocLink link = fileDao.getFileLink(
					contact.getId(), "TX");
			if (link == null) {
				lxr.setImgUrl("");
			}else{
				DocInfo info = link.getDocument();
				
				String webUrl = "";
				try {
					webUrl =  config.get("webUrl");
				} catch (KeException e) {
					e.printStackTrace();
				}
				
				String path = webUrl + info.getUrlName() + "/" + info.getOriginalName();
				lxr.setImgUrl(path);
			}
			bmlxr.getLxrlist().add(lxr);
		}

		return bmlxr;
	}

}
