package com.vieking.jsf.fckeditor;

import java.util.HashMap;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.Tag;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.ComponentHandler;

public class EditorFaceletHandle extends ComponentHandler {
	private Map<String, String> attribute = new HashMap<String, String>();

	public EditorFaceletHandle(ComponentConfig config) {
		super(config);
		// TODO Auto-generated constructor stub
		Tag tag = config.getTag();
		TagAttribute[] tagab = tag.getAttributes().getAll();
		for (TagAttribute t : tagab) {
			// System.out.println("put key:"+t.getQName());
			// System.out.println("put value:"+t.getValue());
			attribute.put(t.getQName(), t.getValue());
		}
	}

	protected void onComponentCreated(FaceletContext ctx, UIComponent c,
			UIComponent parent) {
		UIEditor comp = (UIEditor) c;
		ExpressionFactory ef = ctx.getExpressionFactory();
		ELContext elc = ctx.getFacesContext().getELContext();
		for (Map.Entry<String, String> e : attribute.entrySet()) {
			try {

				ValueExpression ve = ef.createValueExpression(elc,
						e.getValue(), String.class);
				if (!ve.isLiteralText()) {
					// System.out.println("EL put key:"+e.getKey());
					// System.out.println("EL put value:"+ve.getValue(elc));
					c.getAttributes().put(e.getKey(), ve.getValue(elc));
					if (e.getKey().equals("value"))
						comp.setValue(ve.getValue(elc));
				} else {
					c.getAttributes().put(e.getKey(), e.getValue());
				}
			} catch (ELException ele) {
				ele.printStackTrace();
			}
		}
	}
}
