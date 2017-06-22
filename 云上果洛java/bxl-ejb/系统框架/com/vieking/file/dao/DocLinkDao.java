package com.vieking.file.dao;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.file.model.DocLink;
import com.vieking.file.model.DocType;
import com.vieking.role.model.Application;
import com.vieking.sys.base.AppContext;
import com.vieking.sys.base.BaseDaoHibernate;
import com.vieking.sys.bean.QueryParamBean;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.util.StringUtil;

@Name("sys.dao.docLink")
@AutoCreate
@Scope(ScopeType.EVENT)
public class DocLinkDao extends BaseDaoHibernate {

	private static final long serialVersionUID = -1167239862887392603L;

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

	@SuppressWarnings("unchecked")
	public List<DocLink> nqLs(String ebcn, String keyProp, String keyValue,
			List<String> dtIds) throws DaoException {
		keyValue = StringUtils.trim(keyValue);
		ebcn = StringUtils.trim(ebcn);
		keyProp = StringUtils.trim(keyProp);
		List<DocLink> dls = query("sys.dao.doclink.list", "",
				new QueryParamBean("ebcn", ebcn), new QueryParamBean(
						"keyValue", keyValue), new QueryParamBean("keyProp",
						keyProp), new QueryParamBean("dtIds", dtIds));
		return dls;
	}

	public List<DocLink> nqLs(String ebcn, String keyProp, String keyValue,
			String strDtIds) throws DaoException {
		List<String> dtIds = StringUtil.toList(strDtIds, ",");
		return nqLs(ebcn, keyProp, keyValue, dtIds);
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

	public List<DocLink> list(Application app, String keyValue) {
		return list(app.getEbcn(), app.getKeyProp(), keyValue);
	}

	public List<DocLink> list(AppContext ac, String keyValue) {
		return list(ac.getApp(), keyValue);
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public void remove(DocLink docLink) throws DaoException {
		try {
			entityManager.remove(docLink);
			entityManager.flush();
		} catch (Exception e) {

			throw new DaoException("文档链接删除失败！");

		}
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public void addLinks(List<DocLink> docLinks) throws DaoException {
		try {
			for (Iterator<DocLink> iterator = docLinks.iterator(); iterator
					.hasNext();) {
				DocLink docLink = iterator.next();
				if (docLink.getDocument().getVersion() != null) {

					entityManager.persist(docLink.getDocument());
				}
				entityManager.persist(docLink);
			}
			entityManager.flush();
		} catch (Exception e) {

			throw new DaoException("文档链接保存失败！");

		}

	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public void addLinks(DocLink docLink) throws DaoException {
		try {
			if (docLink.getDocument().getVersion() != null) {
				entityManager.persist(docLink.getDocument());
			}
			entityManager.persist(docLink);
			entityManager.flush();
		} catch (Exception e) {
			throw new DaoException("文档链接保存失败！");
		}
	}
}
