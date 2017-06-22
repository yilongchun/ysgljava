package com.vieking.sys.ui;

import java.io.IOException;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.FaceletException;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagHandler;

/**
 * This tag handler provides a way to set a default value for a variable if one
 * is not already set. This is useful to use inside of composition templates to
 * avoid the high cost of the EL resolvers not locating a value.
 * 
 * <samp> &lt;htpl:default var="label" value="Default value goes here"/> </samp>
 */
public class VarDefaultTagHandler extends TagHandler {

	private final TagAttribute var;

	private final TagAttribute value;

	public VarDefaultTagHandler(TagConfig config) {
		super(config);
		this.var = this.getRequiredAttribute("var");
		this.value = this.getRequiredAttribute("value");
	}

	public void apply(FaceletContext ctx, UIComponent parent)
			throws IOException, FacesException, FaceletException, ELException {
		String paramStr = var.getValue(ctx);
		if (ctx.getVariableMapper().resolveVariable(paramStr) == null) {
			ValueExpression valueVe = value.getValueExpression(ctx,
					Object.class);
			ctx.getVariableMapper().setVariable(paramStr, valueVe);
		}
	}
}