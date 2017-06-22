package com.vieking.file.dao;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.file.model.DocInfo;
import com.vieking.file.model.DocLink;
import com.vieking.file.model.DocType;
import com.vieking.role.model.Application;
import com.vieking.sys.base.AppContext;
import com.vieking.sys.base.BaseDaoHibernate;
import com.vieking.sys.exception.DaoException;

@Name("sys.dao.docInfo")
@AutoCreate
@Scope(ScopeType.EVENT)
public class DocInfoDao extends BaseDaoHibernate {

	private static final long serialVersionUID = -1167239862887392603L;

	public DocInfo eo(String id) {
		return entityManager.find(DocInfo.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<DocLink> list(String keyValue) {
		return entityManager
				.createQuery(
						"from DocLink o where o.keyValue=:keyValue "
								+ " order by o.document.docType.name")
				.setParameter("keyValue", keyValue).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DocLink> list(String ebcn, String keyProp, String keyValue) {
		return entityManager
				.createQuery(
						"from DocLink o where o.ebcn=:ebcn and o.keyProp=:keyProp and o.keyValue=:keyValue "
								+ " order by o.document.docType.name")
				.setParameter("keyProp", keyProp).setParameter("ebcn", ebcn)
				.setParameter("keyValue", keyValue).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DocLink> list(String ebcn, String keyProp, String keyValue,
			DocType dt) {
		return entityManager
				.createQuery(
						"from DocLink o where o.ebcn=:ebcn and o.keyProp=:keyProp and o.keyValue=:keyValue "
								+ "  and o.document.docType.name=:docType "
								+ " order by o.document.docType.name")
				.setParameter("keyProp", keyProp).setParameter("ebcn", ebcn)
				.setParameter("keyValue", keyValue)
				.setParameter("docType", dt.getName()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DocLink> list(String ebcn, String keyProp, String keyValue,
			String dtName) {
		return entityManager
				.createQuery(
						"from DocLink o where o.ebcn=:ebcn and o.keyProp=:keyProp and o.keyValue=:keyValue "
								+ "  and o.document.docType.name=:docType "
								+ " order by o.document.docType.name")
				.setParameter("keyProp", keyProp).setParameter("ebcn", ebcn)
				.setParameter("keyValue", keyValue)
				.setParameter("docType", dtName).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DocLink> list(String ebcn, String keyProp, String keyValue,
			List<String> strList) {
		return entityManager
				.createQuery(
						"from DocLink o where o.ebcn=:ebcn and o.keyProp=:keyProp and o.keyValue=:keyValue "
								+ "  and o.document.docType.name in (:str) "
								+ " order by o.document.docType.name")
				.setParameter("keyProp", keyProp).setParameter("ebcn", ebcn)
				.setParameter("keyValue", keyValue)
				.setParameter("str", strList).getResultList();
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public void add(DocLink docLink) throws DaoException {
		try {
			entityManager.persist(docLink.getDocument());
			entityManager.persist(docLink);
			entityManager.flush();
		} catch (Exception e) {

			throw new DaoException("文档链接保存失败！");

		}
	}

	@Transactional
	public void save(DocInfo di) throws DaoException {
		try {
			entityManager.persist(di);
			entityManager.flush();
		} catch (Exception e) {

			throw new DaoException(e.getMessage());

		}
	}

	public List<DocLink> list(Application app, String keyValue) {
		return list(app.getEbcn(), app.getKeyProp(), keyValue);
	}

	public List<DocLink> list(AppContext ac, String keyValue) {
		return list(ac.getApp(), keyValue);
	}

}
