package com.vieking.base;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.bpm.Actor;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.faces.Redirect;
import org.jboss.seam.log.Log;

import com.vieking.basicdata.dao.NqtDao;
import com.vieking.role.model.User;
import com.vieking.sys.exception.Const;

/**
 * <br>
 * <br>
 * 
 * <p>
 * <a href="BaseHome.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Scope(ScopeType.CONVERSATION)
public abstract class BaseManager {

	@In
	protected EntityManager entityManager;

	@In
	protected FacesMessages facesMessages;

	@In(create = true)
	protected Redirect redirect;

	@Logger
	protected Log log;

	@In(value = Const.CURRUSER, required = false)
	protected User currUser;

	@In
	protected Actor actor;

	protected String lastStateChange;

	protected String method;

	protected String fromCid;

	protected String fromUrl;

	protected Long taskId;

	@In("sys.nqtDao")
	protected NqtDao nqtDao;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	protected User entityUser() {
		if (currUser == null)
			return null;
		return entityManager.find(User.class, currUser.getId());
	}

	public String getLastStateChange() {
		return lastStateChange;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getFromCid() {
		return fromCid;
	}

	public void setFromCid(String fromCid) {
		this.fromCid = fromCid;
	}

	public String getFromUrl() {
		return fromUrl;
	}

	public void setFromUrl(String fromUrl) {
		this.fromUrl = fromUrl;
	}

}
