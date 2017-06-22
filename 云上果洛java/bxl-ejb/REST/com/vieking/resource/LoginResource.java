package com.vieking.resource;

import java.util.Iterator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.vieking.file.model.DocInfo;
import com.vieking.file.model.DocLink;
import com.vieking.functions.model.Contact;
import com.vieking.functions.model.ContactPost;
import com.vieking.resource.adapter.SysBeanTrans;
import com.vieking.resource.bean.BuMenBean;
import com.vieking.resource.bean.NumberMessage;
import com.vieking.resource.bean.ReturnMessage;
import com.vieking.resource.bean.UserBean;
import com.vieking.resource.bean.ZhiWuBean;
import com.vieking.resource.dao.ContactDao;
import com.vieking.resource.dao.FileDao;
import com.vieking.resource.dao.LoginDao;
import com.vieking.resource.dao.RegisterDao;
import com.vieking.role.model.User;
import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.KeException;
import com.vieking.sys.exception.ReConst;
import com.vieking.sys.util.Config;

@Path("login")
@Name("loginResource")
public class LoginResource implements ReConst {

	@Logger
	private Log log;
	@In
	private LoginDao loginDao;

	@In(value = "fileDao")
	private FileDao fileDao;

	@In
	private ContactDao contactDao;

	@In
	private RegisterDao registerDao;
	
	@In(value = "app.config")
	private Config config;

	// 用户登录
	@GET
	@Path("userLogin/{name}/{pwd}")
	@Produces("application/json;charset=UTF-8")
	public UserBean login(@PathParam("name") String name,
			@PathParam("pwd") String pwd) {
		User user = loginDao.login(name, pwd);
		if (user != null) {
			UserBean c = new UserBean();
			c = SysBeanTrans.toBean(user);
			Contact contact = contactDao.getContact(c.getPhone());
			if (contact != null) {
//				c.setImgUrl(fileDao.getFileUrl(fileDao.getFileLink(
//						contact.getId(), "TX") == null ? "" : fileDao
//						.getFileLink(contact.getId(), "TX").getDocument()
//						.getId()));
				
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
				c.setZhiwustr(contact.getPost());
				for (Iterator<ContactPost> iterator = contact.getContactPosts()
						.iterator(); iterator.hasNext();) {
					ContactPost contactPost = iterator.next();
					BuMenBean bm = new BuMenBean();
//					ZhiWuBean zw = new ZhiWuBean();
					bm.setBmcode(contactPost.getLxrbm().getCode());
					bm.setBumen(contactPost.getLxrbm().getName());
					bm.setSuperCode(contactPost.getLxrbm().getCode());
					bm.setWn(contactPost.getLxrbm().getWn());
//					zw.setZhiwu(contactPost.getLxrzw().getCode());
//					zw.setZwcode(contactPost.getLxrzw().getName());
					c.getBmlist().add(bm);
//					c.getZwlist().add(zw);
				}
			} else
				c.setImgUrl("");
			return c;
		}
		return new UserBean();
	}

	/** 验证码登录 */
	@GET
	@Path("validLogin/{phone}/{yzm}")
	@Produces("application/json;charset=UTF-8")
	public UserBean validLogin(@PathParam("phone") String phone,
			@PathParam("yzm") String yzm) {
		User user = loginDao.validlogin(phone, yzm);
		if (user != null) {
			UserBean c = new UserBean();
			c = SysBeanTrans.toBean(user);
			Contact contact = contactDao.getContact(c.getPhone());
			if (contact != null) {
//				c.setImgUrl(fileDao.getFileUrl(fileDao.getFileLink(
//						contact.getId(), "TX") == null ? "" : fileDao
//						.getFileLink(contact.getId(), "TX").getDocument()
//						.getId()));
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
				c.setZhiwustr(contact.getPost());
				for (Iterator<ContactPost> iterator = contact.getContactPosts()
						.iterator(); iterator.hasNext();) {
					ContactPost contactPost = iterator.next();
					BuMenBean bm = new BuMenBean();
//					ZhiWuBean zw = new ZhiWuBean();
					bm.setBmcode(contactPost.getLxrbm().getCode());
					bm.setBumen(contactPost.getLxrbm().getName());
					bm.setSuperCode(contactPost.getLxrbm().getCode());
					bm.setWn(contactPost.getLxrbm().getWn());
//					zw.setZhiwu(contactPost.getLxrzw().getCode());
//					zw.setZwcode(contactPost.getLxrzw().getName());
					c.getBmlist().add(bm);
//					c.getZwlist().add(zw);
				}
			} else
				c.setImgUrl("");
			return c;
		}
		return new UserBean();
	}

	// 验证码
	@GET
	@Path("validNumber/{phone}")
	@Produces("application/json;charset=UTF-8")
	public Response validNumber(@PathParam("phone") String phone) {
		User user = loginDao.getUser(phone);
		if (user != null) {
			int number = (int) (Math.random() * (9999 - 1000 + 1)) + 1000;
			if (loginDao.singleSend(user, number + "", Const.APIKEY, phone,
					"【云上果洛】您的验证码是" + number + "。如非本人操作，请忽略本短信"))
				return Response.ok(new NumberMessage(number + "", "1")).build();
		} else {
			return Response.ok(new NumberMessage()).build();
		}
		return Response.ok(new NumberMessage()).build();
	}

	// 获取验证码，任意手机号
	@GET
	@Path("validNumbers/{phone}")
	@Produces("application/json;charset=UTF-8")
	public Response validNumbers(@PathParam("phone") String phone) {
		int number = (int) (Math.random() * (9999 - 1000 + 1)) + 1000;
		if (loginDao.sendvalid(Const.APIKEY, phone, "【云上果洛】您的验证码是" + number
				+ "。如非本人操作，请忽略本短信"))
			return Response.ok(new NumberMessage(number + "", "1")).build();
		else
			return Response.ok(new NumberMessage()).build();
	}

	// 修改密码参数验证码
	@GET
	@Path("upPwdByValid/{phone}/{newPwd}/{yzm}")
	@Produces("application/json;charset=UTF-8")
	public Response upPwdByValid(@PathParam("phone") String phone,
			@PathParam("newPwd") String newPwd, @PathParam("yzm") String yzm) {
		if (loginDao.upPwdByValid(phone, newPwd, yzm))
			return Response.ok(new ReturnMessage(OS_OK, "密码修改成功")).build();
		return Response.ok(new ReturnMessage(OS_ERROR, "密码修改失败")).build();
	}
}
