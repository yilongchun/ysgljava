package com.vieking.role.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;

import com.vieking.file.model.AppDocType;
import com.vieking.file.model.DocType;
import com.vieking.role.model.Application;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;
import com.vieking.sys.base.IAppCache;
import com.vieking.sys.base.IExpXml;
import com.vieking.sys.base.IImpXml;
import com.vieking.sys.util.StringUtil;

@Name("sys.applicationQuery")
@Scope(ScopeType.PAGE)
public class ApplicationQuery extends BaseNqtQuery<Application> implements
		IExpXml<Application>, IImpXml<Application>, IAppCache<Application> {

	private static final long serialVersionUID = 7143583542160447428L;

	@Override
	public void create() {
		super.create();
		setXmlExpPath("D:/ApplicationExp");
		setXmlImpPath("D:/ApplicationImp");
	}

	public void importObj(Application umObj) {
		if (umObj == null || StringUtils.isEmpty(umObj.getName())) {
			facesMessages.add("提示", "导入应用定义对象为空或名称为空！");
			return;
		}
		Application app = entityManager
				.find(Application.class, umObj.getName());
		List<AppDocType> tmpLs = new ArrayList<AppDocType>();
		if (umObj.getDocTypes() != null) {
			tmpLs.addAll(umObj.getDocTypes());
			umObj.getDocTypes().clear();
		}
		if (app == null) {
			entityManager.persist(umObj);
		}
		// 检查并保存文档类型定义
		for (Iterator<AppDocType> iterator = tmpLs.iterator(); iterator
				.hasNext();) {
			AppDocType umAdt = iterator.next();
			DocType dt = entityManager.find(DocType.class, umAdt.getDocType()
					.getName());
			if (dt == null) {
				entityManager.persist(umAdt.getDocType());
			} else {
				umAdt.setDocType(dt);
			}
		}
		// 如果应用未保存过，直接保存应用文档关联
		if (app == null) {
			for (Iterator<AppDocType> iterator = tmpLs.iterator(); iterator
					.hasNext();) {
				AppDocType umAdt = iterator.next();
				umAdt.setApp(umObj);
				entityManager.persist(umAdt);
			}
		} else {
			// 检查并保存应用文档关联
			for (Iterator<AppDocType> iterator = tmpLs.iterator(); iterator
					.hasNext();) {
				AppDocType adt = iterator.next();
				// 检查并保存应用文档关联
				boolean isSave = false;
				for (Iterator<AppDocType> iter = app.getDocTypes().iterator(); iter
						.hasNext();) {

					AppDocType adt2 = iter.next();
					if (adt.getDocType().getName()
							.equals(adt2.getDocType().getName())) {
						isSave = true;
						break;
					}

				}
				if (!isSave) {
					adt.setApp(app);
					entityManager.persist(adt);
				}
			}
		}
		entityManager.flush();
	}

	public void importedProcess(Application umObj) {
		getQhb().parm("name", "DT003").setSv(umObj.getName());
		applyFilter();
	}

	public void processRequestParams() {

	}

	@Override
	public void executeQueryCounts() {
	}

	public String getQueryDesc() {
		return "功能管理";
	}

	public ApplicationQuery() {
		setOrder("o.name asc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.sys.application";
	}

	@Override
	public String nameQueryName() {
		return "sys.application";
	}

	public String expPath() {
		return getXmlExpPath();
	}

	public String impPath() {
		return getXmlImpPath();
	}

	@SuppressWarnings("rawtypes")
	public Class[] classToBeBound() {
		return new Class[] { Application.class, AppDocType.class, DocType.class };
	}

	public String objInfo(Application obj) {

		String expName = obj.getName() + "_"
				+ (obj.getDes() == null ? "#" : obj.getDes()) + "_"
				+ StringUtil.toStr(obj.mt());
		expName.replaceAll("/", "_");
		return expName;
	}

	public void raiseImportEvent(Application obj) {
		Events.instance().raiseEvent("event.chg.com.vieking.sys.model.Application",
				obj.getName());
	}

	public void raiseRefreshEvent(Application obj) {
		Events.instance().raiseEvent("event.chg.com.vieking.sys.model.Application",
				obj.getName());
	}

}
