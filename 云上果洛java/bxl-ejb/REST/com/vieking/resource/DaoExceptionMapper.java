package com.vieking.resource;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.vieking.sys.exception.CustomException;
import com.vieking.sys.exception.DaoException;

@Provider
public class DaoExceptionMapper implements ExceptionMapper<DaoException> {

	public Response toResponse(DaoException e) {
		return Response.ok(
				new CustomException(e.getInfo(), e.getErrorgroup(), e
						.getErrorkey())).build();
	}

}
