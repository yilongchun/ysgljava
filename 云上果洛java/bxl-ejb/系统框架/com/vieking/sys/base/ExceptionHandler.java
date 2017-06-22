/** 
 * +---------------------------------------------------------------------------+
 * | Environmental Data Analysis System (EDAS2)                                |
 * +---------------------------------------------------------------------------+
 * | Copyright (C) 2008-2009 by Tetra Tech, Inc., http://www.tetratech.com     |
 * |                                                                           |
 * |   Contributors:  Dan Allen, Martin Hurd, Benjamin Jessup, Erik Leppo      |
 * |   Vladislav Royzman, Sunitha Sajjan, Daniel Sporea                        |
 * |   Dritan Tako, Jeff White, Liejun Wu, John Zastrow                        |
 * +---------------------------------------------------------------------------+
 * |                                                                           |
 * | This program is free software; you can redistribute it and/or             |
 * | modify it under the terms of the GNU General Public License               |
 * | as published by the Free Software Foundation; either version 2            |
 * | of the License, or (at your option) any later version.                    |
 * |                                                                           |
 * | This program is distributed in the hope that it will be useful,           |
 * | but WITHOUT ANY WARRANTY; without even the implied warranty of            |
 * | MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             |
 * | GNU General Public License for more details.                              |
 * |                                                                           |
 * | You should have received a copy of the GNU General Public License         |
 * | along with this program; if not, write to the Free Software Foundation,   |
 * | Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.           |
 * |                                                                           |
 * +---------------------------------------------------------------------------+
 *
 */

package com.vieking.sys.base;

import javax.faces.application.FacesMessage;

import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

@AutoCreate
@Name("exceptionHandler")
public class ExceptionHandler {

	@In
	protected FacesMessages facesMessages;

	@Logger
	private Log log;

	public ExceptionHandler() {

	}

	@SuppressWarnings("deprecation")
	public void handleException(Exception e, FacesMessages facesMessages) {
		if (e instanceof InvalidStateException) {
			InvalidStateException ex = (InvalidStateException) e;
			InvalidValue[] iv = ex.getInvalidValues();
			for (int i = 0; i < iv.length; i++)
				facesMessages.add(
						FacesMessage.SEVERITY_ERROR,
						buildErrorMessage(iv[i].getPropertyName(),
								iv[i].getMessage()));
		} else {
			e.printStackTrace();
			facesMessages
					.add(FacesMessage.SEVERITY_ERROR,
							"An unknown error occured while processing your request. Please try again.");
		}
	}

	private String buildErrorMessage(String property, String message) {
		StringBuilder errMsg = new StringBuilder();
		errMsg.append(property.substring(0, 1).toUpperCase());
		errMsg.append(property.substring(1));
		errMsg.append(": ");
		errMsg.append(message);
		return errMsg.toString();
	}

}
