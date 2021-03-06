package com.vieking.functions.manager;

import java.util.Calendar;
import java.util.List;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.basicdata.model.Dictionary;
import com.vieking.role.model.Assistance;
import com.vieking.sys.util.StringUtil;

/**
 * 新闻管理 <br>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("fun.toutiaoHome")
@AutoCreate
public class ToutiaoHome extends BaseHome<Assistance> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9203588795518570108L;

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
		getInstance().setBzlx(entityManager.find(Dictionary.class, "XWLX01"));
	}

	@SuppressWarnings("unchecked")
	public boolean validation() {
		boolean result = true;
		
		if (getInstance().getId() == null) {
			List<Assistance> list = entityManager
					.createQuery(" from Assistance o where o.biaoti =:biaoti ")
					.setParameter("biaoti", getInstance().getBiaoti())
					.getResultList();

			if (!list.isEmpty()) {
				facesMessages.add(getInstance().getBiaoti() + ",标题已存在！");
				result = false;
			}
		}
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

		entityManager.persist(o);
		entityManager.flush();
		o.setUser(currUser);
		o.setNamea(currUser.getName());
		lastStateChange = super.update();
		return lastStateChange;
	}

	@Override
	public String appName() {
		return "头条";
	}

	@Override
	public String instanceInfo() {
		return "头条";
	}

}
