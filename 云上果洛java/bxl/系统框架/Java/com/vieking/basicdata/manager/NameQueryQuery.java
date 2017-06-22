package com.vieking.basicdata.manager;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.core.Manager;
import org.jboss.seam.document.ByteArrayDocumentData;
import org.jboss.seam.document.DocumentData;
import org.jboss.seam.document.DocumentData.DocumentType;
import org.jboss.seam.document.DocumentStore;
import org.jboss.seam.excel.ExcelWorkbookException;
import org.jboss.seam.navigation.Pages;

import com.vieking.basicdata.model.NameQuery;
import com.vieking.basicdata.model.QueryDefine;
import com.vieking.basicdata.model.QueryFilter;
import com.vieking.basicdata.model.QueryParamDefine;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;
import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.util.EarPath;
import com.vieking.sys.util.FileUtils;
import com.vieking.sys.util.StringUtil;

@Name("sys.nameQueryQuery")
@Scope(ScopeType.PAGE)
public class NameQueryQuery extends BaseNqtQuery<NameQuery> implements
		Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 7143583542160447428L;

	private byte[] xmlData;

	private String path = "D:/NameQueryExp";

	private String impPath = "D:/NameQueryImp";

	public void reloadCache() throws DaoException {
		exp = true;
		executeQuery();
		for (Iterator<NameQuery> iterator = expResults.iterator(); iterator
				.hasNext();) {
			NameQuery nq = iterator.next();
			Events.instance().raiseEvent(Const.NAMEQUERYCHANGED,
					nq.getName());
		}
		exp = false;
		expResults = null;
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public void importByPath() throws DaoException {
		String[] impFiles = FileUtils.readFolderByPath(impPath);
		for (String fn : impFiles) {
			if (fn != null) {
				importData(impPath + "/" + fn);
			}
		}
		applyFilter();
	}

	public void importData(String fn) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(NameQuery.class,
					QueryFilter.class, QueryDefine.class, BaseEntity.class,
					QueryParamDefine.class);
			Unmarshaller um = jaxbContext.createUnmarshaller();
			NameQuery um_nq = (NameQuery) um.unmarshal(new File(fn));
			log.debug(um_nq);
			importNameQuery(um_nq);
			facesMessages.add("提示", "导入命名查询【{0}-{1}】导入成功！", um_nq.getName(),
					um_nq.getDes());
			Events.instance().raiseEvent(Const.NAMEQUERYCHANGED,
					um_nq.getName());
		} catch (Exception e) {
			e.printStackTrace();
			facesMessages.add("错误提示", "文件导入错误 {0}！", fn);
		}

	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public void importData() throws DaoException {
		if (xmlData == null) {
			facesMessages.add("错误提示", "文件数据为空！");
			return;
		}
		try {
			String xml = new String(xmlData, "UTF-8");
			log.debug("xxxxxxxxxxxxxxx--------->{0}", xml);
			JAXBContext jaxbContext = JAXBContext.newInstance(NameQuery.class,
					QueryFilter.class, QueryDefine.class, BaseEntity.class,
					QueryParamDefine.class);
			Unmarshaller um = jaxbContext.createUnmarshaller();
			StringReader sr = new StringReader(xml);
			NameQuery um_nq = (NameQuery) um.unmarshal(sr);
			log.debug(um_nq);
			importNameQuery(um_nq);
			getQhb().parm("name", "DT003").setSv(um_nq.getName());
			applyFilter();
			facesMessages.add("提示", "导入命名查询【{0}-{1}】导入成功！", um_nq.getName(),
					um_nq.getDes());
			Events.instance().raiseEvent(Const.NAMEQUERYCHANGED,
					um_nq.getName());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("导入数据失败！");
		}

	}

	@SuppressWarnings("unchecked")
	private void importNameQuery(NameQuery um_nq) {
		if (um_nq == null || StringUtils.isEmpty(um_nq.getName())) {
			facesMessages.add("错误提示", "导入命名查询对象为空或名称为空！");
			return;
		}
		NameQuery nq = nameQuery(um_nq.getName());
		if (nq != null) {
			List<String> ids = entityManager
					.createQuery(
							"select o.id from QueryParamDefine o where "
									+ "( o.id  in (select qpd.id from QueryParamDefine qpd where qpd.queryDefine.id "
									+ "  in( select  qf.id from QueryFilter qf where qf.nameQuery.id=:oid)))"
									+ " or (  o.id in ( select qpd2.id from QueryParamDefine qpd2 where qpd2.queryDefine.id=:oid)))")
					.setParameter("oid", nq.getId()).getResultList();
			log.debug("ids-------------->{0}", ids);
			if (!ids.isEmpty()) {
				// 删除参数
				entityManager
						.createQuery(
								"delete QueryParamDefine o where o.id  in (:ids)")
						.setParameter("ids", ids).executeUpdate();
			}
			// 删除过滤器
			entityManager.refresh(nq);
			for (Iterator<QueryFilter> iterator = nq.getQfs().iterator(); iterator
					.hasNext();) {
				QueryFilter qf = iterator.next();
				entityManager.remove(qf);

			}
			// 删除命名查询
			nq.getQfs().clear();
			entityManager.remove(nq);
			entityManager.flush();
			entityManager.clear();
		}
		entityManager.persist(um_nq);
		saveQpds(um_nq);
		for (Iterator<QueryFilter> iterator = um_nq.getQfs().iterator(); iterator
				.hasNext();) {
			QueryFilter qf = iterator.next();
			qf.setNameQuery(um_nq);
			entityManager.persist(qf);
			saveQpds(qf);
		}
		entityManager.flush();
	}

	private void saveQpds(QueryDefine qd) {
		for (Iterator<QueryParamDefine> iterator = qd.getQpds().iterator(); iterator
				.hasNext();) {
			QueryParamDefine qpd = iterator.next();
			qpd.setQueryDefine(qd);
			entityManager.persist(qpd);
		}
	}

	@SuppressWarnings("unchecked")
	private List<String> ormfiles(String path) throws DocumentException {
		List<String> ls = new ArrayList<String>();
		SAXReader reader = new SAXReader();
		File file = new File(path);
		if (file.exists()) {

			Document document = reader.read(file);// 读取XML文件
			Element e_root = document.getRootElement();// 得到根节点
			Element e_pu = e_root.element("persistence-unit");
			List<Element> e_mfs = e_pu.elements("mapping-file");

			for (Iterator<Element> iterator = e_mfs.iterator(); iterator
					.hasNext();) {
				Element element = iterator.next();
				ls.add(element.getText());

			}
			document = null;
		}
		file = null;
		reader = null;
		return ls;

	}

	private Map<String, String> nqMap = new HashMap<String, String>();

	@SuppressWarnings("unchecked")
	public void addNameQuery(String ormXmlPath) throws DocumentException {
		SAXReader reader = new SAXReader();
		File file = new File(ormXmlPath);
		if (file.exists()) {

			Document document = reader.read(file);// 读取XML文件
			Element e_root = document.getRootElement();// 得到根节点
			List<Element> e_nqs = e_root.elements("named-query");
			for (Iterator<Element> iterator = e_nqs.iterator(); iterator
					.hasNext();) {
				Element element = iterator.next();
				String name = String.valueOf(element.attributeValue("name"));
				String query = String.valueOf(element.elementText("query"));
				nqMap.put(StringUtils.trim(StringUtils.lowerCase(name)), query);
			}
			document = null;
		}
		file = null;
		reader = null;
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public void ormXmlToNameQuery() throws DocumentException {
		String persistenceXmlPath = EarPath.ear_home()
				+ "/safety-ejb.jar/META-INF/persistence.xml";
		log.debug("persistenceXmlPath--->{0}", persistenceXmlPath);
		List<String> filePaths = ormfiles(persistenceXmlPath);
		nqMap.clear();
		for (Iterator<String> iterator = filePaths.iterator(); iterator
				.hasNext();) {
			String ormFn = iterator.next();
			log.debug("ormFileName--->{0}", ormFn);
			addNameQuery(EarPath.ear_home() + "/safety-ejb.jar/" + ormFn);
		}
		for (Iterator<String> iterator = nqMap.keySet().iterator(); iterator
				.hasNext();) {
			String name = iterator.next();
			String query = nqMap.get(name);
			log.debug("name----> {0}  query---> {1}", name, query);

		}
		update();

	}

	public void expAll() throws JAXBException, ClassNotFoundException,
			DaoException, UnsupportedEncodingException {
		exp = true;
		executeQuery();
		for (Iterator<NameQuery> iterator = expResults.iterator(); iterator
				.hasNext();) {
			NameQuery nq = iterator.next();
			String expName = nq.getName() + "_"
					+ (nq.getDes() == null ? "#" : nq.getDes()) + "_"
					+ StringUtil.toStr(nq.mt()) + ".exp";
			JAXBContext context = JAXBContext.newInstance(Class
					.forName("com.vieking.basicdata.model.NameQuery"));
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			StringWriter sw = new StringWriter();
			marshaller.marshal(nq, sw);
			byte[] bytes = sw.toString().getBytes("UTF-8");
			FileUtils.writeFile(bytes, path + "/" + expName);
		}
		exp = false;
		expResults.clear();
		expResults = null;
	}

	/**
	 * Puts document in store and redirects
	 * 
	 * @throws JAXBException
	 * @throws ClassNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void xmlExp(Object objId) throws JAXBException,
			ClassNotFoundException, UnsupportedEncodingException {

		NameQuery eo = entityManager.find(NameQuery.class, objId);
		if (eo == null)
			return;
		String viewId = Pages.getViewId(FacesContext.getCurrentInstance());
		String baseName = Pages.getCurrentBaseName();
		baseName = eo.getName() + "_"
				+ (eo.getDes() == null ? "" : eo.getDes());
		baseName = java.net.URLEncoder.encode(baseName, "UTF-8");
		JAXBContext context = JAXBContext.newInstance(Class
				.forName("com.vieking.basicdata.model.NameQuery"));
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		StringWriter sw = new StringWriter();
		marshaller.marshal(eo, sw);
		byte[] bytes = sw.toString().getBytes("UTF-8");

		DocumentType docType = new DocumentType("exp",
				"application/x-wais-source");

		DocumentData documentData = new ByteArrayDocumentData(baseName,
				docType, bytes);
		String id = DocumentStore.instance().newId();
		String url = DocumentStore.instance().preferredUrlForContent(baseName,
				"exp", id);
		url = Manager.instance().encodeConversationId(url, viewId);
		DocumentStore.instance().saveData(id, documentData);

		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(url);
		} catch (IOException e) {
			throw new ExcelWorkbookException(Interpolator.instance()
					.interpolate("Could not redirect to #0", url), e);
		}
	}

	@SuppressWarnings("unchecked")
	public NameQuery nameQuery(String name) {
		List<NameQuery> ls = entityManager
				.createQuery("from NameQuery o where o.name=:name")
				.setParameter("name",
						StringUtils.lowerCase(StringUtils.trim(name)))
				.getResultList();
		if (ls.isEmpty()) {
			return null;
		} else {
			return ls.get(0);
		}

	}

	public void update() {
		for (Iterator<String> iterator = nqMap.keySet().iterator(); iterator
				.hasNext();) {
			String name = iterator.next();
			String query = nqMap.get(name);
			// 如果是Count查询
			if (name.indexOf("count") > 0) {
				int i = name.indexOf("count");
				if (i == (name.length() - 5)) {
					String qname = name.substring(0, (name.length() - 5));
					log.debug(
							"update count name---->{0}  qname----> {1}  query---> {2}",
							name, qname, query);
					NameQuery nq = nameQuery(qname);
					if (nq == null) {
						nq = new NameQuery();
					}
					nq.setName(qname);
					nq.setCountQuery(query);
					entityManager.persist(nq);
					entityManager.flush();
				}

			} else {
				NameQuery nq = nameQuery(name);
				if (nq == null) {
					nq = new NameQuery();
				}
				nq.setName(name);
				nq.setFindQuery(query);
				log.debug("update  qname----> {0}  query---> {1}", name, query);
				entityManager.persist(nq);
				entityManager.flush();
			}

		}
	}

	@Observer(value = "applyfilter", create = false)
	public void rexecute() {
		super.applyFilter();
	}

	public void deleteQf(String id) {

	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public void delNameQuery(NameQuery o) {
		NameQuery nq = entityManager.find(NameQuery.class, o.getId());

		for (Iterator<QueryFilter> iterator = nq.getQfs().iterator(); iterator
				.hasNext();) {
			QueryFilter qf = iterator.next();
			if (qf.getQpds().size() > 0) {
				for (Iterator<QueryParamDefine> iterator2 = qf.getQpds()
						.iterator(); iterator2.hasNext();) {
					QueryParamDefine qd = iterator2.next();
					entityManager.remove(qd);
				}
				entityManager.remove(qf);
			} else {
				entityManager.remove(qf);
			}
		}
		for (Iterator<QueryParamDefine> iterator2 = nq.getQpds().iterator(); iterator2
				.hasNext();) {
			QueryParamDefine qd = iterator2.next();
			entityManager.remove(qd);
		}
		entityManager.remove(nq);
		entityManager.flush();
		Events.instance().raiseEvent("applyfilter");
	}

	public String getQueryDesc() {
		return "查询管理";
	}

	public NameQueryQuery() {
		setOrder("o.name asc ");
	}

	public byte[] getXmlData() {
		return xmlData;
	}

	public void setXmlData(byte[] xmlData) {
		this.xmlData = xmlData;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getImpPath() {
		return impPath;
	}

	public void setImpPath(String impPath) {
		this.impPath = impPath;
	}

	@Override
	public void processRequestParams() {
	}

	@Override
	public String nameQueryName() {
		return "sys.nameQuery";
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.sys.nameQuery";
	}

}
