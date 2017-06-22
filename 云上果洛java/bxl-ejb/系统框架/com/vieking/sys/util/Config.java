package com.vieking.sys.util;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Events;

import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.KeException;

@Name("app.config")
@Scope(ScopeType.APPLICATION)
@Startup
@BypassInterceptors
public class Config implements Serializable {

	private static final long serialVersionUID = 2398294597538567129L;

	private static final String confpath = EarPath.ear_home()
			+ "/config.properties";

	private Configuration config;

	private String key;

	private String value;

	public List<String> keyls = new ArrayList<String>();

	public static Config instance() {
		return (Config) Component.getInstance("app.config");
	}

	public void showValue() {
		value = config.getString(key);
	}

	public void addProp() {
		config.addProperty(key, value);
		Events.instance().raiseEvent(Const.CONFIGCHANGED);
	}

	public void setProp() {
		config.setProperty(key, value);
		Events.instance().raiseEvent(Const.CONFIGCHANGED);
	}

	@Create
	public void create() throws KeException {
		initfile();
		try {
			config = new PropertiesConfiguration(confpath);
			initkeyls();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	@Observer(create = false, value = Const.CONFIGCHANGED)
	public void initkeyls() {
		keyls.clear();
		for (Iterator iterator = config.getKeys(); iterator.hasNext();) {
			keyls.add(String.valueOf(iterator.next()));
		}
	}

	public void reload() throws KeException {
		config.clear();
		initfile();
		try {
			config = new PropertiesConfiguration(confpath);
			Events.instance().raiseEvent(Const.CONFIGCHANGED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return String result with key
	 * @throws KeException
	 */
	public String get(String key) throws KeException {
		return config.getString(key);
	}

	private final void initfile() throws KeException {
		File file = new File(confpath);
		if (file.exists()) {
			return;
		} else {
			throw new KeException("系统路径配置文件未找到！！");
		}
	}

	public Configuration getConfig() {
		return config;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getKeyls() {
		return keyls;
	}

	public void setKeyls(List<String> keyls) {
		this.keyls = keyls;
	}
}
