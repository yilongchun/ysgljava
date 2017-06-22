package com.vieking.basicdata.manager;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;

import com.vieking.basicdata.model.DictionaryType;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;

@Name("dic.dictionaryTypeQuery")
@AutoCreate
public class DictionaryTypeQuery extends BaseNqtQuery<DictionaryType>
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2920205538320399231L;

	@Override
	public void processRequestParams() {
	}

	@Override
	public String getQueryDesc() {
		return "代码字典管理";
	}

	@Override
	public String nameQueryName() {
		return "dic.dictionaryTypeQuery";
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
		return "qhb.dic.dictionaryTypeQuery";
	}

}
