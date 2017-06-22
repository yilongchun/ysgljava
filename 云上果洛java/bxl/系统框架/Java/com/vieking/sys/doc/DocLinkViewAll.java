package com.vieking.sys.doc;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.vieking.file.dao.DocLinkDao;
import com.vieking.file.model.DocLink;
import com.vieking.role.model.User;
import com.vieking.sys.base.EntityResolver;
import com.vieking.sys.exception.Const;

@Name("sys.docLink.all.ui")
@Scope(ScopeType.EVENT)
@AutoCreate
public class DocLinkViewAll implements Serializable {

	private static final long serialVersionUID = 6820580341937493381L;

	@Logger
	protected Log log;

	@In(value = Const.CURRUSER, required = true)
	User user;

	@In
	protected EntityManager entityManager;

	@In
	protected FacesMessages facesMessages;

	@In("entityResolver")
	protected EntityResolver er;

	@In(value = "sys.dao.docLink")
	private DocLinkDao docLinkDao;

	private String reRender;


	@Out(value = "docLinkList", required = false)
	private List<DocLink> docLinks;

	private String mapId;

	private boolean initok = false;



	public void initDocLinks(String ebcn, String keyProp, String keyValue,
			String dtName, String mapId) {
		this.mapId = mapId;
		if (docLinks != null)
			docLinks.clear();
		if (!StringUtils.isEmpty(dtName)) {
			docLinks = docLinkDao.list(ebcn, keyProp, keyValue, dtName);
		} else {
			docLinks = docLinkDao.list(keyValue);
		}
	}

	public void initDocLinks(String keyValue) {
		if (docLinks != null)
			docLinks.clear();
		docLinks = docLinkDao.list(keyValue);

	}

	public void initDocLinks(String ebcn, String keyProp, String keyValue) {
		if (docLinks != null)
			docLinks.clear();
		docLinks = docLinkDao.list(ebcn, keyProp, keyValue);

	}

	public boolean isInitok() {
		return initok;
	}

	public String getReRender() {
		return reRender;
	}

	public List<DocLink> getDocLinks() {
		return docLinks;
	}

	public String getMapId() {
		return mapId;
	}
}
