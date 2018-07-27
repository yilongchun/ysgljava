package com.vieking.resource.dao;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;

import com.vieking.basicdata.model.Dictionary;
import com.vieking.file.dao.DocInfoDao;
import com.vieking.file.dao.DocLinkDao;
import com.vieking.file.enumerable.UrlType;
import com.vieking.file.model.DocInfo;
import com.vieking.file.model.DocLink;
import com.vieking.file.model.DocType;
import com.vieking.functions.model.Contact;
import com.vieking.role.model.Assistance;
import com.vieking.role.model.User;
import com.vieking.sys.base.BaseDaoHibernate;
import com.vieking.sys.enumerable.RegistrationState;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.exception.KeException;
import com.vieking.sys.service.FileManager;
import com.vieking.sys.util.Config;
import com.vieking.sys.util.FileUtils;

@Name("fileDao")
@AutoCreate
public class FileDao extends BaseDaoHibernate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2518696153043452260L;

	@In(value = "sys.dao.docInfo")
	private DocInfoDao docInfoDao;

	@In(value = "sys.dao.docLink")
	private DocLinkDao docLinkDao;

	@In(value = "fileManager")
	private FileManager fileManager;

	@In(value = "app.config")
	private Config config;

	@SuppressWarnings("unchecked")
	public DocLink getFileLink(String id, String name) {
		DocLink d = null;
		List<DocLink> list = entityManager
				.createQuery(
						"from DocLink o where  o.document.docType.name=:name  and o.keyValue=:keyValue ")
				.setParameter("name", name).setParameter("keyValue", id)
				.getResultList();
		if (!list.isEmpty())
			d = list.get(0);
		return d;
	}

	public String getFileUrl(String id) {
		String result = "";
		if (id == null || "".equals(id))
			return result;
		try {
			result = config.get("webUrl")
					+ "/common/docInfo/downLoad.htm?fileId=" + id;
		} catch (KeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<DocLink> getFileLinks(String id, String name) {
		return entityManager
				.createQuery(
						"from DocLink o where  o.document.docType.name=:name  and o.keyValue=:keyValue ")
				.setParameter("name", name).setParameter("keyValue", id)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DocLink> getFileLinks(String id) {
		return entityManager
				.createQuery("from DocLink o where  o.keyValue=:keyValue ")
				.setParameter("keyValue", id).getResultList();
	}

	/** 保存上传的附件 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void saveFile(String userId, String oid, String type,
			String fileName, byte[] in2b) {
		try {
			String doctype = "";
			String modelName = "";
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("JSZZ", 1);
			map.put("JSZF", 2);
			map.put("XCZ", 3);
			map.put("XCZF", 4);
			map.put("XCZFJ", 5);
			map.put("SFZ", 6);
			map.put("GRZP", 7);
			switch (map.get(type)) {
			case 1:
				doctype = "JSZZ";
				modelName = "com.keyer.employee.model.Driver";
				break;
			case 2:
				doctype = "JSZF";
				modelName = "com.keyer.employee.model.Driver";
				break;
			case 3:
				doctype = "XCZ";
				modelName = "com.keyer.car.model.Car";
				break;
			case 4:
				doctype = "XCZF";
				modelName = "com.keyer.car.model.Car";
				break;
			case 5:
				doctype = "XCZFJ";
				modelName = "com.keyer.car.model.Car";
				break;
			case 6:
				doctype = "SFZ";
				modelName = "com.keyer.employee.model.Driver";
				break;
			case 7:
				doctype = "GRZP";
				modelName = "com.keyer.employee.model.Driver";
				break;
			default:
				break;
			}
			DocInfo di = new DocInfo();
			DocType dt = entityManager.find(DocType.class, doctype);
			di.setDocType(dt);
			di.setOriginalName(fileName);
			di.setSize(in2b.length);
			di.setDes(fileName);
			di.setLmOid(userId);
			di.setLmOname(type);
			String newPath = generateNewPath(di);
			di.setUrlName(newPath);
			di.setUrlType(UrlType.FILE);
			if (!di.isJpgImage()) {
				di.setProcessed(true);
			} else {
				di.setProcessed(false);
			}
			docInfoDao.save(di);
			FileUtils.writeFile(in2b, fileManager.getDestFileName(newPath + "/"
					+ fileName, true, true));
			List<DocLink> list = entityManager
					.createQuery(
							" from DocLink o where o.document.docType.name =:doctype and o.ebcn=:modelName and o.keyValue=:oid")
					.setParameter("doctype", doctype)
					.setParameter("modelName", modelName)
					.setParameter("oid", oid).getResultList();
			DocLink dl = null;
			if (list.isEmpty()) {
				dl = new DocLink(di, modelName, "id", oid);
			} else {
				dl = list.get(0);
				dl.setDocument(di);
				dl.setEbcn(modelName);
				dl.setKeyValue(oid);
				log.debug("第二次上传................{0}", modelName);
			}
			docLinkDao.addLinks(dl);
		} catch (DaoException e) {
			e.printStackTrace();
		}
	}

	/** 保存上传的附件 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String saveContactImg(String userId, String type, String fileName,
			byte[] in2b) {
		User u = entityManager.find(User.class, userId);
		Contact c = (Contact) entityManager
				.createQuery(" from Contact o where o.phone =:phone")
				.setParameter("phone", u.getPhone()).getResultList().get(0);
		String doctype = "";
		String modelName = "";
		String fileUrl = "";
		String webUrl = "";
		try {
			webUrl = config.get("webUrl");
		} catch (KeException e) {
			e.printStackTrace();
		}
		try {

			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("TX", 1);
			switch (map.get(type)) {
			case 1:
				doctype = "TX";
				modelName = "com.vieking.functions.model.Contact";
				break;
			default:
				break;
			}
			fileName = FileUtils.generateRandomFilename()
					+ fileName.substring(fileName.lastIndexOf("."));
			DocInfo di = new DocInfo();
			DocType dt = entityManager.find(DocType.class, doctype);
			di.setDocType(dt);
			di.setOriginalName(fileName);
			di.setSize(in2b.length);
			di.setDes(fileName);
			di.setLmOid(userId);
			di.setLmOname(type);
			String newPath = generateNewPath(di);
			newPath = "null/image";
			di.setUrlName(newPath);

			di.setUrlType(UrlType.FILE);
			if (!di.isJpgImage()) {
				di.setProcessed(true);
			} else {
				di.setProcessed(false);
			}
			docInfoDao.save(di);
			FileUtils.writeFile(in2b, fileManager.getDestFileName(newPath + "/"
					+ fileName, true, true));
			List<DocLink> list = entityManager
					.createQuery(
							" from DocLink o where o.document.docType.name =:doctype and o.ebcn=:modelName and o.keyValue=:oid")
					.setParameter("doctype", doctype)
					.setParameter("modelName", modelName)
					.setParameter("oid", c.getId()).getResultList();
			DocLink dl = null;
			if (list.isEmpty()) {
				dl = new DocLink(di, modelName, "id", c.getId());
			} else {
				dl = list.get(0);
				dl.setDocument(di);
				dl.setEbcn(modelName);
				dl.setKeyValue(c.getId());
			}
			docLinkDao.addLinks(dl);

			fileUrl = webUrl + newPath + "/" + fileName;
		} catch (DaoException e) {
			e.printStackTrace();
		}
		// return getFileUrl(getFileLink(c.getId(), doctype) == null ? ""
		// : getFileLink(c.getId(), doctype).getDocument().getId());
		return fileUrl;

	}

	/** 保存上传的附件 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String saveContactImg(String userId, String type, String fileName,
			int begin, int end, byte[] in2b) {
		User u = entityManager.find(User.class, userId);
		Contact c = (Contact) entityManager
				.createQuery(" from Contact o where o.phone =:phone")
				.setParameter("phone", u.getPhone()).getResultList().get(0);
		String doctype = "";
		String modelName = "";
		String fileUrl = "";
		String webUrl = "";
		try {
			webUrl = config.get("webUrl");
		} catch (KeException e) {
			e.printStackTrace();
		}
		try {

			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("TX", 1);
			switch (map.get(type)) {
			case 1:
				doctype = "TX";
				modelName = "com.vieking.functions.model.Contact";
				break;
			default:
				break;
			}
			fileName = FileUtils.generateRandomFilename()
					+ fileName.substring(fileName.lastIndexOf("."));
			DocInfo di = new DocInfo();
			DocType dt = entityManager.find(DocType.class, doctype);
			di.setDocType(dt);
			di.setOriginalName(fileName);
			di.setSize(in2b.length);
			di.setDes(fileName);
			di.setLmOid(userId);
			di.setLmOname(type);
			String newPath = generateNewPath(di);
			newPath = "null/image";
			di.setUrlName(newPath);
			di.setUrlType(UrlType.FILE);
			if (!di.isJpgImage()) {
				di.setProcessed(true);
			} else {
				di.setProcessed(false);
			}
			docInfoDao.save(di);
			FileUtils.writeFile(in2b, begin, end, fileManager.getDestFileName(
					newPath + "/" + fileName, true, true));
			List<DocLink> list = entityManager
					.createQuery(
							" from DocLink o where o.document.docType.name =:doctype and o.ebcn=:modelName and o.keyValue=:oid")
					.setParameter("doctype", doctype)
					.setParameter("modelName", modelName)
					.setParameter("oid", c.getId()).getResultList();
			DocLink dl = null;
			if (list.isEmpty()) {
				dl = new DocLink(di, modelName, "id", c.getId());
			} else {
				dl = list.get(0);
				dl.setDocument(di);
				dl.setEbcn(modelName);
				dl.setKeyValue(c.getId());
			}
			docLinkDao.addLinks(dl);

			fileUrl = webUrl + newPath + "/" + fileName;
		} catch (DaoException e) {
			e.printStackTrace();
		}

		return fileUrl;

		// return getFileUrl(getFileLink(c.getId(), doctype) == null ? ""
		// : getFileLink(c.getId(), doctype).getDocument().getId());
	}

	/** 保存报料信息 */
	@Transactional
	public String saveBaoLiao(String userId, String type, String bllx,String biaoti,
			String info, String fileName, String blid, int begin, int end,
			byte[] in2b) {
		String result = blid;
		try {
			User u = entityManager.find(User.class, userId);
			String fjlx = "FJLX02";

			if (result == null || "".equals(result)) {
				Assistance assistance = new Assistance();
				assistance.setBzlx(entityManager.find(Dictionary.class, bllx));
				assistance.setUser(u);
				assistance.setNamea(u.getName());
				assistance.setNerong(info);
				assistance.setFbsj(Calendar.getInstance());
				assistance.setBiaoti(biaoti);

				if ("SP".equals(type)) {
					fjlx = "FJLX04";
				} else if ("YP".equals(type)) {
					fjlx = "FJLX03";
				} else if ("TP".equals(type)) {
					fjlx = "FJLX02";
				}

				assistance.setFjlx(entityManager.find(Dictionary.class, fjlx));
				assistance.setRegState(RegistrationState.待审核);
				entityManager.persist(assistance);
				entityManager.flush();
				result = assistance.getId();
			}

			String modelName = "com.vieking.role.model.Assistance";
			// String fileUrl = "";
//			String webUrl = "";
			if (!"FJLX01".equals(fjlx)) {

//				try {
//					webUrl = config.get("webUrl");
//				} catch (KeException e) {
//					e.printStackTrace();
//				}
				try {
					fileName = FileUtils.generateRandomFilename()
							+ fileName.substring(fileName.lastIndexOf("."));
					DocInfo di = new DocInfo();
					DocType dt = entityManager.find(DocType.class, type);
					di.setDocType(dt);
					di.setOriginalName(fileName);
					di.setSize(in2b.length);
					di.setDes(fileName);
					di.setLmOid(userId);
					di.setLmOname(type);
					String newPath = generateNewPath(di);
					if ("SP".equals(type)) {
						newPath = "null/video";
					} else if ("TP".equals(type)) {
						newPath = "null/image";
					}

					di.setUrlName(newPath);
					di.setUrlType(UrlType.FILE);
					if (!di.isJpgImage()) {
						di.setProcessed(true);
					} else {
						di.setProcessed(false);
					}
					docInfoDao.save(di);
					FileUtils.writeFile(
							in2b,
							begin,
							end,
							fileManager.getDestFileName(newPath + "/"
									+ fileName, true, true));
					DocLink dl = new DocLink(di, modelName, "id", result);

					// List<DocLink> list = entityManager
					// .createQuery(
					// " from DocLink o where o.document.docType.name =:doctype and o.ebcn=:modelName and o.keyValue=:oid")
					// .setParameter("doctype", type)
					// .setParameter("modelName", modelName)
					// .setParameter("oid", result).getResultList();
					// DocLink dl = null;
					// if (list.isEmpty()) {
					// dl = new DocLink(di, modelName, "id", result);
					// } else {
					// dl = list.get(0);
					// dl.setDocument(di);
					// dl.setEbcn(modelName);
					// dl.setKeyValue(result);
					// }
					docLinkDao.addLinks(dl);

					// fileUrl = webUrl + newPath + "/" + fileName;
				} catch (DaoException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			return "";
		}

		return result;

		// return getFileUrl(getFileLink(c.getId(), doctype) == null ? ""
		// : getFileLink(c.getId(), doctype).getDocument().getId());
	}
 

	private String generateNewPath(DocInfo di) {
		String newPath = di.getLmOid() + "/" + di.getDocType().getPath() + "/"
				+ FileUtils.generateRandomFilename();
		return newPath;
	}

}
