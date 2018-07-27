package com.vieking.functions.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.base.BaseHome;
import com.vieking.basicdata.model.Dictionary;
import com.vieking.role.model.Assistance;
import com.vieking.role.model.VoteItem;
import com.vieking.sys.enumerable.RegistrationState;
import com.vieking.sys.util.StringUtil;


@Name("fun.voteHome")
@AutoCreate
public class VoteHome  extends BaseHome<Assistance>{

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
		getInstance().setBzlx(entityManager.find(Dictionary.class, "XWLX300"));
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
		o.setRegState(RegistrationState.已审核);	
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
		o.setRegState(RegistrationState.已审核);		
		saveVoteItems();
		entityManager.persist(o);
		entityManager.flush(); 
		lastStateChange = super.update();
		return lastStateChange;
	}
	
	//投票选项
	public VoteItem voteItem;
	
	public void addVoteItem(){
		voteItem = new VoteItem(getInstance());
	}
	
	public void setVoteItem() {
		if (voteItem != null) {
			for (int i = 0; i < getInstance().getVoteItems().size(); i++) {
				VoteItem item = getInstance().getVoteItems().get(i);
				if (voteItem.getTmpId().equals(item.getTmpId())) {
					getInstance().getVoteItems().remove(item);
				}
			}
		}
		getInstance().getVoteItems().add(voteItem);
	}
	
	public void editVoteItem(VoteItem o) {
		voteItem = o;
	}

	public void reVoteItems(VoteItem o) {
		getInstance().getVoteItems().remove(o);
	}

	/** 保存定义信息 */
	public void saveVoteItems() {
		List<String> dicIds = new ArrayList<String>();
		for (Iterator<VoteItem> iterator = getInstance().getVoteItems()
				.iterator(); iterator.hasNext();) {
			VoteItem item = iterator.next();
			entityManager.persist(item);
			entityManager.flush();
			dicIds.add(item.getId());
		}
		if (dicIds.isEmpty()) {
			dicIds.add("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		}
		entityManager
		.createQuery(
				"delete VoteItem o where o.assistance.id =:dId and o.id not in (:dicds)")
		.setParameter("dId", getInstance().getId())
		.setParameter("dicds", dicIds).executeUpdate();
	}

	public VoteItem getVoteItem() {
		return voteItem;
	}

	public void setVoteItem(VoteItem voteItem) {
		this.voteItem = voteItem;
	}

	@Override
	public String appName() {
		return "投票";
	}

	@Override
	public String instanceInfo() {
		return "投票";
	}
}
