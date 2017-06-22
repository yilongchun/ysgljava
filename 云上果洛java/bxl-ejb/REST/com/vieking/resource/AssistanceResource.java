package com.vieking.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.vieking.basicdata.dao.DictionaryDao;
import com.vieking.basicdata.model.Dictionary;
import com.vieking.file.model.DocLink;
import com.vieking.resource.adapter.SysBeanTrans;
import com.vieking.resource.bean.AssistanceBean;
import com.vieking.resource.bean.ItemValueBean;
import com.vieking.resource.bean.ReturnMessage;
import com.vieking.resource.bean.XinWenLmBean;
import com.vieking.resource.dao.AssistanceDao;
import com.vieking.resource.dao.FileDao;
import com.vieking.role.model.Assistance;
import com.vieking.sys.exception.KeException;
import com.vieking.sys.exception.ReConst;
import com.vieking.sys.util.Config;

@Path("assistance")
@Name("assistanceResource")
public class AssistanceResource implements ReConst {

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

	// 获取新闻栏目
	@GET
	@Path("getXinWenLM")
	@Produces("application/json;charset=UTF-8")
	public List<XinWenLmBean> getXinWenLM() {
		List<XinWenLmBean> xwlmlist = new ArrayList<XinWenLmBean>();
		try {
			for (Iterator<Dictionary> iterator = dictionaryDao.dictions("XWLX")
					.iterator(); iterator.hasNext();) {
				Dictionary dictionary = iterator.next();
				if (!"XWLX100".equals(dictionary.getCode())) {
					XinWenLmBean xwlm = new XinWenLmBean();
					xwlm.setCode(dictionary.getCode());
					xwlm.setName(dictionary.getName());
					xwlm.setNumber((int) dictionary.getNumbers());
					xwlm.setMsg(dictionary.getSn().trim());
					List<Dictionary> dlist = dictionaryDao
							.dictionarys(dictionary.getCode());
					List<ItemValueBean> urlList = new ArrayList<ItemValueBean>();
					for (Iterator<Dictionary> iterator2 = dlist.iterator(); iterator2
							.hasNext();) {
						Dictionary dictionary2 = iterator2.next();
						ItemValueBean ivb = new ItemValueBean();
						ivb.setNumber((int) dictionary2.getNumbers());
						ivb.setImgUrl(config.get("webUrl")
								+ dictionary2.getDes());
						urlList.add(ivb);
					}
					xwlm.setUrlList(urlList);
					xwlmlist.add(xwlm);
				}
			}

		} catch (KeException e) {
			// TODO: handle exception
		}

		return xwlmlist;
	}

	// 获取新闻信息
	@GET
	@Path("getAssistances/{xwlx}/{pageIndex}/{pageSize}")
	@Produces("application/json;charset=UTF-8")
	public Collection<AssistanceBean> getAssistances(
			@PathParam("xwlx") String xwlx,
			@PathParam("pageIndex") int pageIndex,
			@PathParam("pageSize") int pageSize) {
		List<AssistanceBean> list = new ArrayList<AssistanceBean>();
		for (Iterator<Assistance> iterator = assistanceDao.getAssistances(xwlx,
				pageIndex, pageSize).iterator(); iterator.hasNext();) {
			Assistance dac = iterator.next();
			AssistanceBean c = new AssistanceBean();
			c = SysBeanTrans.toBean(dac);
			String urls = "";
			for (Iterator<DocLink> iterator2 = fileDao.getFileLinks(c.getId())
					.iterator(); iterator2.hasNext();) {
				DocLink doclink = iterator2.next();
				String fjlx = doclink.getDocument().getDocType().getName();
				String webUrl = "";
				try {
					webUrl =  config.get("webUrl");
				} catch (KeException e) {
					e.printStackTrace();
				}
				if ("SP".equals(fjlx) || "YP".equals(fjlx)){
//					urls = urls
//							+ fileDao.getFileUrl(doclink.getDocument().getId())
//							+ "|" + fjlx + "|"
//							+ doclink.getDocument().getDuration() + ",";
					
					urls =  urls + webUrl + doclink.getDocument().getUrlName() + doclink.getDocument().getOriginalName()+ "|" + fjlx + "|" + doclink.getDocument().getDuration() + ",";
				}else{
//					urls = urls
//							+ fileDao.getFileUrl(doclink.getDocument().getId())
//							+ "|" + fjlx + ",";
					
					urls = urls + webUrl + doclink.getDocument().getUrlName() + doclink.getDocument().getOriginalName()+ "|" + fjlx + ",";
				}
					
			}
			if (urls.endsWith(",")) {
				urls = urls.substring(0, urls.length()-1);
			}
			
			c.setFjlj(urls);
			// c.setXczf(fileDao
			// .getFileUrl(fileDao.getFileLink(c.getId(), "XCZF") == null ? ""
			// : fileDao.getFileLink(c.getId(), "XCZF")
			// .getDocument().getId()));

			list.add(c);
		}
		return list;
	}

	// 获取新闻信息
	@GET
	@Path("getAssistance/{newsId}")
	@Produces("application/json;charset=UTF-8")
	public AssistanceBean getAssistance(@PathParam("newsId") String newsId) {
		AssistanceBean result = new AssistanceBean();
		result = SysBeanTrans.toBean(assistanceDao.getAssistance(newsId));
		String urls = "";
		for (Iterator<DocLink> iterator2 = fileDao.getFileLinks(result.getId())
				.iterator(); iterator2.hasNext();) {
			DocLink doclink = iterator2.next();
			
			
			
			String fjlx = doclink.getDocument().getDocType().getName();
//			urls = urls + fileDao.getFileUrl(doclink.getDocument().getId())
//					+ "|" + fjlx + ",";
			
			urls = urls + doclink.getDocument().getUrlName() + doclink.getDocument().getOriginalName()+ "|" + fjlx + ",";
		}
		if (urls.endsWith(",")) {
			urls = urls.substring(0, urls.length()-1);
		}
		result.setFjlj(urls);
		return result;
	}

	@OPTIONS
	@Path(value = "creatorunion/works")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadWorks(@Context HttpRequest request,
			@Context HttpResponse response) {
		HttpHeaders header = request.getHttpHeaders();
		List<String> requestHeader = header.getRequestHeader("Origin");
		if (CollectionUtils.isNotEmpty(requestHeader)) {
			String host = requestHeader.get(0);
			response.getOutputHeaders().putSingle(
					"Access-Control-Allow-Origin", host);
		}
		response.getOutputHeaders().putSingle("Access-Control-Allow-Headers",
				"X-Requested-With, accept, origin, content-type");
		response.getOutputHeaders().putSingle("Content-Type",
				"application/json;charset=utf-8");
		response.getOutputHeaders().putSingle(
				"Access-Control-Allow-Credentials", true);
		response.getOutputHeaders().putSingle("Access-Control-Allow-Methods",
				"GET,POST,PUT,DELETE,OPTIONS");
		return Response.status(200).entity("").build();
	}

	@GET
	@Path("validPhone/{phone}")
	@Produces("application/json;charset=UTF-8")
	public Response validPhone(@PathParam("phone") String phone) {
		return Response.ok(new ReturnMessage(OS_OK, "验证成功")).build();
	}

	/** 添加浏览次数 */
	@GET
	@Path("addLlcs/{newsId}")
	@Produces("application/json;charset=UTF-8")
	public Response addLlcs(@PathParam("newsId") String newsId) {
		int llcs = assistanceDao.addLlcs(newsId);
		if (llcs == 0)
			return Response.ok(new ItemValueBean(OS_ERROR, 0)).build();
		else
			return Response.ok(new ItemValueBean(OS_OK, llcs)).build();
	}

	/** 获取浏览次数 */
	@GET
	@Path("getLlcs/{newsId}")
	@Produces("application/json;charset=UTF-8")
	public Response getLlcs(@PathParam("newsId") String newsId) {
		return Response.ok(
				new ItemValueBean(OS_OK, assistanceDao.getLlcs(newsId)))
				.build();
	}

	@GET
	@Path("newsQuery/{content}/{size}")
	@Produces("application/json;charset=UTF-8")
	public Collection<AssistanceBean> newsQuery(
			@PathParam("content") String content, @PathParam("size") int size) {
		List<AssistanceBean> list = new ArrayList<AssistanceBean>();
		for (Iterator<Assistance> iterator = assistanceDao.newsQuery(content,
				size).iterator(); iterator.hasNext();) {
			Assistance dac = iterator.next();
			AssistanceBean c = new AssistanceBean();
			c = SysBeanTrans.toBean(dac);
			String urls = "";
			for (Iterator<DocLink> iterator2 = fileDao.getFileLinks(c.getId())
					.iterator(); iterator2.hasNext();) {
				DocLink doclink = iterator2.next();
				String fjlx = doclink.getDocument().getDocType().getName();
				urls = urls + fileDao.getFileUrl(doclink.getDocument().getId())
						+ "|" + fjlx + ",";
			}
			if (urls.endsWith(",")) {
				urls = urls.substring(0, urls.length()-1);
			}
			c.setFjlj(urls);

			list.add(c);
		}
		return list;
	}

}
