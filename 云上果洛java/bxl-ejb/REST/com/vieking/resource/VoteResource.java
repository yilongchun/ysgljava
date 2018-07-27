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

import com.vieking.resource.bean.ReturnMessage;
import com.vieking.resource.dao.VoteItemDao;
import com.vieking.role.model.Assistance;
import com.vieking.sys.exception.ReConst;

@Path("vote")
@Name("voteResource")
public class VoteResource implements ReConst {

	@Logger
	private Log log;

	@In
	private VoteItemDao voteItemDao;
	
	/**
	 * 检查是否投票
	 * @return
	 */
	@GET
	@Path("checkVoteStatus/{newsId}/{userId}")
	@Produces("application/json;charset=UTF-8")
	public Response checkVoteStatus(@PathParam("newsId") String newsId,@PathParam("userId") String userId){
		boolean flag = voteItemDao.checkVoteStatus(newsId, userId);
		if (flag)
			return Response.ok(new ReturnMessage(OS_OK, "已参加过投票")).build();
		return Response.ok(new ReturnMessage(OS_NOT, "未参加投票")).build();
	}
	
	/**
	 * 投票
	 * @return
	 */
	@GET
	@Path("vote/{newsId}/{voteItemId}/{userId}")
	@Produces("application/json;charset=UTF-8")
	public Response vote(@PathParam("newsId") String newsId,@PathParam("voteItemId") String voteItemId,@PathParam("userId") String userId){
		
		boolean flag = voteItemDao.vote(newsId, voteItemId, userId);
		if (flag)
			return Response.ok(new ReturnMessage(OS_OK, "投票成功")).build();
		return Response.ok(new ReturnMessage(OS_NOT, "投票失败")).build();
	}

	
}
