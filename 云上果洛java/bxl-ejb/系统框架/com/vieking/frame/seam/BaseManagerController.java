package com.vieking.frame.seam;

import org.jboss.seam.Component;

import com.vieking.basicdata.dao.NqtDao;
import com.vieking.sys.service.ApplicationManger;
import com.vieking.sys.service.FileManager;
import com.vieking.sys.service.NameQueryManger;

public abstract class BaseManagerController<E> extends
		BaseEntityManagerController<E> {

	private static final long serialVersionUID = -4751701740699266597L;

	protected FileManager fileManager;

	private ApplicationManger am;
 
	protected NameQueryManger nqm;

	private NqtDao dao;

	protected ApplicationManger getAm() {
		if (am == null) {
			am = (ApplicationManger) Component
					.getInstance("app.applicationManger");
		}
		return am;
	}
 
	protected NqtDao dao() {
		if (dao == null) {
			dao = (NqtDao) Component.getInstance("sys.nqtDao");
		}
		return dao;
	}

	protected FileManager fm() {
		if (fileManager == null) {
			fileManager = (FileManager) Component.getInstance("fileManager");
		}
		return fileManager;
	}

	protected NameQueryManger nqm() {
		if (nqm == null) {
			nqm = (NameQueryManger) Component.getInstance("nqm");
		}
		return nqm;
	}
}
