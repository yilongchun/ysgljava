package com.vieking.sys.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.vieking.sys.enumerable.DataType;
import com.vieking.sys.util.StringUtil;

/**
 * <br>
 * <br>
 * QueryParam 查询参数bean
 * <p>
 * <a href="QueryParamBean.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
public class QueryParamBean implements Serializable {
	private static final long serialVersionUID = 555713717010156165L;

	/** 参数名称 */
	private String pn;

	/** 文本值 */
	private String sv;

	/** 整形值 */
	private Integer iv;

	/** 长整形值 */
	private Long lv;

	/** 浮点值 */
	private Double dv;

	/** 日期值 */
	private Calendar cv;

	/** Boolean 类型 */
	private Boolean bv;

	/** 文本值 */
	private DataType dt;

	/** 其他类型 */
	private Object obj;

	private Object value;

	/** 文本值 */
	private List<String> sls = new ArrayList<String>();

	/** 参数级别 当 清理参数时 清理低于指定级别参数 */
	private int pl = 0;

	private String des;

	public boolean isDataNull() {
		if (dv == null && sv == null && iv == null && lv == null && bv == null
				&& cv == null && obj == null && (sls == null || sls.isEmpty())) {
			return true;
		} else {
			return false;
		}
	}

	public String strValue() {
		return StringUtil.toStr(value, "未设置");
	}

	/***
	 * 获取文档字符值 用于打印
	 * 
	 */
	public Object getValue() {
		// 浮点型
		if (DataType.DT001.equals(dt)) {
			return getDv();
		}
		// 整形
		if (DataType.DT002.equals(dt)) {
			return getIv();
		}
		// 长整形
		if (DataType.DT008.equals(dt)) {
			return getLv();
		}
		// 字符串
		if (DataType.DT003.equals(dt)) {
			return getSv();
		}
		// 日期
		if (DataType.DT004.equals(dt)) {
			return getCv();
		}
		// Boolean
		if (DataType.DT006.equals(dt)) {
			return getBv();
		}
		// DT100 单选
		if (DataType.DT100.equals(dt)) {
			return getSv();
		}
		// DT110 多选字符串
		if (DataType.DT110.equals(dt)) {
			if (sls.isEmpty()) {
				List<String> _sls = new ArrayList<String>();
				_sls.add("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				return _sls;
			} else {
				return getSls();
			}
		}
		// 对象 枚举 实体
		if (DataType.DT800.equals(dt)) {
			return getObj();
		}
		return null;
	}

	public String getSv() {
		return sv;
	}

	public void setSv(String sv) {
		this.sv = StringUtils.trim(sv);
		this.value = sv;
	}

	public Integer getIv() {
		return iv;
	}

	public void setIv(Integer iv) {
		this.iv = iv;
		this.value = iv;
	}

	public Double getDv() {
		return dv;
	}

	public void setDv(Double dv) {
		this.dv = dv;
		this.value = dv;
	}

	public Calendar getCv() {
		return cv;
	}

	public void setCv(Calendar cv) {
		this.cv = cv;
		this.value = cv;
	}

	public Boolean getBv() {
		return bv;
	}

	public void setBv(Boolean bv) {
		this.bv = bv;
		this.value = bv;
	}

	public DataType getDt() {
		return dt;
	}

	public void setDt(DataType dt) {
		this.dt = dt;
	}

	public List<String> getSls() {
		return sls;
	}

	public void setSls(List<String> sls) {
		this.sls = sls;
		this.value = sls;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		// if (obj == null && this.obj != null)
		// {
		// Events.instance().raiseEvent("NameQueryBeanReSet");
		// }
		// if (!obj.equals(this.obj))
		// {
		// Events.instance().raiseEvent("NameQueryBeanReSet");
		// }
		this.obj = obj;
		this.value = obj;
	}

	@Override
	public String toString() {
		return "QueryParamBean [value=" + String.valueOf(getValue()) + "]";
	}

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = StringUtils.trim(pn);
	}

	public void setNull() {
		setDv(null);
		setCv(null);
		setBv(null);
		setSv(null);
		setIv(null);
		setObj(null);
		setSls(null);
		setLv(null);
		this.value = null;
	}

	public void setValue(Object value) {
		if (value == null) {
			setNull();
			return;
		}
		if (value instanceof Double) {
			setDt(DataType.DT001);
			setDv((Double) value);
			return;
		}
		if (value instanceof Calendar) {
			setDt(DataType.DT004);
			setCv((Calendar) value);
			return;
		}
		if (value instanceof Boolean) {
			setDt(DataType.DT006);
			setBv((Boolean) value);
			return;
		}

		if (value instanceof String) {
			setDt(DataType.DT003);
			setSv((String) value);
			return;
		}
		if (value instanceof Integer) {
			setDt(DataType.DT002);
			setIv((Integer) value);
			return;
		}
		if (value instanceof Long) {
			setDt(DataType.DT008);
			setLv((Long) value);
			return;
		}
		setDt(DataType.DT800);
		setObj(value);
	}

	public QueryParamBean(String pn, Object value) {
		// DT001("浮点型"), DT002("整形"), DT003("字符串"), DT004("日期"), DT005("lob"),
		// DT006("布尔型"), DT100("单选"), DT110(
		// "多选"), DT200("子文档"), DT800("对象"), DT900("实体");
		super();
		setPn(pn);
		if (value == null)
			return;
		setValue(value);
	}

	public QueryParamBean() {
		super();
	}

	public int getPl() {
		return pl;
	}

	public void setPl(int pl) {
		this.pl = pl;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pn == null) ? 0 : pn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryParamBean other = (QueryParamBean) obj;
		if (pn == null) {
			if (other.pn != null)
				return false;
		} else if (!pn.equals(other.pn))
			return false;
		return true;
	}

	public Long getLv() {
		return lv;
	}

	public void setLv(Long lv) {
		this.lv = lv;
		this.value = iv;
	}

}
