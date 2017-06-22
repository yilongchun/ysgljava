package com.vieking.resource.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.functions.model.Collection;
import com.vieking.role.model.Assistance;
import com.vieking.role.model.User;
import com.vieking.sys.base.BaseDaoHibernate;

@Name("collectionDao")
@AutoCreate
public class CollectionDao extends BaseDaoHibernate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4894538613757641404L;

	@SuppressWarnings("unchecked")
	public List<Collection> getCollections(String userid, int pageIndex,
			int pageSize) {
		Query query = entityManager
				.createQuery(
						" from Collection o where  o.user.id =:userid  order by o.ct desc")
				.setParameter("userid", userid);
		query.setFirstResult(pageIndex - 1);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.REQUIRED)
	public boolean addCollection(String userId, String newsId) {
		try {
			List<Collection> list = entityManager
					.createQuery(
							" from Collection o where o.user.id=:userId and o.assistance.id =:newsId")
					.setParameter("userId", userId)
					.setParameter("newsId", newsId).getResultList();
			if (list.isEmpty()) {
				User user = entityManager.find(User.class, userId);
				Assistance assistance = entityManager.find(Assistance.class,
						newsId);
				Collection collection = new Collection();
				collection.setUser(user);
				collection.setAssistance(assistance);
				collection.setScsj(Calendar.getInstance());
				entityManager.persist(collection);
				entityManager.flush();
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.REQUIRED)
	public Collection checkCollection(String userId, String newsId) {
		try {
			List<Collection> list = entityManager
					.createQuery(
							" from Collection o where o.user.id=:userId and o.assistance.id =:newsId")
					.setParameter("userId", userId)
					.setParameter("newsId", newsId).getResultList();
			if (list.isEmpty()) {
				return null;
			}
			return list.get(0);
		} catch (Exception e) {
			return null;
		}
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public boolean delCollection(String cid) {
		try {
			
			String[] cids = cid.split(",");
			ArrayList<String> ids = new ArrayList<String>();
			for (int i = 0; i < cids.length; i++) {
				ids.add(cids[i]);
			}
			
			int result = entityManager.createNativeQuery("delete from Collection where id in (:ids)").setParameter("ids",  ids).executeUpdate();
			
			if(result > 0){
				return true;
			}else{
				return false;
			}
			
//			Collection c = entityManager.find(Collection.class, cid);
//			entityManager.remove(c);
//			entityManager.flush();
			
		} catch (Exception e) {
			return false;
		}
	}

}
