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

import com.vieking.resource.adapter.SysBeanTrans;
import com.vieking.resource.bean.CollectionBean;
import com.vieking.resource.bean.ReturnMessage;
import com.vieking.resource.dao.CollectionDao;
import com.vieking.sys.exception.ReConst;

@Path("collection")
@Name("collectionResource")
public class CollectionResource implements ReConst {

	@Logger
	private Log log;

	@In
	private CollectionDao collectionDao;

	// 获取收藏信息
	@GET
	@Path("getCollections/{userId}/{pageIndex}/{pageSize}")
	@Produces("application/json;charset=UTF-8")
	public Collection<CollectionBean> getCollections(
			@PathParam("userId") String userId,
			@PathParam("pageIndex") int pageIndex,
			@PathParam("pageSize") int pageSize) {
		List<CollectionBean> list = new ArrayList<CollectionBean>();
		for (Iterator<com.vieking.functions.model.Collection> iterator = collectionDao
				.getCollections(userId, pageIndex, pageSize).iterator(); iterator
				.hasNext();) {
			com.vieking.functions.model.Collection dac = iterator.next();
			list.add(SysBeanTrans.toBean(dac));
		}
		return list;
	}

	// 添加收藏
	@GET
	@Path("addCollection/{userId}/{newsId}")
	@Produces("application/json;charset=UTF-8")
	public Response addCollection(@PathParam("userId") String userId,
			@PathParam("newsId") String newsId) {
		boolean delOk = collectionDao.addCollection(userId, newsId);
		if (delOk)
			return Response.ok(new ReturnMessage(OS_OK, "收藏添加成功")).build();
		return Response.ok(new ReturnMessage(OS_ERROR, "收藏添加失败")).build();
	}
	
	//查看是否收藏
	@GET
	@Path("checkCollection/{userId}/{newsId}")
	@Produces("application/json;charset=UTF-8")
	public Response checkCollection(@PathParam("userId") String userId,
			@PathParam("newsId") String newsId) {
		com.vieking.functions.model.Collection col = collectionDao.checkCollection(userId, newsId);
		if (col != null)
			return Response.ok(new ReturnMessage(OS_OK, col.getId())).build();
		return Response.ok(new ReturnMessage(OS_ERROR, "")).build();
	}
	
	@GET
	@Path("delCollection/{userId}/{newsId}")
	@Produces("application/json;charset=UTF-8")
	public Response delCollection(@PathParam("userId") String userId,
			@PathParam("newsId") String newsId) {
		com.vieking.functions.model.Collection col = collectionDao.checkCollection(userId, newsId);
		if (col != null){
			boolean delOk = collectionDao.delCollection(col.getId());
			if (delOk)
				return Response.ok(new ReturnMessage(OS_OK, "收藏删除成功")).build();
			return Response.ok(new ReturnMessage(OS_ERROR, "收藏删除失败")).build();
		}else{
			return Response.ok(new ReturnMessage(OS_ERROR, "收藏删除失败")).build();
		}
			
	}
	

	// 删除收藏
	@GET
	@Path("delCollection/{cid}")
	@Produces("application/json;charset=UTF-8")
	public Response delCollection(@PathParam("cid") String cid) {
		boolean delOk = collectionDao.delCollection(cid);
		if (delOk)
			return Response.ok(new ReturnMessage(OS_OK, "收藏删除成功")).build();
		return Response.ok(new ReturnMessage(OS_ERROR, "收藏删除失败")).build();
	}

}
