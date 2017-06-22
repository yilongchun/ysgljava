package com.vieking.sys.doc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.PersistenceController;

import com.vieking.file.dao.DocLinkDao;
import com.vieking.file.enumerable.DocLinkCount;
import com.vieking.file.model.AppDocType;
import com.vieking.file.model.DocInfo;
import com.vieking.file.model.DocLink;
import com.vieking.file.model.DocType;
import com.vieking.role.model.Application;
import com.vieking.role.model.User;
import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.service.ApplicationManger;

public class DocLinkUiAppNameCompent extends
		PersistenceController<EntityManager> implements Serializable {

	@In
	protected FacesMessages facesMessages;

	private static final long serialVersionUID = -2052740569323891822L;

	User user;

	private DocLinkDao docLinkDao;

	private Application app;

	private ApplicationManger am;

	private AppDocType appDocType;

	private String entId;

	private String reRender;

	private String view;

	private List<DocLink> docLinks;

	private Map<String, List<DocLink>> docLinkMap = new HashMap<String, List<DocLink>>();

	private int maxFilesQuantity = 10;

	private String title;

	private boolean initok = false;

	public void initMap() {
		docLinkMap.clear();
		for (Iterator<DocLink> iterator = docLinks.iterator(); iterator
				.hasNext();) {
			DocLink docLink = (DocLink) iterator.next();
			if (docLinkMap.get(docLink.getDocument().getDocType().getName()) == null) {
				List<DocLink> dls = new ArrayList<DocLink>();
				dls.add(docLink);
				docLinkMap.put(docLink.getDocument().getDocType().getName(),
						dls);
			} else {
				docLinkMap.get(docLink.getDocument().getDocType().getName())
						.add(docLink);
			}
		}
	}

	public List<DocLink> docLinksForDt(DocType dt) {
		return docLinkMap.get(dt.getName());
	}

	@Observer(value = Const.清理文档链接界面数据, create = false)
	public void clear() {
		getLog().debug("clear");
		reset();
		docLinks = null;
		docLinkMap.clear();
	}

	@Observer(value = Const.添加已上传文件文档链接V2, create = false)
	public void addLinks(String _entId, List<DocInfo> seleDocInfos) {
		getLog().debug("addLinks{0}", entId);
		if (getEntId() == null || !getEntId().equals(_entId))
			return;
		List<DocLink> dls = new ArrayList<DocLink>();
		for (Iterator<DocInfo> iterator = seleDocInfos.iterator(); iterator
				.hasNext();) {
			DocInfo docInfo = (DocInfo) iterator.next();
			dls.add(new DocLink(docInfo, app.getEbcn(), app.getKeyProp(),
					getEntId()));
		}
		try {
			getDocLinkDao().addLinks(dls);
			docLinks = getDocLinkDao().list(app, entId);
			for (DocLink dl : dls) {
				seleDocInfos.remove(dl);
			}
			initMap();
		} catch (DaoException e) {
			facesMessages.add(e.getInfo(), e.getInfo());
		}
	}

	@Observer(value = Const.文档上传完毕V2, create = false)
	public void addLinks(FileWrapper fileWrapper) {
		getLog().debug("addLinks {0},{1}", fileWrapper.getFwId(), getEntId());
		if (!fileWrapper.getFwId().equals(getEntId())) {
			return;
		}
		List<DocLink> dls = new ArrayList<DocLink>();
		for (Iterator<DocInfo> iterator = fileWrapper.getDocs().iterator(); iterator
				.hasNext();) {
			DocInfo docInfo = (DocInfo) iterator.next();
			dls.add(new DocLink(docInfo, app.getEbcn(), app.getKeyProp(),
					getEntId()));
		}
		try {
			getDocLinkDao().addLinks(dls);
			docLinks = getDocLinkDao().list(app, entId);
			for (DocLink dl : dls) {
				fileWrapper.getDocs().remove(dl);
			}
			initMap();
		} catch (DaoException e) {
			facesMessages.add(e.getInfo(), e.getInfo());
		}
	}

	private void reset() {

		reRender = "";
		initok = false;
		maxFilesQuantity = 10;
		view = "/common/linkdocs/appDocUploadDiagForAppName.xhtml";
	}

	public void initDocLinks(String appName, String entId, String reRender) {

		this.entId = entId;
		this.reRender = reRender;
		try {
			app = getAm().application(appName);
		} catch (DaoException e) {
			addFacesMessage(e.getMessage());
			reset();
			return;
		}
		docLinks = getDocLinkDao().list(app, entId);
		initMap();
	}

	public boolean canAddLink(AppDocType adt) {
		if (DocLinkCount.单个.equals(adt.getLinkCount())) {
			List<DocLink> _dls = docLinksForDt(adt.getDocType());
			if (_dls == null || _dls.isEmpty()) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	public void remove(DocLink docLink) {
		try {
			getDocLinkDao().remove(docLink);
			docLinks = getDocLinkDao().list(app, entId);
			initMap();
		} catch (DaoException e) {
			docLinks.add(docLink);
			facesMessages.add(e.getInfo(), e.getInfo());
		}

	}

	@Observer(value = Const.文档链接发生变化, create = false)
	public void reloadDocLinks() {
		docLinks = getDocLinkDao().list(appDocType.getApp(), entId);
		initMap();
	}

	@Observer(value = Const.文档链接发生变化Ebcn, create = false)
	public void reloadDocLinks(String ebcn, String keyProp, String keyValue) {
		docLinks = getDocLinkDao().list(ebcn, keyProp, keyValue);
		initMap();
	}

	/***
	 * 验证数据是否具备上传条件
	 * 
	 * @param mode
	 *            1:文档上传 2：文档上传并链接文档到应用
	 * @return
	 */
	private boolean verification(int mode) {
		boolean result = true;

		if (mode == 2) {
			if (StringUtils.isEmpty(entId)) {
				facesMessages.add("文档关联ID为空！", "文档关联ID为空！");
				result = false;
			}
			if (appDocType == null) {
				facesMessages.add("应用文档定义为空！", "应用文档定义为空！");
				result = false;
			}
		}
		return result;
	}

	private void setMaxFilesQuantity(AppDocType adt) {
		if (DocLinkCount.单个.equals(adt.getLinkCount())) {
			maxFilesQuantity = 1;
		}
	}

	/**
	 * 此方法仅供系统管理员角色使用，可选择机构进行文件上传
	 * 
	 * @param _appDocType
	 *            应用文档类型
	 * @param _organ
	 *            选择的机构
	 * @param _view
	 *            视图
	 * @param _reRendar
	 *            重绘区域
	 */
	@Restrict("#{s:hasRole('supervisor')}")
	public void showUploadLinkDiag(AppDocType _appDocType, String title,
			String _organId, String _view, String _reRendar) {
		reset();
		appDocType = _appDocType;
		setMaxFilesQuantity(appDocType);
		this.title = title;

		reRender = _reRendar;
		initok = verification(2);
		if (!StringUtils.isEmpty(_view)) {
			view = _view;
		}
		if (initok) {
			Contexts.getPageContext().set("popDiagUrl", view);
		} else {
			Contexts.getPageContext().remove("popDiagUrl");
		}
	}

	/**
	 * 上传文档并建立文档链接
	 * 
	 * @param _appDocType
	 *            应用文档类型
	 * @param _entId
	 *            实体Id
	 * @param _reRender
	 *            重绘区域
	 */
	public void showUploadLinkDiag(AppDocType _appDocType, String title,
			String _entId, String _reRender) {
		FileWrapper fw = (FileWrapper) Contexts.getPageContext().get(
				FileWrapper.class);
		if (fw == null) {
			fw = new FileWrapper();
			fw.setFwId(_entId);
			Contexts.getPageContext().set("sys.upload.fw", fw);
		} else {
			fw.setFwId(_entId);
		}
		reset();
		this.title = title;

		appDocType = _appDocType;
		setMaxFilesQuantity(appDocType);
		reRender = _reRender;
		entId = _entId;
		initok = verification(2);
		if (initok) {
			getPageContext().set("popDiagUrl", view);
		} else {
			getPageContext().remove("popDiagUrl");
		}
	}

	public AppDocType getAppDocType() {
		return appDocType;
	}

	public void setAppDocType(AppDocType appDocType) {
		this.appDocType = appDocType;
	}

	public String getEntId() {
		return entId;
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

	public String getTitle() {
		return title;
	}

	public int getMaxFilesQuantity() {
		return maxFilesQuantity;
	}

	@Destroy
	private void destroy() {
		getLog().debug("destroy{0}", "DocLinkUi");
		clear();
	}

	public Map<String, List<DocLink>> getDocLinkMap() {
		return docLinkMap;
	}

	public Application getApp() {
		return app;
	}

	@Override
	protected String getPersistenceContextName() {
		return "entityManager";
	}

	public User getUser() {
		if (user == null) {
			user = (User) getSessionContext().get(Const.CURRUSER);
		}
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DocLinkDao getDocLinkDao() {
		if (docLinkDao == null) {
			docLinkDao = (DocLinkDao) getComponentInstance("sys.dao.docLink");
		}
		return docLinkDao;
	}

	public void setDocLinkDao(DocLinkDao docLinkDao) {
		this.docLinkDao = docLinkDao;
	}

	public ApplicationManger getAm() {
		if (am == null) {
			am = (ApplicationManger) getApplicationContext().get(
					"app.applicationManger");
		}
		return am;
	}

	public void setAm(ApplicationManger am) {
		this.am = am;
	}
}
