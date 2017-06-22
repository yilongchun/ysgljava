package com.vieking.sys.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Init;
import org.jboss.seam.ui.component.html.HtmlLink;
import org.richfaces.component.html.HtmlDropDownMenu;
import org.richfaces.component.html.HtmlMenuItem;
import org.richfaces.component.html.HtmlPanelMenu;
import org.richfaces.component.html.HtmlToolBar;

import com.vieking.role.model.Application;
import com.vieking.sys.exception.Const;

@Name("vieking.ui.appMenu")
@Scope(ScopeType.EVENT)
@AutoCreate
public class AppMenu implements Serializable {

	public void setMenuBar(HtmlToolBar menuBar) {
		this.menuBar = menuBar;
	}

	public void setPanelmenu(HtmlPanelMenu panelmenu) {
		this.panelmenu = panelmenu;
	}

	private static final long serialVersionUID = -2688447300704465313L;

	@SuppressWarnings("seam-unresolved-variable")
	@In(value = Const.CURRUSERUSERAPPS, required = true)
	FacesContext context = FacesContext.getCurrentInstance();

	private HtmlDropDownMenu appMenu = new HtmlDropDownMenu();

	private HtmlToolBar menuBar = new HtmlToolBar();

	private HtmlPanelMenu panelmenu = new HtmlPanelMenu();

	@In(value = "org.jboss.seam.core.init", scope = ScopeType.APPLICATION)
	private Init init;

	/*
	 * <s:link view="/module/main.xhtml" value="模块管理" propagation="none"
	 * styleClass="menuHeaderItems"> <f:param name="from" /> </s:link>
	 */
	private HtmlLink newLink(Application app, boolean newWin) {
		HtmlLink link = new HtmlLink();
		ELContext elContext = context.getELContext();
		ExpressionFactory ef = context.getApplication().getExpressionFactory();
		Class[] c = new Class[0];
		link.setValue(app.getDes());
		javax.el.MethodExpression me = ef.createMethodExpression(elContext,
				"#{vieking.ui.nav.open('" + app.getName() + "')}", Void.TYPE, c);
		link.setActionExpression(me);
		link.setPropagation("none");
		if (newWin) {
			link.setTarget("_blank");
		}
		return link;
	}

	@SuppressWarnings("unused")
	private HtmlMenuItem newHtmlMenuItem(Application app, boolean newWin) {
		HtmlMenuItem mi = new HtmlMenuItem();
		mi.setSubmitMode("none");
		HtmlLink newLink = newLink(app, newWin);
		mi.getChildren().add(newLink);
		mi.setOnclick(newLink.getOnclick());
		return mi;
	}

	@SuppressWarnings("unused")
	private HtmlMenuItem toolBarHtmlMenuItem(Application app, boolean newWin) {
		HtmlMenuItem mi = new HtmlMenuItem();
		mi.setSubmitMode("none");
		HtmlLink newLink = newLink(app, newWin);
		newLink.setStyle("color:blue");
		mi.getChildren().add(newLink);
		mi.setOnclick(newLink.getOnclick());
		return mi;
	}

	public List<Application> apps(String mn) {
		List<Application> result = new ArrayList<Application>();
		return result;
	}

	public HtmlDropDownMenu getAppMenu() {
		appMenu.setValue("转至");
		appMenu.setStyleClass("menubarlinkbtn");
		appMenu.setStyle("color:#0078d0");
		return appMenu;
	}

	public HtmlToolBar getMenuBar() {

		return menuBar;
	}

	public HtmlPanelMenu getPanelmenu() {

		return panelmenu;
	}

	public void setAppMenu(HtmlDropDownMenu appMenu) {
		this.appMenu = appMenu;
	}

	public HtmlDropDownMenu mm(String _mn) {
		HtmlDropDownMenu moduleMenu = new HtmlDropDownMenu();
		return moduleMenu;
	}
}
