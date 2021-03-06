package com.vieking.functions.manager;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import com.vieking.basicdata.model.Department;
import com.vieking.basicdata.model.Dictionary;
import com.vieking.functions.model.Contact;
import com.vieking.functions.model.ContactPost;
import com.vieking.sys.base.BaseNqtQuery;
import com.vieking.sys.base.BaseQueryHelp;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.exception.RunException;
import com.vieking.sys.tree.ContactTree;
import com.vieking.sys.utils.ExcelUtils;

@Name("fun.contactQuery")
@Scope(ScopeType.PAGE)
public class ContactQuery extends BaseNqtQuery<Contact> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1874091927872212610L;
	@In
	protected EntityManager entityManager;
	
	/** 部门树 */
	protected ContactTree bumenTree = new ContactTree();
	public ContactTree getBumenTree() {
		return bumenTree;
	}
	public void setBumenTree(ContactTree bumenTree) {
		this.bumenTree = bumenTree;
	}
	@Override
	public void create() {
		super.create();
		bumenTree.setRootCode("01");
	}
	
	// 部门
	public Department dict;
	public Department getDict() {
		return dict;
	}
	public void setDict(Department dict) {
		this.dict = dict;
	}
	
	/** 部门code */
	public String bmCode;
	public String getBmCode() {
		return bmCode;
	}
	public void setBmCode(String bmCode) {
		this.bmCode = bmCode;
	}

	/** 选择部门 */
	public void selBuMen() {
		if (bmCode != null) {
			dict = entityManager.find(Department.class, bmCode);
		}
		bmCode = null;
	}
	
	protected byte[] excelData;
	public byte[] getExcelData() {
		return excelData;
	}
	public void setExcelData(byte[] excelData) {
		this.excelData = excelData;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@Transactional(TransactionPropagationType.REQUIRED)
	public void importData() throws DaoException {
		
		if (dict == null) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, "请选择部门！");
			return;
		}
		System.out.println(dict.getCode());
		if (excelData == null) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, "请选择文件！");
			return;
		}
		int insertIndex = 0;
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(excelData);
			
			List<String[]> list = ExcelUtils.readXlsx(is);
			
			for (int i = 0; i < list.size(); i++) {
				String[] strArr = list.get(i);
				
				String name = strArr[0];
                String phone = strArr[1];
//                String sex = strArr[2];
//	                String bm = strArr[3];
                String bm = dict.getCode();
                String level = strArr[4];
                String telephone = strArr[5];
                String post = strArr[6];
                String email = strArr[7];
                String jianjie = strArr[8];
                
                if (!phone.trim().equals("")) {
                	Contact o = new Contact();
    				o.setName(name);
    				o.setPhone(phone);
    				o.setTelephone(telephone);
    				o.setPost(post);
    				o.setEmail(email);
    				o.setJianjie(jianjie);
    				o.setUser(currUser);
    				o.setCjsj(Calendar.getInstance());
    				
    				level = "D";
    				
    				Dictionary levelD = dictionaryDao.dictionsByName(level);
    				o.setLevel(levelD);
    				
    				entityManager.persist(o);
    				
    				
    				if (bm.contains("|")) {
    					String[] bms = bm.split("\\|");
    					for (int j = 0; j < bms.length; j++) {
    						String bmstr = bms[j];
    						if (!bmstr.equals("")) {
    							Department depart = entityManager.find(Department.class, bmstr);
    							ContactPost cp = new ContactPost();
    							cp.setContact(o);
    							cp.setLxrbm(depart);
    							entityManager.persist(cp);
    							entityManager.flush();
    						}
    					}
    				}else{
    					Department depart = entityManager.find(Department.class, bm);
    					ContactPost cp = new ContactPost();
    					cp.setContact(o);
    					cp.setLxrbm(depart);
    					entityManager.persist(cp);
    					entityManager.flush();
    				}
    				entityManager.flush();
    				System.out.println(i + " " +  o.getName() + "\t\t" + o.getPhone());	
    				insertIndex ++;
				}
			}
			
			facesMessages.add(FacesMessage.SEVERITY_INFO, "导入成功"+ (insertIndex) + "条数据");
			getEntityManager().clear();
//				String xml = new String(xmlData, "UTF-8");
//				log.debug("importData xml->{0}", xml);
//				JAXBContext jaxbContext = JAXBContext
//						.newInstance(((IImpXml) this).classToBeBound());
//				Unmarshaller um = jaxbContext.createUnmarshaller();
//				StringReader sr = new StringReader(xml);
//				T um_obj = (T) um.unmarshal(sr);
//				log.debug(um_obj);
//				((IImpXml) this).importObj(um_obj);
//				((IImpXml) this).importedProcess(um_obj);
//				facesMessages.add("【{0}】导入成功！",
//						((IImpXml) this).objInfo(um_obj));
//				((IImpXml) this).raiseImportEvent(um_obj);
//				getEntityManager().clear();
		} catch (RunException e) {
			facesMessages.add("导入失败！【{0}】",
					e.getMessage());
			getEntityManager().clear();
		}catch (Exception e) {
//				e.printStackTrace();
			
			String msg = "导入失败";
			
			if (e instanceof NullPointerException) {
				facesMessages.add(FacesMessage.SEVERITY_ERROR, "数据异常");
				return;
			}
			if (e instanceof PersistenceException) {
				String message = e.getCause().getCause().getMessage();
				if (message.contains("'")) {
					int start = message.indexOf("'");
					String pho = message.substring(start+1, start + 12);
					msg = "导入失败，已存在手机号码:" + pho + ",序号:"+(insertIndex+2);
				}
				facesMessages.add(FacesMessage.SEVERITY_INFO, msg);
				return;
			}
			facesMessages.add(FacesMessage.SEVERITY_INFO, msg);
			return;
		}
	}

	public void processRequestParams() {

	}

	@Override
	public void executeQueryCounts() {
	}

	public String getQueryDesc() {
		return "通讯录管理";
	}

	public ContactQuery() {
		setOrder("o.ct desc ");
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

	@Override
	public String queryHelpName() {
		return "qhb.fun.contactQuery";
	}

	@Override
	public String nameQueryName() {
		return "fun.contactQuery";
	}

}
