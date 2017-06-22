package com.vieking.sys.doc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.vieking.file.dao.DocLinkDao;
import com.vieking.file.model.DocLink;
import com.vieking.role.model.User;
import com.vieking.sys.base.EntityResolver;
import com.vieking.sys.exception.Const;

@Name("sys.docLink.view.ui")
@Scope(ScopeType.EVENT)
@AutoCreate
public class DocLinkViewUi implements Serializable {

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

	private List<DocLink> docLinks;

	private String mapId;

	@Out(scope = ScopeType.PAGE, value = "empdocLinks")
	private Map<String, List<DocLink>> docLinkMap = new HashMap<String, List<DocLink>>();

	private boolean initok = false;

	public void initMap() {
		docLinkMap.clear();
		for (Iterator<DocLink> iterator = docLinks.iterator(); iterator
				.hasNext();) {
			DocLink docLink = (DocLink) iterator.next();
			if (docLinkMap.get(docLink.getDocument().getDocType().getDes()) == null) {
				List<DocLink> dls = new ArrayList<DocLink>();
				dls.add(docLink);
				docLinkMap
						.put(docLink.getDocument().getDocType().getDes(), dls);
			} else {
				docLinkMap.get(docLink.getDocument().getDocType().getDes())
						.add(docLink);
			}
		}
	}

	public void initDocLinks(String keyValue, String ebcn, String keyProp,
			String mapId) {
		this.mapId = mapId;
		if (docLinks != null)
			docLinks.clear();
		docLinks = docLinkDao.list(ebcn, keyProp, keyValue);
		initMap();
		Contexts.getPageContext().set(mapId, docLinkMap);
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

	public Map<String, List<DocLink>> getDocLinkMap() {
		return docLinkMap;
	}

	public String getMapId() {
		return mapId;
	}
}
