package com.vieking.functions.manager;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.lowagie.text.pdf.hyphenation.TernaryTree.Iterator;
import com.vieking.base.BaseHome;
import com.vieking.basicdata.model.Dictionary;
import com.vieking.role.model.Assistance;
import com.vieking.role.model.Role;
import com.vieking.sys.enumerable.RegistrationState;
import com.vieking.sys.util.StringUtil;

/**
 * 新闻管理 <br>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("fun.liuxianHome")
@AutoCreate
public class LiuxianHome extends BaseHome<Assistance> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9203588795518570108L;

	public boolean isWired() {
		return true;
	}
	
	public boolean getRoles2(String userRole) {
		System.out.println(currUser.getRoles());
		
		for (java.util.Iterator iterator = currUser.getRoles().iterator(); iterator
				.hasNext();) {
			Role r = (Role)iterator.next();
			System.out.println(r.getId() + "," + r.getName());
			
			if (r.getId().equals(userRole))
				return true;
			
		}
		return false;
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
		getInstance().setBzlx(entityManager.find(Dictionary.class, "XWLX11"));
		if(identity.hasRole("MAQIN")){
			getInstance().setDistrs("玛沁县");
		}else if(identity.hasRole("GANDE")){
			getInstance().setDistrs("甘德县");
		}else if(identity.hasRole("DARI")){
			getInstance().setDistrs("达日县");
		}else if(identity.hasRole("BANMA")){
			getInstance().setDistrs("班玛县");
		}else if(identity.hasRole("MADUO")){
			getInstance().setDistrs("玛多县");
		}else if(identity.hasRole("JIUZHI")){
			getInstance().setDistrs("久治县");
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
		redirect.setViewId("/fun/liuxian/main.xhtml");
		redirect.execute();
	}

	@Override
	public String appName() {
		return "六县";
	}

	@Override
	public String instanceInfo() {
		return "六县";
	}

}
