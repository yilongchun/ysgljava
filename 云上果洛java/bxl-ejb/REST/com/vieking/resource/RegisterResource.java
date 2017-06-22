package com.vieking.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.vieking.functions.model.Contact;
import com.vieking.resource.adapter.SysBeanTrans;
import com.vieking.resource.bean.NumberMessage;
import com.vieking.resource.bean.ReturnMessage;
import com.vieking.resource.bean.UserBean;
import com.vieking.resource.dao.ContactDao;
import com.vieking.resource.dao.FileDao;
import com.vieking.resource.dao.RegisterDao;
import com.vieking.role.model.User;
import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.ReConst;

@Path("register")
@Name("registerResource")
public class RegisterResource implements ReConst {

	@Logger
	private Log log;

	@In
	private RegisterDao registerDao;

	@In
	private ContactDao contactDao;

	@In(value = "fileDao")
	private FileDao fileDao;

	// 验证号码
	@GET
	@Path("validPhone/{phone}")
	@Produces("application/json;charset=UTF-8")
	public Response validPhone(@PathParam("phone") String phone) {
		if (registerDao.validPhone(phone))
			return Response.ok(new ReturnMessage(OS_OK, "手机号码验证成功")).build();
		return Response.ok(new ReturnMessage(OS_ERROR, "手机号码验证失败")).build();
	}

	// 验证号码
	@GET
	@Path("validMsg/{phone}")
	@Produces("application/json;charset=UTF-8")
	public Response validMsg(@PathParam("phone") String phone) {
		Contact contact = registerDao.getContact(phone);
		if (contact != null) {
			if (contact.getReg() == 0) {
				return Response.ok(new ReturnMessage(OS_OK, "手机号码未注册"))// 200
						.build();
			} else {
				return Response.ok(new ReturnMessage(OS_NOT, "手机号码已注册"))// 401
						.build();
			}
		} else {
			return Response.ok(new ReturnMessage(OS_ERROR, "手机号码未通过系统认证"))
					.build();// 403.1
		}
	}

	// 验证码
	@GET
	@Path("validNumber/{phone}")
	@Produces("application/json;charset=UTF-8")
	public Response validNumber(@PathParam("phone") String phone) {
		if (registerDao.validPhone(phone)) {
			int number = (int) (Math.random() * (9999 - 1000 + 1)) + 1000;
			if (registerDao.singleSend(phone, number + "", Const.APIKEY, phone,
					"【云上果洛】您的验证码是" + number + "。如非本人操作，请忽略本短信")) {
				return Response.ok(new NumberMessage(number + "", "1")).build();
			} else
				return Response.ok(new NumberMessage()).build();
		} else
			return Response.ok(new NumberMessage()).build();
	}

	// 注册
	@GET
	@Path("registerUser/{phone}/{pwd}/{yzm}")
	@Produces("application/json;charset=UTF-8")
	public UserBean registerUser(@PathParam("phone") String phone,
			@PathParam("pwd") String pwd, @PathParam("yzm") String yzm) {

		User users = registerDao.saveUser(phone, pwd, yzm);
		if (users != null) {
			UserBean c = new UserBean();
			c = SysBeanTrans.toBean(users);
			Contact contact = contactDao.getContact(c.getPhone());
			if (contact != null)
				c.setImgUrl(fileDao.getFileUrl(fileDao.getFileLink(
						contact.getId(), "TX") == null ? "" : fileDao
						.getFileLink(contact.getId(), "TX").getDocument()
						.getId()));
			else
				c.setImgUrl("");
			return c;
		} else
			return new UserBean();
	}

	// 修改手机号码
	@GET
	@Path("updataPhone/{oldPhone}/{newPhone}/{yzm}")
	@Produces("application/json;charset=UTF-8")
	public Response updataPhone(@PathParam("oldPhone") String oldPhone,
			@PathParam("newPhone") String newPhone, @PathParam("yzm") String yzm) {
		if (registerDao.upDataPhone(oldPhone, newPhone, yzm) != null)
			return Response.ok(new ReturnMessage(OS_OK, "手机号码修改成功")).build();
		return Response.ok(new ReturnMessage(OS_ERROR, "手机号码修改失败")).build();
	}

	// 修改密码
	@GET
	@Path("updataPwd/{phone}/{oldPwd}/{newPwd}")
	@Produces("application/json;charset=UTF-8")
	public Response updataPwd(@PathParam("phone") String phone,
			@PathParam("oldPwd") String oldPwd,
			@PathParam("newPwd") String newPwd) {

		if (registerDao.upPwd(phone, oldPwd, newPwd))
			return Response.ok(new ReturnMessage(OS_OK, "密码修改成功")).build();
		return Response.ok(new ReturnMessage(OS_ERROR, "密码修改失败")).build();
	}

	// 修改邮箱
	@GET
	@Path("updataEmail/{phone}/{email}")
	@Produces("application/json;charset=UTF-8")
	public Response updataEmail(@PathParam("phone") String phone,
			@PathParam("email") String email) {

		if (registerDao.upEmail(phone, email))
			return Response.ok(new ReturnMessage(OS_OK, "邮箱修改成功")).build();
		return Response.ok(new ReturnMessage(OS_ERROR, "邮箱修改失败")).build();
	}

}
