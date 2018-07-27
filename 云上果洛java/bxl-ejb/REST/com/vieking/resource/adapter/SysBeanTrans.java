package com.vieking.resource.adapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vieking.basicdata.model.Dictionary;
import com.vieking.functions.model.Collection;
import com.vieking.functions.model.Comment;
import com.vieking.functions.model.Contact;
import com.vieking.functions.model.ContactPost;
import com.vieking.functions.model.UserNotice;
import com.vieking.resource.bean.AssistanceBean;
import com.vieking.resource.bean.BuMenBean;
import com.vieking.resource.bean.CollectionBean;
import com.vieking.resource.bean.CommentBean;
import com.vieking.resource.bean.ContactBean;
import com.vieking.resource.bean.DictionaryBean;
import com.vieking.resource.bean.UserBean;
import com.vieking.resource.bean.UserNoticeBean;
import com.vieking.resource.bean.VoteItemBean;
import com.vieking.role.model.Assistance;
import com.vieking.role.model.User;
import com.vieking.role.model.VoteItem;
import com.vieking.sys.util.CalendarUtil;

public class SysBeanTrans {

	@SuppressWarnings("unused")
	private String encoding(String str) {
		try {
			return new String(str.getBytes("UTF-8"), "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static CommentBean toBean(Comment o) {
		CommentBean bean = new CommentBean();
		setProperties(o, bean);
		return bean;
	}

	protected static void setProperties(Comment obj, CommentBean d) {
		d.setId(obj.getId());
		d.setName(obj.getUser().getName());
		d.setUserid(obj.getUser().getId());
		d.setNewsid(obj.getAssistance().getId());
		d.setPl(obj.getPlnr());
		d.setPlsj(CalendarUtil.dataTimeStr(obj.getPlsj()));

	}

	public static UserNoticeBean toBean(UserNotice o) {
		UserNoticeBean bean = new UserNoticeBean();
		setProperties(o, bean);
		return bean;
	}

	protected static void setProperties(UserNotice obj, UserNoticeBean d) {
		d.setId(obj.getId());
		d.setBiaoti(obj.getAssistance().getBiaoti());
		d.setFbsj(CalendarUtil.dataTimeStr(obj.getAssistance().getFbsj()));
		d.setNerong(obj.getAssistance().getNerong());
		d.setReads(obj.getIsreads());
		d.setUser(obj.getAssistance().getNamea());
	}

	public static CollectionBean toBean(Collection o) {
		CollectionBean bean = new CollectionBean();
		setProperties(o, bean);
		return bean;
	}

	protected static void setProperties(Collection obj, CollectionBean d) {
		d.setId(obj.getId());
		d.setNewsBt(obj.getAssistance().getBiaoti());
		d.setNewsId(obj.getAssistance().getId());
		d.setScsj(obj.getScsj() == null ? "" : CalendarUtil.dataTimeStr(obj
				.getScsj()));
		d.setUserId(obj.getUser().getId());
	}

	public static ContactBean toBean(Contact o) {
		ContactBean bean = new ContactBean();
		setProperties(o, bean);
		return bean;
	}

	protected static void setProperties(Contact obj, ContactBean d) {
		d.setId(obj.getId());
		d.setPhone(obj.getPhone());
		d.setName(obj.getName());
		d.setZhuce(obj.getReg());
		d.setLevel(obj.getLevel() == null ? "" : obj.getLevel().getCode());
		d.setEmail(obj.getEmail() == null ? "" : obj.getEmail());
		d.setJianjie(obj.getJianjie() == null ? "" : obj.getJianjie());
		d.setPost(obj.getPost() == null ? "" : obj.getPost());
		d.setZhiwustr(obj.getPost() == null ? "" : obj.getPost());
		d.setTelephone(obj.getTelephone() == null ? "" : obj.getTelephone());
		for (Iterator<ContactPost> iterator = obj.getContactPosts().iterator(); iterator
				.hasNext();) {
			ContactPost contactPost = iterator.next();
			BuMenBean bm = new BuMenBean();
			// ZhiWuBean zw = new ZhiWuBean();
			bm.setBmcode(contactPost.getLxrbm().getCode());
			bm.setBumen(contactPost.getLxrbm().getName());
			bm.setSuperCode(contactPost.getLxrbm().getCode());
			bm.setWn(contactPost.getLxrbm().getWn());
			// zw.setZhiwu(contactPost.getLxrzw().getCode());
			// zw.setZwcode(contactPost.getLxrzw().getName());
			d.getBmlist().add(bm);
			// d.getZwlist().add(zw);
		}
	}

	public static AssistanceBean toBean(Assistance o) {
		AssistanceBean bean = new AssistanceBean();
		setProperties(o, bean);
		return bean;
	}

	protected static void setProperties(Assistance obj, AssistanceBean d) {
		d.setId(obj.getId());
		d.setBiaoti(obj.getBiaoti());
		d.setSubTitle(obj.getSubTitle());
		d.setBzlx(obj.getBzlx().getName());
		d.setFjlx(obj.getFjlx() == null ? "" : obj.getFjlx().getName());
		d.setFbsj(obj.getFbsj() == null ? "" : CalendarUtil.getYyyyMmDd(obj
				.getFbsj()));
		d.setNerong(obj.getNerong());
		d.setUser(obj.getUser()!=null ? obj.getUser().getName() : "");
		d.setLlcs(obj.getLlcs());
		d.setVideoUrl(obj.getVideoUrl());
		d.setRegState(obj.getRegState().getDesc());
		d.setRemark(obj.getRemark());
		d.setZanCount(obj.getZanCount());
		d.setDistrs(obj.getDistrs());
		d.setZhiding(obj.getSyncSign());
		
		if (obj.getVoteItems() != null && obj.getVoteItems().size()>0) {
			List<VoteItemBean> beans = new ArrayList<VoteItemBean>();
			for (int i = 0; i < obj.getVoteItems().size(); i++) {
				VoteItem o = obj.getVoteItems().get(i);
				VoteItemBean bean = toBean(o);
				beans.add(bean);
			}
			d.setVoteItemBeans(beans);
		}
	}
	
	public static VoteItemBean toBean(VoteItem o) {
		VoteItemBean bean = new VoteItemBean();
		setProperties(o, bean);
		return bean;
	}

	protected static void setProperties(VoteItem obj, VoteItemBean d) {
		d.setId(obj.getId());
		d.setItem(obj.getItem());
		d.setItemCount(obj.getItemCount());
	}

	public static DictionaryBean toBean(Dictionary o) {
		DictionaryBean bean = new DictionaryBean();
		setProperties(o, bean);
		return bean;
	}

	protected static void setProperties(Dictionary obj, DictionaryBean d) {
		d.setId(obj.getId());
		d.setCode(obj.getCode());
		d.setName(obj.getName());
		d.setWn(obj.getWn());
		d.setSuperCode(obj.getSuperior() == null ? "" : obj.getSuperior()
				.getCode());
	}

	public static UserBean toBean(User obj) {
		UserBean bean = new UserBean();
		setProperties(obj, bean);
		return bean;
	}

	protected static void setProperties(User obj, UserBean d) {
		d.setId(obj.getId());
		d.setLoginName(obj.getLoginName());
		d.setName(obj.getName());
		d.setPhone(obj.getPhone() == null ? "" : obj.getLoginName());
		d.setSex(obj.getSex() == null ? "男" : obj.getSex().getName());
		d.setEmail(obj.getYx() == null ? "" : obj.getYx());
		d.setJianjie(obj.getXxmc() == null ? "" : obj.getXxmc());
		d.setScore(obj.getScore());
		d.setUserType(obj.getUserType() == null ? "0" : obj.getUserType());
		d.setThirdId(obj.getThirdId() == null ? "" : obj.getThirdId());
	}
	
	

}
