package com.vieking.sys.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.vieking.file.model.AppDocType;
import com.vieking.file.model.DocType;
import com.vieking.role.model.Application;
import com.vieking.sys.exception.DaoException;

@Name("app.applicationManger")
@Scope(ScopeType.APPLICATION)
@Startup(depends = "app.config")
public class ApplicationManger implements Serializable {

	private static final long serialVersionUID = 5504980973800632206L;

	@Logger
	protected Log log;

	@In
	protected FacesMessages facesMessages;

	private Map<String, Application> apps = new HashMap<String, Application>();

	@In
	protected EntityManager entityManager;

	@SuppressWarnings("deprecation")
	public void reload(String name) throws DaoException {
		log.debug("reload---> {0}", name);
		if (StringUtils.isEmpty(name)) {

			facesMessages.add(FacesMessage.SEVERITY_ERROR, "应用名称为空字符！");
			throw new DaoException("应用名称为空字符！");
		}
		name = name.toLowerCase();
		Application app = entityManager.find(Application.class, name);
		if (apps.containsKey(name)) {
			if (app == null) {
				apps.remove(name);
			} else {
				Application _app = apps.get(name);
				load(app, _app);
			}
		} else {
			if (app != null) {
				apps.put(app.getName().toLowerCase(), load(app, null));
			}
		}
	}

	public Application load(Application e_app, Application app) {
		// public Application(String name, String des, String path, String
		// smbcn, String ebcn,
		// String keyProp, boolean docEnabled, String orderby, int menuPos,
		// String restrictions,
		// String currView)
		if (app == null) {
			app = new Application(e_app.getName(), e_app.getDes(),
					e_app.getPath(), e_app.getSmbcn(), e_app.getEbcn(),
					e_app.getKeyProp(), e_app.isDocEnabled(),
					e_app.getOrderby(), e_app.getMenuPos(),
					e_app.getRestrictions(), e_app.getCurrView());

		} else {
			app.setPropertis(e_app.getName(), e_app.getDes(), e_app.getPath(),
					e_app.getSmbcn(), e_app.getEbcn(), e_app.getKeyProp(),
					e_app.isDocEnabled(), e_app.getOrderby(),
					e_app.getMenuPos(), e_app.getRestrictions(),
					e_app.getCurrView());
		}
		app.setIco(e_app.getIco());
		app.setIcob(e_app.getIcob());
		loadAppDocTypes(app, e_app);
		return app;
	}

	public void loadAppDocTypes(Application app, Application e_app) {
		app.getDocTypes().clear();
		for (Iterator<AppDocType> iterator = e_app.getDocTypes().iterator(); iterator
				.hasNext();) {
			AppDocType e_adt = iterator.next();
			// public DocType(String name, String des, String path, String
			// fileType, int maxLength, String ico)
			DocType e_dt = e_adt.getDocType();
			DocType dt = new DocType(e_dt.getName(), e_dt.getDes(),
					e_dt.getPath(), e_dt.getFileType(), e_dt.getMaxLength(),
					e_dt.getIco());
			dt.setVersion(e_dt.getVersion());
			// public AppDocType(DocType docType, Application app, DocLinkCount
			// linkCount)
			AppDocType adt = new AppDocType(dt, app, e_adt.getLinkCount());
			// AppDocType adt = new AppDocType(qd, e_qpd.getName(),
			app.getDocTypes().add(adt);
		}
	}

	@SuppressWarnings("unchecked")
	public void loadAll() {
		List<Application> ls = entityManager.createQuery(
				"from Application o order by o.name ").getResultList();
		for (Iterator<Application> iterator = ls.iterator(); iterator.hasNext();) {
			Application app = iterator.next();
			apps.put(app.getName().toLowerCase(), load(app, null));
		}
		entityManager.clear();
	}

	@Observer(create = false, value = { "event.chg.com.vieking.sys.model.Application" })
	public void onChanged(String name) throws DaoException {
		reload(name);
	}

	@SuppressWarnings("unchecked")
	@Observer(create = false, value = { "event.chg.com.vieking.sys.model.DocType" })
	public void onDocTypeChanged(String name) throws DaoException {
		List<String> appNames = entityManager
				.createQuery(
						"select adt.app.name  from AppDocType adt where adt.docType.name=:name")
				.setParameter("name", name).getResultList();
		for (Iterator<String> iterator = appNames.iterator(); iterator
				.hasNext();) {
			String appName = iterator.next();
			reload(appName);
		}
		appNames = null;
	}

	/***
	 * 
	 * @param name
	 *            查询名称
	 * @return 命名查询
	 * @throws DaoException
	 */
	@SuppressWarnings("deprecation")
	public Application application(String name) throws DaoException {
		name = name.toLowerCase().trim();
		Application result = apps.get(name);
		if (result == null) {
			reload(name);
			result = apps.get(name);
		}
		if (result == null) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, "应用【" + name
					+ "】定义未找到！");
			throw new DaoException("应用【" + name + "】定义未找到！");
		} else {
			return result;
		}
	}

	@Create
	public void init() {
		apps.clear();
		loadAll();

	}

}
