package com.vieking.sys.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("mobileNumberValidator")
@org.jboss.seam.annotations.faces.Validator(id = "mobileNumberValidator")
@BypassInterceptors
public class MobileNumberValidator implements Validator {

	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String number = (String) value;
		String regex = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(number);
		if (!matcher.find()) {
			{
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "手机号码不正确", "手机号码不正确");
				throw new ValidatorException(message);
			}
		}
	}
}
