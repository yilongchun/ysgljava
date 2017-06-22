package com.vieking.resource.dao;

import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.role.model.Assistance;
import com.vieking.sys.base.BaseDaoHibernate;
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
		Query query = entityManager
				.createQuery(
						" from Assistance o where  o.bzlx.code =:xwlx  order by o.ct desc")
				.setParameter("xwlx", xwlx);
		query.setFirstResult(pageIndex - 1);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Assistance> newsQuery(String content, int size) {
		Query query = entityManager
				.createQuery(
						" from Assistance o where ( o.biaoti   like concat('%',concat(lower(:content)),'%')    or o.nerong like concat('%',concat(lower(:content2)),'%')  ) and o.bzlx.code not in ('XWLX03','XWLX04','XWLX05','XWLX06') order by o.ct desc")
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

}
