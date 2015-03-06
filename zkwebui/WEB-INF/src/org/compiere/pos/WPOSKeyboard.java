/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 Adempiere, Inc. All Rights Reserved.               *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/

package org.compiere.pos;

import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.HashMap;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Borderlayout;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.event.ActionEvent;
import org.adempiere.webui.event.ActionListener;
import org.adempiere.webui.session.SessionManager;
import org.compiere.model.MPOSKey;
import org.compiere.model.MPOSKeyLayout;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.supercsv.cellprocessor.ParseInt;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.North;
import org.zkoss.zkex.zul.South;
import org.zkoss.zul.Doublebox;


/**
 *	On Screen Keyboard
 *	@author Paul Bowden
 *	Adaxa Pty Ltd
 */
public class WPOSKeyboard extends Window implements ActionListener, PosKeyListener, EventListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3296839634889851637L;

		private WPosTextField field;
		private Doublebox dfield;
		private MPOSKeyLayout keylayout;


	/**
	 * 	Constructor
	 *	@param posPanel POS Panel
	 */
	public WPOSKeyboard (WPosBasePanel posPanel, int C_POSKeyLayout_ID, WPosTextField field, String title)
	{
		this(posPanel, C_POSKeyLayout_ID);
		setTitle(title);
		setPosTextField(field);
	}
	
	public WPOSKeyboard(WPosBasePanel posPanel, int keyLayoutId) {
		super();
		keylayout = MPOSKeyLayout.get(posPanel.getCtx(), keyLayoutId);
		init( keyLayoutId );
	}
	public WPOSKeyboard(Window parent, WPosBasePanel posPanel, int keyLayoutId, WPosTextField field) {
		super();
		setPosTextField(field);
		setTitle(Msg.translate(Env.getCtx(), "M_Product_ID"));
		keylayout = MPOSKeyLayout.get(posPanel.getCtx(), keyLayoutId);
		init( keyLayoutId );
		AEnv.showCenterWindow(parent, this);
	}

	private Textbox text = new Textbox();

	private HashMap<Integer, MPOSKey> keys;
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(POSKeyboard.class);
	private WQueryProduct queryProduct;
	
	/**
	 * 	Initialize
	 * @param startText 
	 * @param POSKeyLayout_ID 
	 */
	public void init(int POSKeyLayout_ID )
	{
		Panel panel = new Panel();
		appendChild(panel);
		//	Content
		if(getWidth()!=null){
			setWidth("750px");
			setHeight("380px");
		}
		Panel mainPanel = new Panel();
		Borderlayout mainLayout = new Borderlayout();
		Grid productLayout = GridFactory.newGridLayout();
		appendChild(panel);
		//	North
		Panel northPanel = new Panel();
		mainPanel.appendChild(mainLayout);
		mainPanel.setStyle("width: 100%; height: 100%; padding: 0; margin: 0");
		mainLayout.setHeight("100%");
		mainLayout.setWidth("100%");
		Center center = new Center();
		//
		North north = new North();
		north.setStyle("border: none");
		mainLayout.appendChild(north);
		north.appendChild(northPanel);
		northPanel.appendChild(productLayout);
		productLayout.setWidth("100%");
		appendChild(mainPanel);
		Rows rows = null;
		Row row = null;
		rows = productLayout.newRows();
		row = rows.newRow();
		
		row.appendChild(text);
		
		WPosKeyPanel keys = new WPosKeyPanel(POSKeyLayout_ID, this);
		center = new Center();
		center.setStyle("border: none");
		keys.setWidth("100%");
		keys.setHeight("99%");
		
		center.appendChild(keys);
		mainLayout.appendChild(center);
		South south = new South();
		ConfirmPanel confirm = new ConfirmPanel(true, false, true, false, false, false, false);
		confirm.addActionListener(this);

		mainLayout.appendChild(south);
		Dimension buttonDim = new Dimension(50,50);
//		confirm.getResetButton().setPreferredSize(buttonDim);
//		confirm.getOKButton().setPreferredSize(buttonDim);
//		confirm.getCancelButton().setPreferredSize(buttonDim);
		south.appendChild(confirm);
//		pack();
//		setLocationByPlatform(true);
//		text.requestFocusInWindow();
		
	}	//	init
	
	/**
	 * 	Dispose - Free Resources
	 */
	public void dispos()
	{
		if (keys != null)
		{
			keys.clear();
			keys = null;
		}
		onClose();
	}	//	dispose

	/**
	 * 	Action Listener
	 *	@param e event
	 */
//	public void actionPerformed (ActionEvent e)
//	{
//		
//
//	}	//	actionPerformed
	@Override
	
	public void keyReturned(MPOSKey key) {
		
		String entry = key.getText();
		String old = text.getText();
//		text.(start, end);
		
//		int caretPos = text.getCaretPosition();
//		if ( text.getSelectedText() != null )
//			caretPos = text.getSelectionStart();
//		String head = old.substring(0, caretPos);
//		if ( text.getSelectedText() != null )
//			caretPos = text.getSelectionEnd();
		String tail = old;
		
		if ( entry != null && !entry.isEmpty() )
		{	
			if(entry.equals("-")){
				int last = text.getText().length();
				text.setText(text.getText().substring(0, last-1));
				
			}
			else if ( keylayout.getPOSKeyLayoutType().equals(MPOSKeyLayout.POSKEYLAYOUTTYPE_Keyboard))
			{
				if ( key.getText() != null )
					text.setText( tail + entry );
			}
			else if ( keylayout.getPOSKeyLayoutType().equals(MPOSKeyLayout.POSKEYLAYOUTTYPE_Numberpad))
			{
				if ( entry.equals(".") )
				{
					text.setText(tail + entry);
				}
				if ( entry.equals(",") )
				{
					text.setText(tail + entry);
				}
				else if ( entry.equals("C") )
				{
					text.setText("0");
				}
				else {
				try
				{
					int number = Integer.parseInt(entry);		// test if number
					if ( number >= 0 && number <= 9 )
					{
						text.setText(tail+number);
					}
					// greater than 9, add to existing
					else 
					{
						Boolean current = text.getValue().contains(".");
						if ( current==true )
						{
							text.setText(number+Double.parseDouble(tail)+"");
						}
						else
						{
							text.setText(""+Integer.parseInt(tail)+number);
						}
//						else if ( current instanceof Double || current instanceof BigDecimal || current instanceof Long  )
//						{
//							
//						}
//						else if ( current instanceof Integer )
//						{
//							text.setText(tail+number);
//						}
//						else {
//							System.out.println("String");
//						}
													
					}


				}
				catch (NumberFormatException e)
				{
					// ignore non-numbers
				}
				}
				
//				try {
//					text.commitEdit();
//				} catch (ParseException e) {
//					log.log(Level.FINE, "JFormattedTextField commit failed");
//				}
			}
		}
	}

	public void setPosTextField(WPosTextField posTextField) {
		
		field = posTextField;
//		text.setFormatterFactory(field.getFormatterFactory());
		text.setText(field.getText());
		text.setValue(field.getValue());
//		getContentPane().invalidate();
		
	}
	public void setPosTextField(Doublebox posTextField) {
		
		dfield = posTextField;
//		text.setFormatterFactory(field.getFormatterFactory());
		text.setText(dfield.getText());
		text.setValue(dfield.getValue().toString());
//		getContentPane().invalidate();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String action = e.getSource().toString();
		if (action == null || action.length() == 0)
			return;
		else if ( action.equals(ConfirmPanel.A_RESET))
		{
			if ( keylayout.getPOSKeyLayoutType().equals(MPOSKeyLayout.POSKEYLAYOUTTYPE_Numberpad))
				text.setText("0");
			else
				text.setText("");
//			try {
////				text.commitEdit();
//			} catch (ParseException e1) {
//				log.log(Level.FINE, "JFormattedTextField commit failed");
//			}
		}
		else if ( action.equals(ConfirmPanel.A_CANCEL))
		{
			dispos();
		}
		else if (e.getSource().equals(ConfirmPanel.A_OK))
		{
			field.setText(text.getText());
//			try {
////				field.commitEdit();
//			} catch (ParseException e1) {
//				log.log(Level.FINE, "JFormattedTextField commit failed");
//			}
			dispos();
		}
		log.info( "PosSubBasicKeys - actionPerformed: " + action);
	}

	@Override
	public void onEvent(Event e) throws Exception {
		String action = e.getTarget().getId();
		if (action == null || action.length() == 0)
			return;
		else if ( action.equals(ConfirmPanel.A_RESET))
		{
			if ( keylayout.getPOSKeyLayoutType().equals(MPOSKeyLayout.POSKEYLAYOUTTYPE_Numberpad))
				text.setText("0");
			else
				text.setText("");
//			try {
////				text.commitEdit();
//			} catch (ParseException e1) {
//				log.log(Level.FINE, "JFormattedTextField commit failed");
//			}
		}
		else if ( action.equals(ConfirmPanel.A_CANCEL))
		{
			dispos();
		}
		else if (action.equals(ConfirmPanel.A_OK))
		{
			if(dfield!=null)
				dfield.setText(text.getText());
			else 
				field.setText(text.getText());
//			try {
////				field.commitEdit();
//			} catch (ParseException e1) {
//				log.log(Level.FINE, "JFormattedTextField commit failed");
//			}
			dispos();
		}
		log.info( "PosSubBasicKeys - actionPerformed: " + action);
	}

}	//	PosSubBasicKeys
