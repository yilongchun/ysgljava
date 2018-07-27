package com.vieking.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.vieking.basicdata.dao.DictionaryDao;
import com.vieking.basicdata.model.Dictionary;
import com.vieking.resource.adapter.SysBeanTrans;
import com.vieking.resource.bean.DictionaryBean;
import com.vieking.sys.exception.ReConst;

@Path("dictionary")
@Name("dictionaryResource")
public class DictionaryResource implements ReConst {

	@Logger
	private Log log;
	@In
	private DictionaryDao dictionaryDao;

	// 获取部门岗位信息
	@GET
	@Path("getDictionarys")
	@Produces("application/json;charset=UTF-8")
	public Collection<DictionaryBean> getDictionarys() {
		List<DictionaryBean> list = new ArrayList<DictionaryBean>();
		for (Iterator<Dictionary> iterator = dictionaryDao.dictionnaryType(
				"BMLX").iterator(); iterator.hasNext();) {
			Dictionary dac = iterator.next();
			DictionaryBean c = new DictionaryBean();
			c = SysBeanTrans.toBean(dac);
			list.add(c);
		}
		return list;
	}

	// 通过上级code获取字典数据
	@GET
	@Path("getDictionarys/{superCode}")
	@Produces("application/json;charset=UTF-8")
	public Collection<DictionaryBean> getDictionarys(
			@PathParam("superCode") String superCode) {
		List<DictionaryBean> list = new ArrayList<DictionaryBean>();
		for (Iterator<Dictionary> iterator = dictionaryDao.dictionarys(
				superCode).iterator(); iterator.hasNext();) {
			Dictionary dac = iterator.next();
			DictionaryBean c = new DictionaryBean();
			c = SysBeanTrans.toBean(dac);
			list.add(c);
		}
		return list;
	}

}
