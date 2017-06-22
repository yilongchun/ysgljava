package com.vieking.sys.base;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.core.Manager;
import org.jboss.seam.document.ByteArrayDocumentData;
import org.jboss.seam.document.DocumentData;
import org.jboss.seam.document.DocumentData.DocumentType;
import org.jboss.seam.document.DocumentStore;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.navigation.Pages;

import com.vieking.basicdata.dao.DictionaryDao;
import com.vieking.basicdata.dao.NqtDao;
import com.vieking.sys.enumerable.DataState;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.exception.MyRunException;
import com.vieking.sys.exception.RunException;
import com.vieking.sys.model.BaseEntity;
import com.vieking.sys.util.FileUtils;
import com.vieking.sys.util.HibernateUtil;

@Scope(ScopeType.PAGE)
public abstract class BaseNqtQuery<T> extends BaseQuery<T> implements
		Serializable {

	private static final long serialVersionUID = 6320235176967268609L;

	@In
	protected EntityManager entityManager;

	@In
	protected FacesMessages facesMessages;
	
	@In(value = "dictionaryDao")
	protected DictionaryDao dictionaryDao;
	
 
	
	@In("sys.nqtDao")
	protected NqtDao nqtDao;

	protected byte[] xmlData;

	protected String xmlExpPath;

	protected String xmlImpPath;

	/** 初始化页面请求参数 */
	public abstract void processRequestParams() throws DaoException;

	/**
	 * Puts document in store and redirects
	 * 
	 * @throws UnsupportedEncodingException
	 */
	protected void redirectExport(byte[] bytes, String baseName)
			throws DaoException {
		try {
			DocumentType dt = new DocumentData.DocumentType("xls",
					"application/vnd.ms-excel");
			String viewId = Pages.getViewId(FacesContext.getCurrentInstance());
			baseName = java.net.URLEncoder.encode(baseName, "UTF-8");
			DocumentData documentData = new ByteArrayDocumentData(baseName, dt,
					bytes);
			String id = DocumentStore.instance().newId();
			String url = DocumentStore.instance().preferredUrlForContent(
					baseName, dt.getExtension(), id);
			url = Manager.instance().encodeConversationId(url, viewId);
			DocumentStore.instance().saveData(id, documentData);
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(url);
		} catch (IOException e) {
			throw new DaoException("输出Excel流错误！" + e.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void reloadCache() throws DaoException {
		if (this instanceof IAppCache) {
			exp = true;
			executeQuery();
			for (Iterator<T> iterator = expResults.iterator(); iterator
					.hasNext();) {
				T obj = iterator.next();
				((IAppCache) this).raiseRefreshEvent(obj);
			}
			exp = false;
			expResults = null;
		} else {
			throw new DaoException("未实现IExpXml接口!");
		}
	}

	/**
	 * Puts document in store and redirects
	 * 
	 * @throws JAXBException
	 * @throws ClassNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws DaoException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void xmlExp(Object objId) throws JAXBException,
			ClassNotFoundException, UnsupportedEncodingException,
			DaoException {
		if (this instanceof IImpXml) {
			T eo = entityManager.find(getEntityClass(), objId);
			if (eo == null)
				return;
			String viewId = Pages.getViewId(FacesContext.getCurrentInstance());
			String baseName = Pages.getCurrentBaseName();
			baseName = ((IExpXml) this).objInfo(eo);
			baseName = java.net.URLEncoder.encode(baseName, "UTF-8");
			JAXBContext context = JAXBContext.newInstance(getEntityClass());
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
			String url = DocumentStore.instance().preferredUrlForContent(
					baseName, "exp", id);
			url = Manager.instance().encodeConversationId(url, viewId);
			DocumentStore.instance().saveData(id, documentData);

			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(url);
			} catch (IOException e) {
				throw new MyRunException(Interpolator.instance().interpolate(
						"Could not redirect to #0", url), e);
			}
		} else {
			throw new DaoException("未实现IExpXml接口!");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void expAll() throws JAXBException, ClassNotFoundException,
			DaoException, UnsupportedEncodingException {
		if (this instanceof IExpXml) {
			exp = true;
			executeQuery();
			for (Iterator<T> iterator = expResults.iterator(); iterator
					.hasNext();) {
				T _obj = iterator.next();
				String expName = ((IExpXml) this).objInfo(_obj) + ".exp";
				expName = expName.replaceAll("%", "％");
				log.debug("expName-->{0}", expName);
				JAXBContext context = JAXBContext.newInstance(getEntityClass());
				Marshaller marshaller = context.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				StringWriter sw = new StringWriter();
				marshaller.marshal(_obj, sw);
				byte[] bytes = sw.toString().getBytes("UTF-8");
				FileUtils.writeFile(bytes, ((IExpXml) this).expPath() + "/"
						+ expName);
			}
			exp = false;
			expResults.clear();
			expResults = null;
		} else {
			throw new DaoException("未实现IExpXml接口!");
		}

	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@Transactional(TransactionPropagationType.REQUIRED)
	public void importData() throws DaoException {
		if (this instanceof IImpXml) {
			if (xmlData == null) {
				facesMessages.add(FacesMessage.SEVERITY_ERROR, "文件数据为空！");
				return;
			}
			try {
				String xml = new String(xmlData, "UTF-8");
				log.debug("importData xml->{0}", xml);
				JAXBContext jaxbContext = JAXBContext
						.newInstance(((IImpXml) this).classToBeBound());
				Unmarshaller um = jaxbContext.createUnmarshaller();
				StringReader sr = new StringReader(xml);
				T um_obj = (T) um.unmarshal(sr);
				log.debug(um_obj);
				((IImpXml) this).importObj(um_obj);
				((IImpXml) this).importedProcess(um_obj);
				facesMessages.add("【{0}】导入成功！",
						((IImpXml) this).objInfo(um_obj));
				((IImpXml) this).raiseImportEvent(um_obj);
				getEntityManager().clear();
			} catch (RunException e) {
				facesMessages.add("导入失败！【{0}】",
						e.getMessage());
				getEntityManager().clear();
			} catch (Exception e) {
				e.printStackTrace();
				throw new DaoException("导入数据失败！");
			}
		} else {
			throw new DaoException("未实现IImpXml接口!");
		}

	}

	/**
	 * 本地路径导入数据
	 * 
	 * @throws DaoException
	 */
	@SuppressWarnings({"rawtypes" })
	@Transactional(TransactionPropagationType.REQUIRED)
	public void importByPath() throws DaoException {
		if (this instanceof IImpXml) {
			String[] impFiles = FileUtils.readFolderByPath(((IImpXml) this)
					.impPath());
			if (impFiles == null) {
				facesMessages.add("【{0}】导入路径不存在！",
						((IImpXml) this).impPath());
				return;
			}
			for (String fn : impFiles) {
				if (fn != null) {
					importData(((IImpXml) this).impPath() + "/" + fn);
				}
			}
			applyFilter();
		} else {
			throw new DaoException("未实现IImpXml接口!");
		}

	}

	/**
	 * 给定文件名导入数据
	 * 
	 * @param fn
	 *            文件名
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void importData(String fn) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(((IImpXml) this)
					.classToBeBound());
			Unmarshaller um = jaxbContext.createUnmarshaller();
			File f = new File(fn);
			T um_obj = (T) um.unmarshal(f);
			log.debug(um_obj);
			((IImpXml) this).importObj(um_obj);
			facesMessages.add( "【{0}】导入成功！",
					((IImpXml) this).objInfo(um_obj));
			((IImpXml) this).raiseImportEvent(um_obj);
		} catch (RunException e) {
			facesMessages.add("文件导入错误 ！{0}【{1}】",
					fn, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			facesMessages.add( "文件导入错误 {0}！", fn);
		}
		getEntityManager().clear();// 清理实体管理器
	}

	@Override
	public void executeQueryCounts() {
	}

	private Query applyCriteriaToQuery(Query query) throws DaoException {
		processRequestParams();
		applyParmToQuery(query);
		return query;
	}

	@SuppressWarnings({ "unchecked", "rawtypes"})
	@Override
	public void executeQuery() throws DaoException {

		String nqlStr = getNqb().getQl();
		try {
			String orderBy = getOrder();
			String ql = "";
			if (StringUtils.isNotEmpty(orderBy)) {
				ql = nqlStr;
				if (StringUtils.isNotEmpty(getAuOrder())) {
				ql = nqlStr + " order by " + orderBy + " , " + getAuOrder();
			} else {
				ql = nqlStr + " order by " + orderBy;
			}
//				if (StringUtils.isNotEmpty(getAuOrder())) {
//					ql = nqlStr + " order by " + orderBy + " , " + getAuOrder();
//				} else {
//					ql = nqlStr + " order by " + orderBy + " , o.id";
//				}
			} else {
				ql = nqlStr;
			}
			log.debug("查询语句 {0}", ql);
			Query query = null;
			if (nq.isUserNativeQuery()) {
				query = applyCriteriaToQuery(entityManager
						.createNativeQuery(ql));
			} else {
				query = applyCriteriaToQuery(entityManager.createQuery(ql));
			}
			if (exp) {
				if (this instanceof IResultConvert) {
					List<Object[]> ls = query.getResultList();
					setResults(((IResultConvert) this).toResult(ls));
				} else {
					setExpResults(query.getResultList());
				}
			} else {
				if (this instanceof IResultConvert) {
					List<Object[]> ls = query.setFirstResult(getOffset())
							.setMaxResults(getPageSize()).getResultList();
					setResults(((IResultConvert) this).toResult(ls));
				} else {
					setResults(query.setFirstResult(getOffset())
							.setMaxResults(getPageSize()).getResultList());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			facesMessages.add("app.executeQuery.fail",
					"数据查询发生错误！", getQueryDesc());
		}
	}

	public void executeCountQuery() throws DaoException {
		processRequestParams();
		String ql = getNqb().getQlCount();
		try {
			log.debug("查询数量语句 {0}", ql);
			Query query = null;
			if (nq.isUserNativeQuery()) {
				query = applyCriteriaToQuery(entityManager
						.createNativeQuery(ql));
				setTotalRecordCount(Long
						.parseLong(query.getSingleResult() + ""));
			} else {
				query = applyCriteriaToQuery(entityManager.createQuery(ql));
				setTotalRecordCount(((Long) query.getSingleResult())
						.longValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
			facesMessages.add("app.countQuery.fail", "查询行数错误！", getQueryDesc());
		}
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public FacesMessages getFacesMessages() {

		return facesMessages;
	}

	@Override
	public String getPageSizeCookiesName() {
		return "pageSize";
	}

	@Override
	protected boolean isPostback() {
		return false;
	}

	@Override
	public void resetFilter() {
		getQhb().reset();
		applyFilter();
	}

	public void resetFilter(int pl) {
		getQhb().reset(pl);
		applyFilter();
	}

	public T entity(Object objId) {
		try {
			T obj = entityManager.find(getEntityClass(), objId);
			if (obj == null) {
				getFacesMessages().add("id={0}数据未找到！", objId);
			}
			return obj;
		} catch (Exception e) {
			getFacesMessages().add("获取数据失败！{0}", e.getMessage());
		}
		return null;
	}

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
				//getFacesMessages().add("id={0}删除数据成功！", eo.getId());
				getFacesMessages().add("删除数据成功！");
				refreshResults();
			} else {
				entityManager.remove(obj);
				entityManager.flush();
				getFacesMessages().add("删除数据成功！");
				refreshResults();
			}
		} catch (Exception e) {
			getFacesMessages().add("删除数据失败！存在其他数据关联！");
		}
	}

	@Transactional
	public void validItem(Object objId) {
		try {
			Object obj = entityManager.find(getEntityClass(), objId);
			if (obj == null) {
				//getFacesMessages().add("id={0}数据未找到，恢复数据失败！", objId);
				getFacesMessages().add("数据未找到，恢复数据失败！" );
				return;
			}
			if (isBaseEntity(obj)) {
				BaseEntity eo = (BaseEntity) obj;
				eo.setState(DataState.正常);
				entityManager.flush();
				//getFacesMessages().add("id={0}恢复数据成功！", eo.getId());
				getFacesMessages().add("恢复数据成功！");
				refreshResults();
			}
		} catch (Exception e) {
			getFacesMessages().add("恢复数据失败！");
		}
	}

	@Transactional
	public void invalidItem(Object objId) {
		try {
			Object obj = entityManager.find(getEntityClass(), objId);
			if (obj == null) {
				//getFacesMessages().add("id={0}数据未找到，作废数据失败！", objId);
				getFacesMessages().add("数据未找到，作废数据失败！");
				return;
			}
			if (isBaseEntity(obj)) {
				BaseEntity eo = (BaseEntity) obj;
				eo.setState(DataState.作废);
				entityManager.flush();
				//getFacesMessages().add("id={0}作废数据成功！", eo.getId());
				getFacesMessages().add("作废数据成功！");
				refreshResults();
			}
		} catch (Exception e) {
			//getFacesMessages().add("id={0}作废数据失败！", objId);
			getFacesMessages().add("作废数据失败！");
		}
	}

	public boolean isBaseEntity(Object obj) {
		Object o = HibernateUtil.relObject(obj);
		if (o instanceof BaseEntity) {
			return true;
		} else {
			return false;
		}
	}



	public byte[] getXmlData() {
		return xmlData;
	}

	public void setXmlData(byte[] xmlData) {
		this.xmlData = xmlData;
	}

	public String getXmlExpPath() {
		return xmlExpPath;
	}

	public void setXmlExpPath(String xmlExpPath) {
		this.xmlExpPath = xmlExpPath;
	}

	public String getXmlImpPath() {
		return xmlImpPath;
	}

	public void setXmlImpPath(String xmlImpPath) {
		this.xmlImpPath = xmlImpPath;
	}

}
