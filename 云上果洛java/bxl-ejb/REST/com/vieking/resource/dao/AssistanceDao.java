package com.vieking.resource.dao;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.basicdata.model.Dictionary;
import com.vieking.functions.model.Comment;
import com.vieking.functions.model.Contact;
import com.vieking.functions.model.UserNotice;
import com.vieking.role.model.Assistance;
import com.vieking.role.model.User;
import com.vieking.sys.base.BaseDaoHibernate;
import com.vieking.sys.enumerable.RegistrationState;
import com.vieking.sys.util.StringUtil;

@Name("assistanceDao")
@AutoCreate
public class AssistanceDao extends BaseDaoHibernate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2767214125082884844L;

	@SuppressWarnings("unchecked")
	public List<Assistance> getAssistances(String xwlx, int pageIndex,
			int pageSize) {
		if (xwlx.startsWith("XWLX900") || xwlx.equals("XWLX06") || xwlx.equals("XWLX08")) {
			Query query = entityManager
					.createQuery(
							" from Assistance o where  o.bzlx.code =:xwlx and o.state = '正常' order by o.syncSign desc,o.ct desc")
					.setParameter("xwlx", xwlx);
			query.setFirstResult(pageIndex - 1);
			query.setMaxResults(pageSize);
			return query.getResultList();
		}else{			
			Query query = entityManager
					.createQuery(
							" from Assistance o where  o.bzlx.code =:xwlx and o.state = '正常' and o.regState = '已审核' order by o.syncSign desc,o.ct desc")
					.setParameter("xwlx", xwlx);
			query.setFirstResult(pageIndex - 1);
			query.setMaxResults(pageSize);
			return query.getResultList();
		}
//		Query query = entityManager
//				.createQuery(
//						" from Assistance o where  o.bzlx.code =:xwlx and ((o.state = '正常' and o.bzlx.superior.code !='XWL200' ) or (o.state = '正常'  AND o.regState = '已审核' and o.bzlx.superior.code ='XWL200')) order by o.ct desc")
//				.setParameter("xwlx", xwlx);
//		query.setFirstResult(pageIndex - 1);
//		query.setMaxResults(pageSize);
//		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Assistance> getAssistancesByDistrict(String xwlx,
			String district, int pageIndex, int pageSize) {
		Query query = entityManager
				.createQuery(
						" from Assistance o where  o.bzlx.code =:xwlx  and distrs=:distrs and o.state = '正常' and o.regState = '已审核' order by o.syncSign desc,o.ct desc")
				.setParameter("xwlx", xwlx).setParameter("distrs", district);
		query.setFirstResult(pageIndex - 1);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public String getContactid(String phone) {
		List<Contact> list = entityManager
				.createQuery(" from Contact o where o.phone =:phone")
				.setParameter("phone", phone).getResultList();
		if (list.size() > 0) {
			Contact c = list.get(0);
			return c.getId();
		} else {
			return "";
		}
		// Contact c = (Contact) entityManager
		// .createQuery(" from Contact o where o.phone =:phone")
		// .setParameter("phone", phone).getResultList().get(0);
		// return c.getId();
	}

	@SuppressWarnings("unchecked")
	public List<Assistance> newsQuery(String content, int size) {
		Query query = entityManager
				.createQuery(
						" from Assistance o where ( o.biaoti   like concat('%',concat(lower(:content)),'%')    or o.nerong like concat('%',concat(lower(:content2)),'%')  ) and o.bzlx.code not in ('XWLX03','XWLX04','XWLX05','XWLX06') and o.state = '正常' order by o.syncSign desc,o.ct desc")
				.setParameter("content", content)
				.setParameter("content2", StringUtil.encodeString(content));
		query.setFirstResult(0);
		query.setMaxResults(size);
		return query.getResultList();
	}

	public Assistance getAssistance(String id) {
		return (Assistance) entityManager
				.createQuery(" from Assistance o where  o.id =:id  ")
				.setParameter("id", id).getResultList().get(0);
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public int addLlcs(String id) {
		try {
			Assistance assistance = entityManager.find(Assistance.class, id);
			assistance.setLlcs(assistance.getLlcs() + 1);
			entityManager.persist(assistance);
			entityManager.flush();
			return assistance.getLlcs();
		} catch (Exception e) {
			return 0;
		}

	}

	public int getLlcs(String id) {
		try {
			Assistance assistance = entityManager.find(Assistance.class, id);
			return assistance.getLlcs();
		} catch (Exception e) {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<UserNotice> getNotice(String userid, int isReads,
			int pageIndex, int pageSize) {
		Query query;
		String hql = " from UserNotice o where  o.user.id =:userid and o.isreads =:isReads and o.state = '正常'  order by o.syncSign desc,o.ct desc";
		if (isReads > 1) {
			hql = " from UserNotice o where  o.user.id =:userid   order by o.ct desc";
			query = entityManager.createQuery(hql).setParameter("userid",
					userid);
		} else
			query = entityManager.createQuery(hql)
					.setParameter("userid", userid)
					.setParameter("isReads", isReads);

		query.setFirstResult(pageIndex - 1);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public int setReads(String id) {
		try {
			UserNotice userNotice = entityManager.find(UserNotice.class, id);
			userNotice.setIsreads(1);
			entityManager.persist(userNotice);
			entityManager.flush();
			return userNotice.getIsreads();
		} catch (Exception e) {
			return 0;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Comment> plQuery(String newsId) {
		return entityManager
				.createQuery(
						" from Comment o where o.assistance.id =:newsId  and o.pllx.code  ='PLLX01'  order by o.ct desc")
				.setParameter("newsId", newsId).getResultList();
	}

	public boolean getZan(String newsId, String userId) {
		@SuppressWarnings("unchecked")
		List<Comment> comments = entityManager
				.createQuery(
						" from Comment o where o.user.id =:userId and o.assistance.id =:newsId")
				.setParameter("userId", userId).setParameter("newsId", newsId)
				.getResultList();
		if (!comments.isEmpty()) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.REQUIRED)
	public boolean addZan(String newsId, String userId) {
		try {
			List<Comment> comments = entityManager
					.createQuery(
							" from Comment o where o.user.id =:userId and o.assistance.id =:newsId")
					.setParameter("userId", userId)
					.setParameter("newsId", newsId).getResultList();
			if (comments.isEmpty()) {
				Assistance assistance = entityManager.find(Assistance.class,
						newsId);
				User u = entityManager.find(User.class, userId);
				Comment comment = new Comment();
				comment.setUser(u);
				comment.setAssistance(assistance);
				comment.setZan(true);
				comment.setPllx(entityManager.find(Dictionary.class, "PLLX02"));// 点赞
				assistance.setZanCount(assistance.getZanCount() + 1);// 点赞数量
				entityManager.persist(assistance);
				entityManager.persist(comment);
				entityManager.flush();
				return true;
			} else
				return false;

		} catch (Exception e) {
			return false;
		}

	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public boolean addPingLun(String newsId, String userId, String pl) {
		try {
			Assistance assistance = entityManager
					.find(Assistance.class, newsId);
			User u = entityManager.find(User.class, userId);
			Comment comment = new Comment();
			comment.setUser(u);
			comment.setAssistance(assistance);
			comment.setPlnr(pl);
			comment.setPlsj(Calendar.getInstance());
			comment.setPllx(entityManager.find(Dictionary.class, "PLLX01"));// 评论
			entityManager.persist(comment);
			entityManager.flush();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public boolean addBaoLiao(String userId, String bllx,String biaoti, String info) {
		try {
			User u = entityManager.find(User.class, userId);
//			String fjlx = "FJLX02";
			Assistance assistance = new Assistance();
			assistance.setBzlx(entityManager.find(Dictionary.class, bllx));
			assistance.setUser(u);
			assistance.setNamea(u.getName());
			assistance.setNerong(info);
			assistance.setFbsj(Calendar.getInstance());
			assistance.setBiaoti(biaoti);
			// if ("SP".equals(bllx)) {
			// fjlx = "FJLX04";
			// } else if ("YP".equals(bllx)) {
			// fjlx = "FJLX03";
			// } else if ("TP".equals(bllx)) {
			// fjlx = "FJLX02";
			// }
			// assistance.setFjlx(entityManager.find(Dictionary.class, fjlx));
			assistance.setRegState(RegistrationState.待审核);
			entityManager.persist(assistance);
			entityManager.flush();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

}
