package com.vieking.basicdata.manager;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;

import com.vieking.basicdata.model.Dictionary;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;

@Name("dic.dictionaryQuery")
@AutoCreate
public class DictionaryQuery extends BaseNqtQuery<Dictionary> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2920205538320399231L;

	@Override
	public void processRequestParams() {
	}

	@Override
	public String getQueryDesc() {
		return "字典表定义查询";
	}

	@Override
	public String nameQueryName() {
		return "dic.dictionaryQuery";
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	public Calendar gcnow() {
		return GregorianCalendar.getInstance();
	}

	@Override
	public String queryHelpName() {
		return "qhb.dic.dictionaryQuery";
	}

}
