package com.vieking.resource.dao;

import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONObject;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.functions.model.Contact;
import com.vieking.functions.model.Register;
import com.vieking.resource.SendCode;
import com.vieking.role.model.User;
import com.vieking.role.model.UserGroup;
import com.vieking.sys.base.BaseDaoHibernate;
import com.vieking.sys.util.StringUtil;
import com.yunpian.sdk.model.ResultDO;
import com.yunpian.sdk.model.SendSingleSmsInfo;
import com.yunpian.sdk.service.SmsOperator;
import com.yunpian.sdk.service.YunpianRestClient;

@Name("registerDao")
@AutoCreate
public class RegisterDao extends BaseDaoHibernate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7811814922827204952L;

	/** 修改手机号码 */
	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.REQUIRED)
	public User upDataPhone(String oldPhone, String newPhone, String yzm) {
		User user = null;
		List<User> list = entityManager
				.createQuery(" from User o where o.phone =:phone")
				.setParameter("phone", oldPhone).getResultList();
		if (!list.isEmpty()) {
			user = list.get(0);
			// if (yzm.equals(user.getPwd())) {
			user.setLoginName(newPhone);
			user.setPhone(newPhone);
			UserGroup userGroup = entityManager.find(UserGroup.class, "user");
			user.setGroup(userGroup);
			entityManager.persist(user);
			List<Contact> clist = entityManager
					.createQuery(" from Contact o where o.phone =:phone")
					.setParameter("phone", oldPhone).getResultList();
			if (!clist.isEmpty()) {
				Contact contact = clist.get(0);
				contact.setPhone(newPhone);
				entityManager.persist(contact);
			}
		} else {
				user = null;
			}
//		}
		entityManager.flush();
		return user;
	}

	/** 修改邮箱 */
	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.REQUIRED)
	public boolean upEmail(String phone, String email) {
		boolean result = false;
		List<User> list = entityManager
				.createQuery(" from User o where o.phone =:phone")
				.setParameter("phone", phone).getResultList();
		if (!list.isEmpty()) {
			User user = list.get(0);
			user.setYx(email);
			entityManager.persist(user);
			List<Contact> clist = entityManager
					.createQuery(" from Contact o where o.phone =:phone")
					.setParameter("phone", phone).getResultList();
			if (!clist.isEmpty()) {
				Contact contact = clist.get(0);
				contact.setEmail(email);
				entityManager.persist(contact);
			}
			result = true;
		}
		entityManager.flush();
		return result;
	}

	/** 修改/找回密码 */
	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.REQUIRED)
	public boolean upPwd(String phone, String oldPwd, String newPwd) {
		boolean result = false;
		List<User> list = entityManager
				.createQuery(" from User o where o.phone =:phone")
				.setParameter("phone", phone).getResultList();
		if (!list.isEmpty()) {
			User u = list.get(0);
			if (StringUtil.makePassword(oldPwd).equals(u.getNoLockpwd())) {
				u.setNoLockpwd(StringUtil.makePassword(newPwd));
				entityManager.persist(u);
				entityManager.flush();
				result = true;
			}
		}
		return result;
	}

	/** 验证手机号码 */
	@SuppressWarnings("unchecked")
	public boolean validPhone(String phone) {
		boolean result = false;
		List<Contact> list = entityManager
				.createQuery(
						" from Contact o where o.phone =:phone and o.reg=0")
				.setParameter("phone", phone).getResultList();
		if (!list.isEmpty())
			result = true;
		return result;
	}

	@SuppressWarnings("unchecked")
	public Contact getContact(String phone) {
		Contact result = null;
		List<Contact> list = entityManager
				.createQuery(" from Contact o where o.phone =:phone ")
				.setParameter("phone", phone).getResultList();
		if (!list.isEmpty())
			result = list.get(0);
		return result;
	}

	/** 注册保存用户 */
	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.REQUIRED)
	public User saveUser(String phone, String pwd, String yzm) {
		List<Register> rlist = entityManager
				.createQuery(
						" from Register o where o.phone=:phone and o.yzm=:yzm ")
				.setParameter("phone", phone).setParameter("yzm", yzm)
				.getResultList();
		if (rlist.isEmpty())
			return new User();
		User user = null;
		List<User> list = entityManager
				.createQuery(" from User o where o.phone =:phone")
				.setParameter("phone", phone).getResultList();
		if (list.isEmpty()) {
			List<Contact> contacts = entityManager
					.createQuery(
							" from Contact o where o.phone =:phone and o.reg=0")
					.setParameter("phone", phone).getResultList();
			if (!contacts.isEmpty()) {
				Contact contact = contacts.get(0);
				user = new User();
				user.setLoginName(phone);
				user.setName(contact.getName());
				user.setPhone(phone);
				user.setNoLockpwd(StringUtil.makePassword(pwd));
				user.setSex(contact.getSex());
				user.setYx(contact.getEmail());
				// user.setLxrbm(contact.getLxrbm());
				// user.setLxrgw(contact.getLxrzw());
				UserGroup userGroup = entityManager.find(UserGroup.class,
						"user");
				user.setGroup(userGroup);
				entityManager.persist(user);

				contact.setReg(1);
				entityManager.persist(contact);

				Register reg = rlist.get(0);
				reg.setReg(1);
				entityManager.persist(reg);

				entityManager.flush();
			}
		}
		return user;
	}

	/** 手机注册验证码 */
	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.REQUIRED)
	public String singleSend(String phone) {
		List<User> list = entityManager
				.createQuery(" from User o where o.phone =:phone")
				.setParameter("phone", phone).getResultList();
		if (list.isEmpty()) {
			List<Contact> contacts = entityManager
					.createQuery(
							" from Contact o where o.phone =:phone and o.reg=0")
					.setParameter("phone", phone).getResultList();
			if (!contacts.isEmpty()) {
				
				try {
					String res = SendCode.sendMsg(phone);
					JSONObject obj = JSONObject.fromObject(res);
			        int code = obj.getInt("code");
			        String msg = obj.getString("obj");
			        if (code == 200) {
			        	Contact contact = contacts.get(0);
						List<Register> rlist = entityManager
								.createQuery(
										" from Register o where o.phone =:phone ")
								.setParameter("phone", phone).getResultList();
						Register reg = null;
						if (rlist.isEmpty()) {
							reg = new Register();
							reg.setName(contact.getName());
							reg.setPhone(phone);
							reg.setZcsj(Calendar.getInstance());
							reg.setYzm(msg);
							reg.setReg(0);
							entityManager.persist(reg);
						} else {
							reg = rlist.get(0);
							reg.setYzm(msg);
							reg.setReg(0);
							entityManager.persist(reg);
						}

						entityManager.flush();
						return res;
					}
				} catch (Exception e) {
					
				}
				
				
				
				
				
//				YunpianRestClient client = new YunpianRestClient(apikey);
//				SmsOperator smsOperator = client.getSmsOperator();
//				ResultDO<SendSingleSmsInfo> result = smsOperator.singleSend(
//						mobile, text);
//				if (result.isSuccess()) {
//					Contact contact = contacts.get(0);
//					List<Register> rlist = entityManager
//							.createQuery(
//									" from Register o where o.phone =:phone ")
//							.setParameter("phone", phone).getResultList();
//					Register reg = null;
//					if (rlist.isEmpty()) {
//						reg = new Register();
//						reg.setName(contact.getName());
//						reg.setPhone(phone);
//						reg.setZcsj(Calendar.getInstance());
//						reg.setYzm(yzm);
//						reg.setReg(0);
//						entityManager.persist(reg);
//					} else {
//						reg = rlist.get(0);
//						reg.setYzm(yzm);
//						reg.setReg(0);
//						entityManager.persist(reg);
//					}
//
//					entityManager.flush();
//					return true;
//				}
			}
		}
		return null;

	}

}
