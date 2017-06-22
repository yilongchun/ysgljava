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

@Name("emailValidator")
@org.jboss.seam.annotations.faces.Validator(id = "emailValidator")
@BypassInterceptors
public class EmailValidator implements Validator {

	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String input = (String) value;
		String regex = "^([a-z0-9A-Z]+[-|\\._]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		if (!matcher.find()) {
			{
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "电子邮箱不正确", "电子邮箱不正确");
				throw new ValidatorException(message);
			}
		}
	}
}
