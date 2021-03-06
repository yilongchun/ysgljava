package com.vieking.functions.manager;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.role.model.Assistance;
import com.vieking.sys.enumerable.DataState;
import com.vieking.sys.enumerable.RegistrationState;
import com.vieking.sys.util.StringUtil;

/**
 * 新闻管理 <br>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("fun.assistanceHome")
@AutoCreate
public class AssistanceHome extends BaseHome<Assistance> {

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
			if(getInstance() != null && getInstance().getNerong() != null){
				String text = StringUtil.decodeString(getInstance().getNerong());		
				if (isBase64(text)) {
					text = StringUtil.decodeString(text);
				}				
				getInstance().setNerong(text);
			}			
		} else {
			clearInstance();
			getInstance();
			Calendar c = Calendar.getInstance();
			getInstance().setCt(c);
			getInstance().setFbsj(c);
		}
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
		if (isBase64(text)) {
			o.setNerong(text);
		}else{
			StringUtil.encodeString(text);
			o.setNerong(text);
		}
		
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
		if (isBase64(text)) {
			o.setNerong(text);
		}else{
			StringUtil.encodeString(text);
			o.setNerong(text);
		}
		o.setCt(o.getFbsj());

		entityManager.persist(o);
		entityManager.flush();
		o.setUser(currUser);
		o.setNamea(currUser.getName());
		lastStateChange = super.update();
		return lastStateChange;
	}
	
	private static boolean isBase64(String str) {
	    String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
	    return Pattern.matches(base64Pattern, str);
	}
	
	
	@Transactional(TransactionPropagationType.REQUIRED)
	public void pass() {		
		Assistance o = getInstance();
		String text = StringUtil.encodeString(o.getNerong());
		if (isBase64(text)) {
			o.setNerong(text);
		}else{
			StringUtil.encodeString(text);
			o.setNerong(text);
		}
		o.setCt(o.getFbsj());
		o.setRegState(RegistrationState.已审核);
		o.setExamine(Calendar.getInstance());
		o.setExamineUser(currUser.getName());
		entityManager.persist(o);
		entityManager.flush();
		redirect.setViewId("/fun/assistance/main.xhtml");
		redirect.setConversationPropagationEnabled(true);
		redirect.setParameter("drId", null);
		redirect.execute();
	}

	@Override
	public String appName() {
		return "新闻管理";
	}

	@Override
	public String instanceInfo() {
		return "新闻管理";
	}

}
