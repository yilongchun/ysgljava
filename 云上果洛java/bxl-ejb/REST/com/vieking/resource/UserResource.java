package com.vieking.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import com.vieking.file.model.DocInfo;
import com.vieking.file.model.DocLink;
import com.vieking.functions.model.Contact;
import com.vieking.resource.adapter.SysBeanTrans;
import com.vieking.resource.bean.ItemValueBean;
import com.vieking.resource.bean.UserBean;
import com.vieking.resource.dao.ContactDao;
import com.vieking.resource.dao.FileDao;
import com.vieking.resource.dao.LoginDao;
import com.vieking.role.model.User;
import com.vieking.sys.exception.KeException;
import com.vieking.sys.exception.ReConst;
import com.vieking.sys.util.Config;

@Path("user")
@Name("userResource")
public class UserResource implements ReConst {

	@In
	private LoginDao loginDao;
	@In
	private ContactDao contactDao;
	@In(value = "fileDao")
	private FileDao fileDao;
	@In(value = "app.config")
	private Config config;

	/** 签到 */
	@GET
	@Path("sign/{userid}")
	@Produces("application/json;charset=UTF-8")
	public Response sign(@PathParam("userid") String userid) {
		if (!loginDao.setSign(userid))
			return Response.ok(new ItemValueBean(OS_ERROR, 0)).build();
		else
			return Response.ok(new ItemValueBean(OS_OK, 1)).build();
	}
	
	/** 是否签到 */
	@GET
	@Path("isSign/{userid}")
	@Produces("application/json;charset=UTF-8")
	public Response isSign(@PathParam("userid") String userid) {
		if (!loginDao.isSign(userid))
			return Response.ok(new ItemValueBean(OS_OK, 0)).build();
		else
			return Response.ok(new ItemValueBean(OS_OK, 1)).build();
	}
	
	/**
	 * 获取积分排名
	 * @return
	 */
	@GET
	@Path("getScoreRanking")
	@Produces("application/json;charset=UTF-8")
	public List<UserBean> getScoreRanking(){
		List<UserBean> userBeanList = new ArrayList<UserBean>();
		List<User> list = loginDao.getScoreRanking();
		for (int i = 0; i < list.size(); i++) {
			User user = list.get(i);
			UserBean c = SysBeanTrans.toBean(user);
			Contact contact = contactDao.getContact(c.getPhone());
			if (contact != null) {
				DocLink link = fileDao.getFileLink(contact.getId(), "TX");
				if (link == null) {
					c.setImgUrl("");
				} else {
					DocInfo info = link.getDocument();

					String webUrl = "";
					try {
						webUrl = config.get("webUrl");
					} catch (KeException e) {
						e.printStackTrace();
					}
					String path = webUrl + info.getUrlName() + "/"
							+ info.getOriginalName();
					c.setImgUrl(path);
				}
			}
			userBeanList.add(c);
		}
		return userBeanList;
	}
	
}
