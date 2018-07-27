package com.vieking.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.vieking.basicdata.dao.DictionaryDao;
import com.vieking.file.model.DocInfo;
import com.vieking.file.model.DocLink;
import com.vieking.functions.model.UserNotice;
import com.vieking.resource.adapter.SysBeanTrans;
import com.vieking.resource.bean.FileLinkBean;
import com.vieking.resource.bean.ItemValueBean;
import com.vieking.resource.bean.UserNoticeBean;
import com.vieking.resource.dao.AssistanceDao;
import com.vieking.resource.dao.FileDao;
import com.vieking.sys.exception.KeException;
import com.vieking.sys.exception.ReConst;
import com.vieking.sys.util.Config;

@Path("notice")
@Name("userNoticeResource")
public class UserNoticeResource implements ReConst {

	@Logger
	private Log log;

	@In
	private AssistanceDao assistanceDao;

	@In(value = "fileDao")
	private FileDao fileDao;

	@In
	private DictionaryDao dictionaryDao;

	@In(value = "app.config")
	private Config config;

	// 获取通知信息
	@GET
	@Path("getUserNotices/{userid}/{isReads}/{pageIndex}/{pageSize}")
	@Produces("application/json;charset=UTF-8")
	public Collection<UserNoticeBean> getUserNotices(
			@PathParam("userid") String userid,
			@PathParam("isReads") int isReads,
			@PathParam("pageIndex") int pageIndex,
			@PathParam("pageSize") int pageSize) {
		List<UserNoticeBean> list = new ArrayList<UserNoticeBean>();
		for (Iterator<UserNotice> iterator = assistanceDao.getNotice(userid,
				isReads, pageIndex, pageSize).iterator(); iterator.hasNext();) {
			UserNotice dac = iterator.next();
			UserNoticeBean c = new UserNoticeBean();

			c = SysBeanTrans.toBean(dac);
            log.debug("-------------------"+c.getId());   
			for (Iterator<DocLink> iterator2 = fileDao.getFileLinks(dac.getAssistance().getId())
					.iterator(); iterator2.hasNext();) {
				DocLink doclink = iterator2.next();
				DocInfo document = doclink.getDocument();
				String fjlx = document.getDocType().getName();
				String webUrl = "";
				try {
					webUrl = config.get("webUrl");
				} catch (KeException e) {
					e.printStackTrace();
				}

				FileLinkBean flink = new FileLinkBean();
				flink.setFjName(document.getDes());
				flink.setId(doclink.getKeyValue());
				flink.setFjlx(fjlx);
				flink.setFjurl(webUrl + doclink.getDocument().getUrlName()
						+ "/" + doclink.getDocument().getOriginalName());
				c.getFileList().add(flink);
			}

			list.add(c);
		}
		return list;
	}

	/** 设置阅览状态 */
	@GET
	@Path("setReads/{noticeId}")
	@Produces("application/json;charset=UTF-8")
	public Response setReads(@PathParam("noticeId") String noticeId) {
		int result = assistanceDao.setReads(noticeId);
		if (result == 0)
			return Response.ok(new ItemValueBean(OS_ERROR, 0)).build();
		else
			return Response.ok(new ItemValueBean(OS_OK, result)).build();
	}

}
