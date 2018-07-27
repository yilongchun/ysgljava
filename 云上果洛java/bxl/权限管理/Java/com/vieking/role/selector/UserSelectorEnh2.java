package com.vieking.role.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.vieking.base.NqtSelector;
import com.vieking.basicdata.model.Department;
import com.vieking.role.model.User;
import com.vieking.sys.base.IResultConvert;

@Name("sys.userSelector2")
@Scope(ScopeType.PAGE)
public class UserSelectorEnh2 extends NqtSelector<User> implements
		IResultConvert<User> {

	private static final long serialVersionUID = -4780934312775103604L;

	public List<Department> getBmList() {
		List<Department> list = entityManager.createQuery("from Department o")
				.getResultList();
		return list;
	}

	private Department bumen;

	public Department getBumen() {
		return bumen;
	}

	public void setBumen(Department bumen) {
		this.bumen = bumen;
	}

	@Override
	public String getQueryDesc() {
		return "用户选择器";
	}

	@Override
	public String nameQueryName() {
		return "sys.user2";
	}

	@Override
	public String queryHelpName() {
		return "sys.user.selector2";
	}

	@Override
	public String defEvent() {
		return "event.sel.com.vieking.sys.model.User";
	}

	@Override
	protected Object getObjectId(User obj) {
		return obj.getId();
	}

	@Override
	protected String getObjectName(User obj) {
		return obj.getName();
	}

	@Override
	protected String getViewId() {
		return "/common/selector/userSelector2.xhtml";
	}

	@Override
	public void processRequestParams() {
		if (bumen != null) {
			getQhb().parm("bmcode", "DT003").setSv(bumen.getCode());
		}
	}

	public List<User> toResult(List<Object> ls) {
		List<User> result = new ArrayList<User>();

		for (Iterator<Object> iterator = ls.iterator(); iterator.hasNext();) {
			Object[] row = (Object[]) iterator.next();
			User obj = new User();
			obj.setId(row[0] == null ? null : (String) row[0]);
			obj.setLoginName(row[21] == null ? null : (String) row[21]);
			obj.setName(row[22] == null ? null : (String) row[22]);
			obj.setPhone(row[26] == null ? null : (String) row[26]);

			// obj.setOrgname(row[0] == null ? null : (String) row[0]);
			// obj.setPname(row[1] == null ? null : (String) row[1]);
			// obj.setBm(row[2] == null ? null : (String) row[2]);
			// obj.setGz(row[3] == null ? null : (String) row[3]);
			// obj.setJzname(row[4] == null ? null : (String) row[4]);
			// obj.setPhone(row[6] == null ? null : (String) row[6]);
			// if (row[5] != null) {
			// SimpleDateFormat sdf = new SimpleDateFormat(
			// "yyyy-MM-dd HH:mm:ss");
			// obj.setDrrq(sdf.format((Timestamp) row[5]));
			// }
			result.add(obj);
		}
		return result;

	}

}
