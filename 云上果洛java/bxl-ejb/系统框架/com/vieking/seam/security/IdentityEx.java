package com.vieking.seam.security;

import static org.jboss.seam.ScopeType.SESSION;

import java.security.Principal;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.Role;
import org.jboss.seam.security.SimpleGroup;

@Name("org.jboss.seam.security.identity")
@Scope(SESSION)
@Install(precedence = Install.APPLICATION)
@BypassInterceptors
@Startup
public class IdentityEx extends Identity {

	private static final long serialVersionUID = -6505673961341222899L;

	public static final String ROLES_GLOBAL_GROUP = "GlobalRoles";

	@Override
	public boolean hasRole(String role) {
		return super.hasRole(StringUtils.trim(StringUtils.upperCase(role)));
	}

	public boolean hasInAllRoles(String... params) {
		for (int i = 0; i < params.length; i++) {
			if (hasRole(params[i].toUpperCase()))
				return true;
			if (hasGlobalRole(params[i].toUpperCase()))
				return true;
		}
		return false;
	}

	public boolean notHasInAllRoles(String... params) {
		for (int i = 0; i < params.length; i++) {
			if (hasRole(params[i].toUpperCase()))
				return false;
			if (hasGlobalRole(params[i].toUpperCase()))
				return false;
		}
		return true;
	}

	public boolean hasRoles(String... params) {
		for (int i = 0; i < params.length; i++) {
			if (hasRole(params[i].toUpperCase()))
				return true;
		}
		return false;
	}

	public boolean notHasRoles(String... params) {
		for (int i = 0; i < params.length; i++) {
			if (hasRole(params[i].toUpperCase()))
				return false;
		}
		return true;
	}

	public List<String> hasRoleList(String... params) {
		List<String> roleList = new ArrayList<String>();
		instance();
		for (int i = 0; i < params.length; i++) {
			if (hasRole(params[i].toUpperCase()))
				roleList.add(params[i]);
		}
		return roleList;
	}

	public List<String> roles() {
		return roles(ROLES_GROUP);
	}

	public List<String> globalRoles() {
		return roles(ROLES_GLOBAL_GROUP);
	}

	@SuppressWarnings("unchecked")
	public List<String> roles(String groupType) {
		List<String> ls = new ArrayList<String>();
		for (Group sg : getSubject().getPrincipals(Group.class)) {
			if (groupType.equals(sg.getName())) {
				Enumeration<Principal> ee = (Enumeration<Principal>) sg
						.members();
				while (ee.hasMoreElements()) {
					Principal principal = ee.nextElement();
					ls.add(principal.getName());
				}
			}
		}
		Collections.sort(ls);
		return ls;
	}

	public boolean addGlobalRole(String role) {
		if (role == null || "".equals(role))
			return false;
		for (Group sg : getSubject().getPrincipals(Group.class)) {
			if (ROLES_GLOBAL_GROUP.equals(sg.getName())) {
				return sg.addMember(new Role(role));
			}
		}
		SimpleGroup roleGroup = new SimpleGroup(ROLES_GLOBAL_GROUP);
		roleGroup.addMember(new Role(role));
		getSubject().getPrincipals().add(roleGroup);
		return true;
	}

	public boolean hasGlobalRoles(String... params) {
		for (int i = 0; i < params.length; i++) {
			if (hasGlobalRole(params[i].toUpperCase()))
				return true;
		}
		return false;
	}

	public boolean notGlobalRoles(String... params) {
		for (int i = 0; i < params.length; i++) {
			if (hasGlobalRole(params[i].toUpperCase()))
				return false;
		}
		return true;
	}

	public boolean hasGlobalRole(String role) {
		return hasRole(role, ROLES_GLOBAL_GROUP);
	}

	public boolean hasRole(String role, String groupType) {
		if (!securityEnabled)
			return true;
		for (Group sg : getSubject().getPrincipals(Group.class)) {
			if (groupType.equals(sg.getName())) {
				return sg.isMember(new Role(role));
			}
		}
		return false;
	}
}
