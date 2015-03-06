package org.compiere.pos;

import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;


public class WPosPanel implements IFormController {
	private WPosBasePanel panel;
	private CustomForm form = new CustomForm();
	public WPosPanel() {
		init();
	}
	public void init() {
		panel = new WPosBasePanel();
		form.appendChild(panel);
		
	}

	@Override
	public ADForm getForm() {
		return form;
	}

}
