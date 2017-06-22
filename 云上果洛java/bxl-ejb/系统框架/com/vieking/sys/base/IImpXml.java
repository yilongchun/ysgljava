package com.vieking.sys.base;

public interface IImpXml<T> {

	public String impPath();

	public String objInfo(T um_obj);

	public void importObj(T um_obj);

	/** 导入完成后处理 */
	public void importedProcess(T um_obj);

	@SuppressWarnings("rawtypes")
	public Class[] classToBeBound();

	public void raiseImportEvent(T um_obj);
}
