package com.vieking.seam.pdf.ui;

import java.io.IOException;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.pdf.ITextUtils;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;

public class UIFont extends org.jboss.seam.pdf.ui.UIFont {
	public static final String COMPONENT_TYPE = "org.jboss.seam.pdf.ui.UIFont";

	String fontPath;

	Font font;

	@Override
	public Font getFont() {
		return font;
	}

	@Override
	public void createITextObject(FacesContext context) {
		if (StringUtils.isEmpty(fontPath)) {
			if (getEncoding() == null) {

				font = FontFactory.getFont(getName(), getSize());
			} else {
				font = FontFactory.getFont(getName(), getEncoding(),
						getEmbedded(), getSize());
			}
		} else {
			BaseFont bfont = null;
			try {
				bfont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H,
						BaseFont.NOT_EMBEDDED);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			font = new Font(bfont, getSize());
		}
		if (getStyle() != null) {
			font.setStyle(getStyle());
		}

		if (getColor() != null) {
			font.setColor(ITextUtils.colorValue(getColor()));
		}
	}

	@Override
	public void handleAdd(Object o) {
		addToITextParent(o);
	}

	public String getFontPath() {
		return fontPath;
	}

	public void setFontPath(String fontPath) {
		this.fontPath = fontPath;
	}

}
