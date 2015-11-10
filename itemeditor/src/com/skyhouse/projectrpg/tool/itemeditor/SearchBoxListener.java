package com.skyhouse.projectrpg.tool.itemeditor;

import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchBoxListener implements DocumentListener {

	private TreeSet<ItemData> oridatalist;
	private DefaultListModel<ItemData> model;
	private JTextField searchbox;
	
	public SearchBoxListener(TreeSet<ItemData> oridatalist, DefaultListModel<ItemData> model, JTextField searchbox) {
		this.oridatalist = oridatalist;
		this.model = model;
		this.searchbox = searchbox;
	}
	
	@Override
	public void changedUpdate(DocumentEvent arg0) {	}
	@Override
	public void insertUpdate(DocumentEvent arg0) { doFilter(); }
	@Override
	public void removeUpdate(DocumentEvent arg0) { doFilter(); }
	
	private void doFilter() {
		model.clear();
		String filter = searchbox.getText();
		for(ItemData data : oridatalist) {
			if(data.name.toLowerCase().startsWith(filter.toLowerCase())) {
				model.addElement(data);
			}
		}
	}

}
