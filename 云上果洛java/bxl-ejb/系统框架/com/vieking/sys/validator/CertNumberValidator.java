package com.vieking.sys.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Events;

import com.vieking.sys.util.IDCardNumUtil;

@Name("certNumberValidator")
@org.jboss.seam.annotations.faces.Validator(id = "certNumberValidator")
@BypassInterceptors
public class CertNumberValidator implements Validator {

	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String IDCardNum = value.toString();
		if (!"123456789123456".equals(IDCardNum)) {
			String result;
			result = IDCardNumUtil.validate(IDCardNum);
			// 通过效验码检验身份证的合法性
			if (result != null) {
				Events.instance().raiseEvent("身份证号无效", IDCardNum);
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "身份证号无效", result);
				throw new ValidatorException(message);
			}
		}
	}

}
