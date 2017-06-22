package com.vieking.role.manager;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;

import com.vieking.role.model.User;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;
import com.vieking.sys.model.BaseEntity;

@Name("sys.userQuery")
@Scope(ScopeType.PAGE)
public class UserQueryEnh extends BaseNqtQuery<User> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7143583542160447428L;

	public void processRequestParams() {

	}

	@Override
	@Transactional
	public void deleteItem(Object objId) {
		try {
			Object obj = entityManager.find(getEntityClass(), objId);
			if (obj == null) {
				getFacesMessages().add("id={0}数据未找到，恢复数据失败！", objId);
				return;
			}
			if (isBaseEntity(obj)) {
				BaseEntity eo = (BaseEntity) obj;
				if (eo.readOnly()) {
					getFacesMessages().add("数据处于锁定状态，不能删除！");
					return;
				}
 
				entityManager.remove(eo);
				entityManager.flush();
				refreshResults();
			}
		} catch (Exception e) {
			getFacesMessages().add("删除数据失败！存在其他数据关联！");
		}
	}

	public String getQueryDesc() {
		return "用户查询";
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.sys.user";
	}

	@Override
	public String nameQueryName() {
		return "sys.user";
	}

}
