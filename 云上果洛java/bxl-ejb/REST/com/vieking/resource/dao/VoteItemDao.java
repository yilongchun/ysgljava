package com.vieking.resource.dao;

import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.role.model.Assistance;
import com.vieking.role.model.User;
import com.vieking.role.model.VoteItem;
import com.vieking.role.model.VoteResult;
import com.vieking.sys.base.BaseDaoHibernate;

@Name("voteItemDao")
@AutoCreate
public class VoteItemDao extends BaseDaoHibernate{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * 查询当前投票是否已投
	 * @param newsId
	 * @param userId
	 * @return
	 */
	public Boolean checkVoteStatus(String newsId,String userId){
		Query query = entityManager
		.createQuery(
				" from VoteResult o where  o.assistance.id =:newsId and o.user.id = :userId")
		.setParameter("newsId", newsId).setParameter("userId", userId);
		if (query.getResultList().size() > 0) {
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 投票
	 * @param newsId
	 * @param voteItemId
	 * @param userId
	 * @return
	 */
	@Transactional(TransactionPropagationType.REQUIRED)
	public boolean vote(String newsId,String voteItemId,String userId){
		Assistance assistance = entityManager.find(Assistance.class,
				newsId);
		
		VoteItem item = entityManager.find(VoteItem.class, voteItemId);
		
		User user = entityManager.find(User.class, userId);
		
		if (assistance != null && item != null && user != null) {
			VoteResult re = new VoteResult();
			re.setAssistance(assistance);
			re.setUser(user);
			re.setVoteItem(item);
			entityManager.persist(re);
			entityManager.flush();
			
			item.setItemCount(item.getItemCount()+1);
			entityManager.persist(item);
			entityManager.flush();
			
//			assistance = entityManager.find(Assistance.class,
//					newsId);
//			
//			return assistance;
			
			return true;
			
		}else{
			return false;
		}
	}
}
