package com.vieking.jsf.fckeditor;

import java.io.IOException;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;

public class EditorHTMLRenderer extends Renderer {
	public static final String TYPE = "com.vieking.jsf.fckeditor.EditorRenderer";
	private String value = "";
	private String basePath = "fckeditor";
	private String height = "300";
	private String width = "100%";

	public void decode(FacesContext context, UIComponent component) {
		Map<String, String> requestMap = context.getExternalContext()
				.getRequestParameterMap();

		String clientId = component.getClientId(context);
		UIEditor editor = (UIEditor) component;
		editor.setValue(requestMap.get(clientId).toString());
		ValueExpression ve = component.getValueExpression("value");

		editor.setSubmittedValue(requestMap.get(clientId).toString());
		ve.setValue(context.getELContext(), requestMap.get(clientId).toString());
	}

	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientid = component.getClientId(context);
		Map<String, Object> attributes = component.getAttributes();
		String contextpath = ((HttpServletRequest) context.getExternalContext()
				.getRequest()).getContextPath();
		UIInput in = (UIInput) component;

		if (in.getValue() != null)
			value = (String) in.getValue();
		if (attributes.get("basePath") != null)
			basePath = (String) attributes.get("basePath");
		if (attributes.get("height") != null)
			height = (String) attributes.get("height");
		if (attributes.get("width") != null)
			width = (String) attributes.get("width");
		writer.startElement("script", component);
		writer.writeAttribute("src", contextpath + "/" + basePath
				+ "/fckeditor.js", null);
		writer.endElement("script");
		writer.startElement("script", component);
		writer.write("var oFCKeditor = new FCKeditor('" + clientid + "');");

		writer.write("oFCKeditor.BasePath='" + contextpath + "/" + basePath
				+ "/';");

		writer.write("oFCKeditor.Height='" + height + "';");

		writer.write("oFCKeditor.Width='" + width + "';");

		writer.write("oFCKeditor.Value='" + value + "';");
		writer.write("oFCKeditor.Create();");
		writer.endElement("script");

	}
}
