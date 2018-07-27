package com.vieking.functions.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.basicdata.model.Dictionary;
import com.vieking.functions.model.UserNotice;
import com.vieking.resource.SendCode;
import com.vieking.role.model.Application;
import com.vieking.role.model.Assistance;
import com.vieking.role.model.ModuleApp;
import com.vieking.role.model.User;

/**
 * 通知管理 <br>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@Name("fun.userNoticeHome")
@AutoCreate
public class UserNoticeHome extends BaseHome<Assistance> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 967705309245313124L;

	public boolean isWired() {
		return true;
	}

	/***
	 * 页面初始化方法
	 */
	public void wire() {
		if (isIdDefined()) {
			getInstance();
		} else {
			clearInstance();
			getInstance();
			getInstance().setFbsj(Calendar.getInstance());
			getInstance().setUser(currUser);
			getInstance().setNamea(currUser.getName());
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
		Assistance o = getInstance();
		o.setFbsj(Calendar.getInstance());
		o.setBzlx(entityManager.find(Dictionary.class, "XWLX90001"));
		entityManager.persist(o);
		saveUserNotices();
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
		entityManager.persist(o);
		saveUserNotices();
		entityManager.flush();
		lastStateChange = super.update();
		return lastStateChange;
	}

	@Observer(value = "event.sel.com.vieking.sys.model.User", create = false)
	public void addApplications(String tagTmpId, List<User> sel) {
		if (!getInstance().getTmpId().equals(tagTmpId))
			return;

		for (Iterator<User> iterator = sel.iterator(); iterator.hasNext();) {
			User user = iterator.next();
			User u = entityManager.find(User.class, user.getId());
			getInstance().getUserNotices()
					.add(new UserNotice(u, getInstance()));
		}
	}

	public void remove(String userid) {
		for (Iterator<UserNotice> iterator = getInstance().getUserNotices()
				.iterator(); iterator.hasNext();) {
			UserNotice un = iterator.next();
			if (userid.equals(un.getUser().getId())) {
				getInstance().getUserNotices().remove(un);
				return;
			}
		}
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public void saveUserNotices() {
		
		List<String> phones = new ArrayList<String>();
		List<String> ids = new ArrayList<String>();
		for (Iterator<UserNotice> iterator = getInstance().getUserNotices()
				.iterator(); iterator.hasNext();) {
			UserNotice ma = iterator.next();
			ma.setAssistance(getInstance());
			ma.setTzsj(Calendar.getInstance());
			ma.setIsreads(0);
			entityManager.persist(ma);
			ids.add(ma.getId());
			
		}
		if (ids.isEmpty()) {
			ids.add("xxxxxxxxxxxxxxxxxxxxxxxxxx");
		}
		log.debug("ids--->{0}", ids);
		entityManager
				.createQuery(
						"delete UserNotice o where o.id not in( :ids) and o.assistance.id=:assistanceId")
				.setParameter("ids", ids)
				.setParameter("assistanceId", getInstance().getId())
				.executeUpdate();
		entityManager.flush();
	}
	
	//发短信
	@Transactional(TransactionPropagationType.REQUIRED)
	public void sendNoticeMsg() {
		List<String> mobileList = new ArrayList<String>();
		
		String user = getInstance().getUser().getName();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = sdf.format(getInstance().getFbsj().getTime());
		
		for (Iterator<UserNotice> iterator = getInstance().getUserNotices()
				.iterator(); iterator.hasNext();) {
			UserNotice ma = iterator.next();
			mobileList.add(ma.getUser().getPhone());
		}
		System.out.println("sendNoticeMsg:"+mobileList + "," + user + "," + datetime);
		
		
		int page;
    	if (mobileList.size()%100 == 0) {
			page = mobileList.size()/100;
		}else{
			page = mobileList.size()/100 + 1;
		}
    	
    	boolean flag = true;
    	for (int i = 0; i < page; i++) {
    		int toIndex = (i+1)*100;
    		if (toIndex > mobileList.size()) {
    			toIndex = mobileList.size();
			}
    		List<String> subList = mobileList.subList((i)*100, toIndex);
    		try {
				SendCode.sendNotice(subList, user, datetime);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("sendNoticeMsg:" + e.getLocalizedMessage());
				flag = false;
			}
		}
    	if (flag) {
    		facesMessages.add("通知成功");
		}else{
			facesMessages.add("通知失败");
		}
    	
	}

	@Override
	public String appName() {
		return "通知管理";
	}

	@Override
	public String instanceInfo() {
		return "通知管理";
	}

}
