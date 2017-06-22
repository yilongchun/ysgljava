package com.vieking.sys.base;

import java.util.List;

public interface IResultConvert<T> {
	public List<T> toResult(List<Object> ls);
}
