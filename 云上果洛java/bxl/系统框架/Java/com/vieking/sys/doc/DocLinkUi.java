package com.vieking.sys.doc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.vieking.file.dao.DocLinkDao;
import com.vieking.file.enumerable.DocLinkCount;
import com.vieking.file.model.AppDocType;
import com.vieking.file.model.DocInfo;
import com.vieking.file.model.DocLink;
import com.vieking.file.model.DocType;
import com.vieking.role.model.User;
import com.vieking.sys.base.AppContext;
import com.vieking.sys.base.EntityResolver;
import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.DaoException;

@Name("sys.docLink.ui")
@Scope(ScopeType.PAGE)
@AutoCreate
public class DocLinkUi implements Serializable {

	private static final long serialVersionUID = 6820580341937493381L;

	@Logger
	protected Log log;

	@In(value = "vieking.ui.ac", required = true)
	AppContext appContext;

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
		log.debug("clear");
		reset();
		docLinks = null;
		docLinkMap.clear();
	}

	@Observer(value = Const.添加已上传文件文档链接, create = false)
	public void addLinks(List<DocInfo> seleDocInfos) {
		log.debug("addLinks");
		List<DocLink> dls = new ArrayList<DocLink>();
		for (Iterator<DocInfo> iterator = seleDocInfos.iterator(); iterator
				.hasNext();) {
			DocInfo docInfo = (DocInfo) iterator.next();
			dls.add(new DocLink(docInfo, appContext.getApp().getEbcn(),
					appContext.getApp().getKeyProp(), getEntId()));
		}
		try {
			docLinkDao.addLinks(dls);
			docLinks = docLinkDao.list(appContext.getApp(), entId);
			initMap();
		} catch (DaoException e) {
			facesMessages.addFromResourceBundleOrDefault(e.getInfo(),
					e.getInfo());
		}
	}

	private void reset() {

		reRender = "";
		initok = false;
		maxFilesQuantity = 10;
		view = "/common/linkdocs/appDocUploadDiag.xhtml";
	}

	public void initDocLinks(String entId, String reRender) {
		this.entId = entId;
		this.reRender = reRender;
		docLinks = docLinkDao.list(appContext.getApp(), entId);
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
			docLinkDao.remove(docLink);
			docLinks = docLinkDao.list(appContext.getApp(), entId);
			initMap();
		} catch (DaoException e) {
			docLinks.add(docLink);
			facesMessages.addFromResourceBundleOrDefault(e.getInfo(),
					e.getInfo());
		}

	}

	@Observer(value = Const.文档链接发生变化, create = false)
	public void reloadDocLinks() {
		docLinks = docLinkDao.list(appDocType.getApp(), entId);
		initMap();
	}

	@Observer(value = Const.文档链接发生变化Ebcn, create = false)
	public void reloadDocLinks(String ebcn, String keyProp, String keyValue) {
		docLinks = docLinkDao.list(ebcn, keyProp, keyValue);
		initMap();
	}

	public void initDocLinks(String keyValue, String ebcn, String keyProp) {
		if (docLinks != null)
			docLinks.clear();
		docLinks = docLinkDao.list(ebcn, keyProp, keyValue);
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
				facesMessages.addFromResourceBundleOrDefault("文档关联ID为空！",
						"文档关联ID为空！");
				result = false;
			}
			if (appDocType == null) {
				facesMessages.addFromResourceBundleOrDefault("应用文档定义为空！",
						"应用文档定义为空！");
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
			appContext.setPopUrl(view);
		} else {
			appContext.setPopUrl("");
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
		reset();
		this.title = title;

		appDocType = _appDocType;
		setMaxFilesQuantity(appDocType);
		reRender = _reRender;
		entId = _entId;
		initok = verification(2);
		if (initok) {
			appContext.setPopUrl(view);
		} else {
			appContext.setPopUrl("");
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
		log.debug("destroy{0}", "DocLinkUi");
		clear();
	}

	public Map<String, List<DocLink>> getDocLinkMap() {
		return docLinkMap;
	}
}
