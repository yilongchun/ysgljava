package com.vieking.jsf.fckeditor;

import javax.faces.component.UIInput;

public class UIEditor extends UIInput {
	public static final String TYPE = "com.vieking.jsf.fckeditor.Editor";
	private String basePath;
	private String height;
	private String width;

	public UIEditor() {
		this.setRendererType("com.vieking.jsf.fckeditor.EditorRenderer");
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
}
