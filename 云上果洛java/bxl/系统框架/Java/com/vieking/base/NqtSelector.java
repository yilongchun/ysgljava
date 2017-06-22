package com.vieking.base;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;

import com.vieking.sys.base.BaseQueryHelp;
import com.vieking.sys.bean.QueryParamBean;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.util.ExpressUtil;
import com.vieking.sys.util.StringUtil;

/**
 * 对象选择基础封装模板类 页面导航模式
 * 
 */
@Scope(ScopeType.PAGE)
public abstract class NqtSelector<T> extends BaseSelector<T> implements
		Serializable {

	private static final long serialVersionUID = 8248273007328226876L;

	protected String tmpId;

	private QueryParamBean qpb;

	private boolean raiseEvent = true;

	private Object[] nvs;

	public void initQhb() throws DaoException {
		BaseQueryHelp _qhb = getQhb();
		if (nvs.length % 2 != 0) {
			throw new DaoException("错误，参数应为偶数！");
		}
		for (int i = 0; i < nvs.length; i = i + 2) {
			if (nvs[i] != null && nvs[i] instanceof String) {

				String _pn = (String) nvs[i];
				if (StringUtils.isEmpty(_pn))
					continue;
				QueryParamBean qpb = _qhb.parm(_pn);
				qpb.setPl(10);
				// 如果值类型为 字符串，分析是否需要计算
				Object _pv = nvs[i + 1];
				if (_pv != null && _pv instanceof String) {
					String _valStr = (String) _pv;
					if (!StringUtils.isEmpty(_valStr)) {
						if (StringUtil.hasExp(_valStr)) {
							_valStr = _valStr.replaceAll("\\^", "'");
							Object _val = ExpressUtil.elValue(_valStr);
							qpb.setValue(_val);
						} else {
							qpb.setValue(_valStr);
						}
					} else {
						qpb.setValue(null);
					}
				} else
				// 如果值类型为非字符串数据 放入参数中
				{
					qpb.setValue(_pv);
				}
			} else {
				continue;
			}
		}
		nvs = null;
	}

	public void reset() {
		tmpId = null;
	}

	// /**
	// * 打开选择对话框 全参数 加入名值对初始化 查询参数功能 _nvs：名值对参数
	// *
	// * @throws DaoException
	// */
	// public void openEnh(String tmpId, String title, int pageSize,
	// String reRender, String event, String _qhn, String _nqn,
	// boolean selectOne, Object... _nvs) throws DaoException {
	// reset();
	// super.reRender = reRender;
	// super.title = title;
	// super.event = defEvent();
	// setPageSize(pageSize);
	// this.tmpId = tmpId;
	// super.event = StringUtils.isEmpty(event) ? super.event : event;
	// super.selectOne = selectOne;
	// setQhn(_qhn);
	// setNqn(_nqn);
	// this.nvs = _nvs;
	// init();
	// }

	/**
	 * 打开选择对话框 全参数 加入名值对初始化 查询参数功能 _nvs：名值对参数
	 * 
	 * @throws DaoException
	 */
	public void openEnh(String tmpId, String title, int pageSize,
			String reRender, String event, boolean selectOne, Object... _nvs)
			throws DaoException {
		reset();
		this.tmpId = tmpId;
		super.reRender = reRender;
		super.title = title;
		super.event = defEvent();
		setPageSize(pageSize);
		super.event = StringUtils.isEmpty(event) ? super.event : event;
		super.selectOne = selectOne;
		this.nvs = _nvs;
		init();
	}

	/**
	 * 打开单选对话框 参数直接写入方式 使用指定查询Bean及命名查询 -pageSize 默认为10 -selectOne
	 * 
	 * @throws DaoException
	 */
	public void selectOneDW(String title, String reRender, QueryParamBean _qpb,
			String _qhn, String _nqn) throws DaoException {
		openDW(title, 10, reRender, _qpb, _qhn, _nqn, true);
	}

	/**
	 * 打开选择对话框 参数直接写入方式 使用指定查询Bean及命名查询 -pageSize 默认为10 -selectOne
	 * 
	 * @throws DaoException
	 */
	public void selectManyDW(String title, String reRender,
			QueryParamBean _qpb, String _qhn, String _nqn) throws DaoException {
		openDW(title, 10, reRender, _qpb, _qhn, _nqn, false);
	}

	/**
	 * 打开单选对话框 参数直接写入方式 使用默认查询Bean及命名查询 -pageSize 默认为10 -selectOne
	 * 
	 * -_nqn 命名查询名称 - _qhn 查询HelpBean名称
	 * 
	 * 
	 * @throws DaoException
	 */
	public void selectOneDW(String title, String reRender, QueryParamBean _qpb)
			throws DaoException {
		openDW(title, 10, reRender, _qpb, true);
	}

	/**
	 * 打开单选对话框 参数直接写入方式 使用默认查询Bean及命名查询 -pageSize 默认为10 -selectOne
	 * 
	 * -_nqn 命名查询名称 - _qhn 查询HelpBean名称
	 * 
	 * 
	 * @throws DaoException
	 */
	public void selectManyDW(String title, String reRender, QueryParamBean _qpb)
			throws DaoException {
		openDW(title, 10, reRender, _qpb, false);
	}

	/** 打开选择对话框 参数直接写入方式 使用默认查询Bean及命名查询 */
	public void openDW(String title, int pageSize, String reRender,
			QueryParamBean _qpb, boolean selectOne) throws DaoException {
		reset();
		if (_qpb == null) {
			throw new DaoException("接收参数 QueryParamBean 不能为空！");
		}
		super.selectOne = selectOne;
		super.reRender = reRender;
		super.title = title;
		raiseEvent = false;
		qpb = _qpb;
		setPageSize(pageSize);
		init();
	}

	/**
	 * 打开选择对话框 全参数 pl：参数级别 _qpb 直接接收参数 不再raiseEvent
	 * 
	 * @throws DaoException
	 */
	public void openDW(String title, int pageSize, String reRender,
			QueryParamBean _qpb, String _qhn, String _nqn, boolean selectOne)
			throws DaoException {
		reset();
		if (_qpb == null) {
			throw new DaoException("接收参数 QueryParamBean 不能为空！");
		}
		super.reRender = reRender;
		super.title = title;
		raiseEvent = false;
		qpb = _qpb;
		setPageSize(pageSize);
		super.selectOne = selectOne;
		setQhn(_qhn);
		setNqn(_nqn);
		init();
	}

	/**
	 * 打开单选对话框 全参数 - event -pageSize 默认为10 -selectOne
	 * 
	 * @throws DaoException
	 */
	public void selectOne(String tmpId, String title, String reRender,
			String _qhn, String _nqn) throws DaoException {
		open(tmpId, title, 10, reRender, null, _qhn, _nqn, true);
	}

	/**
	 * 打开选择对话框 全参数 - event -pageSize 默认为10 -selectOne
	 * 
	 * @throws DaoException
	 */
	public void selectMany(String tmpId, String title, String reRender,
			String _qhn, String _nqn) throws DaoException {
		open(tmpId, title, 10, reRender, null, _qhn, _nqn, false);
	}

	/**
	 * 打开单选对话框 全参数 - event -pageSize 默认为10 -selectOne
	 * 
	 * -_nqn 命名查询名称 - _qhn 查询HelpBean名称
	 * 
	 * 
	 * @throws DaoException
	 */
	public void selectOne(String tmpId, String title, int pageSize,
			String reRender) {
		open(tmpId, title, pageSize, reRender, true);
	}

	/**
	 * 打开单选对话框 全参数 - event -pageSize 默认为10 -selectOne
	 * 
	 * -_nqn 命名查询名称 - _qhn 查询HelpBean名称
	 * 
	 * 
	 * @throws DaoException
	 */
	public void selectMany(String tmpId, String title, int pageSize,
			String reRender) {
		open(tmpId, title, pageSize, reRender, false);
	}

	/** 打开选择对话框 */
	public void open(String tmpId, String title, int pageSize, String reRender,
			boolean selectOne) {

		reset();
		super.selectOne = selectOne;
		super.reRender = reRender;
		super.title = title;
		super.event = StringUtils.isEmpty(super.event) ? defEvent()
				: super.event;
		setPageSize(pageSize);
		this.tmpId = tmpId;
		init();
	}

	/**
	 * 打开选择对话框 全参数 - event
	 * 
	 * @throws DaoException
	 */
	public void open(String tmpId, String title, int pageSize, String reRender,
			String _qhn, String _nqn, boolean selectOne) throws DaoException {
		open(tmpId, title, pageSize, reRender, null, _qhn, _nqn, selectOne);
	}

	/**
	 * 打开选择对话框 全参数 - event -pageSize 默认为10
	 * 
	 * @throws DaoException
	 */
	public void open(String tmpId, String title, String reRender, String _qhn,
			String _nqn, boolean selectOne) throws DaoException {
		open(tmpId, title, 10, reRender, null, _qhn, _nqn, selectOne);
	}

	/**
	 * 打开选择对话框 全参数 pl：参数级别
	 * 
	 * @throws DaoException
	 */
	public void open(String tmpId, String title, int pageSize, String reRender,
			String event, String _qhn, String _nqn, boolean selectOne)
			throws DaoException {
		reset();
		super.reRender = reRender;
		super.title = title;
		super.event = defEvent();
		setPageSize(pageSize);
		this.tmpId = tmpId;
		super.event = StringUtils.isEmpty(event) ? super.event : event;
		super.selectOne = selectOne;
		setQhn(_qhn);
		setNqn(_nqn);
		init();
	}

	public abstract String defEvent();

	public void init() {
		if (nvs != null && nvs.length > 0) {
			try {
				initQhb();
			} catch (DaoException e) {
				facesMessages.add("初始化initQhb查询参数错误！【{0}】", e.getMessage());
			}
		}
		super.find();
		super.initSelectableItems();
		if (popMode) {
			setOpen(true);
		}
	}

	/** 基类回调方法 返回页长度 cookesName */
	@Override
	public String getPageSizeCookiesName() {
		return "selector_PageSize";
	}

	/**
	 * 选择结束
	 * 
	 * @return，选择结束后需要返回的viewId
	 */
	@Override
	public String ok() {
		if (!popMode) {
			if (ret == null || ret.length() < 1) {
				return "/home.xhtml";
			}
			submited = true;
			return ret;
		} else {
			if (raiseEvent) {
				Events.instance().raiseEvent(getEvent(), tmpId,
						getSelectedList());
			} else {
				if (qpb != null) {
					if (selectOne) {
						qpb.setValue(getSelectedList().get(0));
					} else {
						qpb.setValue(getSelectedList());
					}
				}
			}
			super.setOpen(false);
			emptySelected();
			return null;
		}
	}

	@Override
	public BaseQueryHelp queryHelpInstance() {
		return new BaseQueryHelp();
	}

}
