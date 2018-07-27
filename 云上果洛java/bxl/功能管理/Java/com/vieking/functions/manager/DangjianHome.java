package com.vieking.functions.manager;

import java.util.Calendar;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.basicdata.model.Dictionary;
import com.vieking.role.model.Assistance;
import com.vieking.sys.enumerable.RegistrationState;
import com.vieking.sys.util.StringUtil;


@Name("fun.dangjianHome")
@AutoCreate
public class DangjianHome  extends BaseHome<Assistance>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7209111185852469290L;

	public boolean isWired() {
		return true;
	}

	/***
	 * 页面初始化方法
	 */
	public void wire() {
		if (isIdDefined()) {
			getInstance();
			String text = StringUtil.decodeString(getInstance().getNerong());
			getInstance().setNerong(text);
		} else {
			clearInstance();
			getInstance();
		}
		getInstance().setBzlx(entityManager.find(Dictionary.class, "XWLX15"));
		getInstance().setFjlx(entityManager.find(Dictionary.class, "FJLX01"));
	}

	public boolean validation() {
		boolean result = true;

		return result;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String persist() {
		if (!validation()) {
			return null;
		}
		Assistance o = getInstance();
		String text = StringUtil.encodeString(o.getNerong());
		o.setNerong(text);
		o.setUser(currUser);
		o.setNamea(currUser.getName());
		o.setFbsj(Calendar.getInstance());
		entityManager.persist(o);
		entityManager.flush();
		lastStateChange = super.persist();
		return lastStateChange;
	}

	@Override
	@Transactional(TransactionPropagationType.REQUIRED)
	public String update() {
		if (!validation()) {
			return null;
		}
		Assistance o = getInstance();
		String text = StringUtil.encodeString(o.getNerong());
		o.setNerong(text);
//		o.setExamine(Calendar.getInstance());
//		o.setRegState(RegistrationState.已审核);		
		entityManager.persist(o);
		entityManager.flush(); 
		lastStateChange = super.update();
		return lastStateChange;
	}
	
	@Transactional(TransactionPropagationType.REQUIRED)
	public void pass() {		
		Assistance o = getInstance();
		String text = StringUtil.encodeString(o.getNerong());
		o.setNerong(text);
		o.setRegState(RegistrationState.已审核);
		o.setExamine(Calendar.getInstance());
		o.setExamineUser(currUser.getName());
		entityManager.persist(o);
		entityManager.flush();
		redirect.setViewId("/fun/dangjian/main.xhtml");
		redirect.execute();
	}

	@Override
	public String appName() {
		return "党建";
	}

	@Override
	public String instanceInfo() {
		return "党建";
	}
}
