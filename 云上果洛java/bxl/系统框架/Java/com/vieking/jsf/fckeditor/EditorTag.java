package com.vieking.jsf.fckeditor;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

@SuppressWarnings("deprecation")
public class EditorTag extends UIComponentTag {
	private String basePath;
	private String height;
	private String width;
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getHeight() {
		return height;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getWidth() {
		return width;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String path) {
		basePath = path;
	}

	@Override
	public String getComponentType() {
		// TODO Auto-generated method stub
		return UIEditor.TYPE;
	}

	@Override
	public String getRendererType() {
		// TODO Auto-generated method stub
		return EditorHTMLRenderer.TYPE;
	}

	public void setProperties(UIComponent component) {
		super.setProperties(component);
		setStringProperty(component, "basePath", basePath);
		setStringProperty(component, "height", height);
		setStringProperty(component, "width", width);
		setStringProperty(component, "value", value);
	}

	public void setStringProperty(UIComponent component, String attrName,
			String attrValue) {
		if (attrValue == null)
			return;

		if (isValueReference(attrValue)) {
			setValueBinding(component, attrName, attrValue);

		} else {
			component.getAttributes().put(attrName, attrValue);
		}
	}

	public void setValueBinding(UIComponent component, String attrName,
			String attrValue) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ValueBinding binding = application.createValueBinding(attrValue);
		component.setValueBinding(attrName, binding);
		component.getAttributes().put(attrName, binding.getValue(context));

	}
}
