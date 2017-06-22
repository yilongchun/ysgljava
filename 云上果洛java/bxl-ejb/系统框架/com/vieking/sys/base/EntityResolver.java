package com.vieking.sys.base;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@AutoCreate
@Name("entityResolver")
public class EntityResolver implements Serializable {

	private static final long serialVersionUID = 3859949866357292958L;

	@In
	protected EntityManager entityManager;

	public <T> T findEntityByUid(Class<T> c, String Uid) {
		if (c == null || Uid == null)
			return null;
		return entityManager.find(c, Uid);
	}
}
