package com.vieking.frame.seam;

import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.core.Init;
import org.jboss.seam.framework.PersistenceController;

import com.vieking.role.model.User;
import com.vieking.sys.exception.Const;
import com.vieking.sys.util.Config;

public abstract class BaseEntityManagerController<E> extends
	PersistenceController<EntityManager> {

    private static final long serialVersionUID = 3122462934602895639L;

    private Init init;

    private Config config;

    private User currentUser;

    public EntityManager getEntityManager() {
	EntityManager em = getPersistenceContext();
	if (em.isOpen()) {
	    return em;
	} else {
	    setPersistenceContext(null);
	    em = getPersistenceContext();
	    return em;
	}
    }

    @Override
    protected String getPersistenceContextName() {
	return "entityManager";
    }

    protected User getCurrentUser() {
	if (currentUser == null) {
	    currentUser = (User) getSessionContext().get(Const.CURRUSER);
	}
	return currentUser;
    }

    protected Config getConfig() {
	if (config == null) {
	    config = (Config) Component.getInstance("app.config");
	}
	return config;
    }

    protected Init getInit() {
	if (init == null) {
	    init = (Init) getApplicationContext().get(
		    "org.jboss.seam.core.init");
	}
	return init;
    }

}
