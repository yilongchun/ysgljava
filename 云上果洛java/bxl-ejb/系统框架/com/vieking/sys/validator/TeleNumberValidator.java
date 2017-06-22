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

@Name("teleNumberValidator")
@org.jboss.seam.annotations.faces.Validator(id = "teleNumberValidator")
@BypassInterceptors
public class TeleNumberValidator implements Validator {

	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String phone = (String) value;
		String regex = "(^\\d{3,4}\\-\\d{6,8}$)|(^\\d{3,4}\\d{6,8}$)|(^\\d{7,8}$)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phone);
		if (!matcher.find()) {
			{
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "电话号码不正确", "电话号码不正确");
				throw new ValidatorException(message);
			}
		}
	}
}
