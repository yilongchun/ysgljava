package com.vieking.functions.manager;

import java.util.Calendar;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.role.model.Assistance;
import com.vieking.sys.enumerable.RegistrationState;
import com.vieking.sys.util.StringUtil;

/**
 * 报料管理 <br>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("fun.baoLiaoHome")
@AutoCreate
public class BaoLiaoHome extends BaseHome<Assistance> {

 

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
			getInstance().setExamineUser(currUser.getName());
			//String text = StringUtil.decodeString(getInstance().getNerong());
			//getInstance().setNerong(text);
		} else {
			clearInstance();
			getInstance();
		}
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
		o.setExamine(Calendar.getInstance());
		o.setRegState(RegistrationState.已审核);		
		entityManager.persist(o);
		entityManager.flush(); 
		lastStateChange = super.update();
		return lastStateChange;
	}

	@Override
	public String appName() {
		return "报料管理";
	}

	@Override
	public String instanceInfo() {
		return "报料管理";
	}

}
