/*
 * EDAS2 - TetraTech, Inc.
 *
 * Distributable under GPL license.
 * See terms of license at gnu.org.
 */
package com.vieking.sys.utils;

import java.util.Calendar;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.vieking.role.model.User;
import com.vieking.sys.exception.Const;
import com.vieking.sys.util.CalendarUtil;

@Name("srvcontext")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class ServerContext {
	@Logger
	private Log log;
	@In
	private EntityManager entityManager;

	@In(value = Const.CURRUSER)
	protected User currUser;

	public Calendar today() {
		return CalendarUtil.getToday();
	}

	public Calendar todayEt() {
		return CalendarUtil.getEndTime(Calendar.getInstance(), false);
	}
}
