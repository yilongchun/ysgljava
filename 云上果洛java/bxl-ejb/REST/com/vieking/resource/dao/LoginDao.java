package com.vieking.resource.dao;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import net.sf.json.JSONObject;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.resource.SendCode;
import com.vieking.role.model.User;
import com.vieking.role.model.UserGroup;
import com.vieking.sys.base.BaseDaoHibernate;
import com.vieking.sys.util.CalendarUtil;
import com.vieking.sys.util.StringUtil;
import com.yunpian.sdk.model.ResultDO;
import com.yunpian.sdk.model.SendSingleSmsInfo;
import com.yunpian.sdk.service.SmsOperator;
import com.yunpian.sdk.service.YunpianRestClient;

@Name("loginDao")
@AutoCreate
public class LoginDao extends BaseDaoHibernate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1550065528179841319L;

	/** 用户登录 */
	@SuppressWarnings("unchecked")
	public User login(String phone, String pwd) {
		User user = null;
		List<User> list = entityManager
				.createQuery(
						" from User o where (o.phone =:phone  or o.loginName =:name) and o.noLockpwd=:pwd")
				.setParameter("phone", phone).setParameter("name", phone)
				.setParameter("pwd", StringUtil.makePassword(pwd))
				.getResultList();
		if (!list.isEmpty())
			user = (User) list.get(0);
		return user;
	}

	/** 用户 */
	@SuppressWarnings("unchecked")
	public User getUser(String phone) {
		User user = null;
		List<User> list = entityManager
				.createQuery(" from User o where o.phone =:phone ")
				.setParameter("phone", phone).getResultList();
		if (!list.isEmpty())
			user = (User) list.get(0);
		return user;
	}

	/** 用户 */
	@SuppressWarnings("unchecked")
	public User getUserById(String userId) {
		User user = null;
		List<User> list = entityManager
				.createQuery(" from User o where o.id =:id ")
				.setParameter("id", userId).getResultList();
		if (!list.isEmpty())
			user = (User) list.get(0);
		return user;
	}

	/** 用户验证码登录 */
	@SuppressWarnings("unchecked")
	public User validlogin(String phone, String yzm) {
		User user = null;
		List<User> list = entityManager
				.createQuery(
						" from User o where o.phone =:phone and o.pwd=:yzm")
				.setParameter("phone", phone).setParameter("yzm", yzm)
				.getResultList();
		if (!list.isEmpty())
			user = (User) list.get(0);
		return user;
	}

	/** 用户第三方登录 */
	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.REQUIRED)
	public User thirdLogin(String id, String name, String ptname) {
		User user = null;
		List<User> list = entityManager
				.createQuery(
						" from User o where o.name =:name and o.thirdId=:id and o.userType=:ptname")
				.setParameter("name", name).setParameter("id", id)
				.setParameter("ptname", ptname).getResultList();
		if (!list.isEmpty())
			user = (User) list.get(0);
		else {
			user = new User();
			user.setName(name);
			user.setUserType(ptname);
			user.setThirdId(id);
			user.setLoginName(name);
			UserGroup userGroup = entityManager.find(UserGroup.class, "user");
			user.setGroup(userGroup);
			entityManager.persist(user);
			entityManager.flush();
		}
		return user;
	}

	/** 保存用户登录验证码 */
	@Transactional(TransactionPropagationType.REQUIRED)
	public String singleSend(User user,String mobile) {
		
		
		try {
			String res = SendCode.sendMsg(mobile);
			JSONObject obj = JSONObject.fromObject(res);
	        int code = obj.getInt("code");
	        String msg = obj.getString("obj");
	        if (code == 200) {
	        	user.setPwd(msg);
				entityManager.persist(user);
				entityManager.flush();
			}else{
				
			}
	        
	        return res;
		} catch (Exception e) {
			
		}
		
//		YunpianRestClient client = new YunpianRestClient(apikey);
//		SmsOperator smsOperator = client.getSmsOperator();
//		ResultDO<SendSingleSmsInfo> results = smsOperator.singleSend(mobile,
//				text);
//		if (results.isSuccess()) {
//			user.setPwd(yzm);
//			entityManager.persist(user);
//			result = true;
//		}
		
		return null;
	}

	public boolean sendvalid(String apikey, String mobile, String text) {
		boolean result = false;
		YunpianRestClient client = new YunpianRestClient(apikey);
		SmsOperator smsOperator = client.getSmsOperator();
		ResultDO<SendSingleSmsInfo> results = smsOperator.singleSend(mobile,
				text);
		if (results.isSuccess()) {
			result = true;
		}
		return result;
	}

	/** 根据验证码修改密码 */
	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.REQUIRED)
	public boolean upPwdByValid(String phone, String newPwd, String yzm) {
		boolean result = false;
		List<User> list = entityManager
				.createQuery(" from User o where o.phone =:phone")
				.setParameter("phone", phone).getResultList();
		if (!list.isEmpty()) {
			User u = list.get(0);
			if (yzm.equals(u.getPwd())) {
				u.setNoLockpwd(StringUtil.makePassword(newPwd));
				entityManager.persist(u);
				entityManager.flush();
				result = true;
			}
		}
		return result;
	}

	/** 签到 */
	@Transactional(TransactionPropagationType.REQUIRED)
	public boolean setSign(String userid) {
		boolean result = true;
		try {
			User u = entityManager.find(User.class, userid);
			u.setQdrq(Calendar.getInstance());
			u.setScore(u.getScore() + 1);
			entityManager.persist(u);
			entityManager.flush();
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/** 用户是否已签到 */
	public boolean isSign(String userid) {
		User u = entityManager.find(User.class, userid);
		if (u.getQdrq() == null)
			return false;
		if (CalendarUtil.getBetweenDayOfCalendar(u.getQdrq(),
				Calendar.getInstance()) > 0)
			return false;
		return true;
	}
	
	/** 积分排名 */
	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.REQUIRED)
	public List<User> getScoreRanking() {
//		List<User> list = entityManager
//				.createQuery(
//						" from User o order by o.score desc limit 0,100").getResultList();
		
		Query query = entityManager
				.createQuery(
						" from User o order by o.score desc");
		query.setMaxResults(500);
		
		List<User> list = query.getResultList();
		
		return list;
	}

}
