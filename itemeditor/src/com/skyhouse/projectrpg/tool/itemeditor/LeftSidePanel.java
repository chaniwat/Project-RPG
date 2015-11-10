package com.skyhouse.projectrpg.tool.itemeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.skyhouse.projectrpg.data.item.BaseItem.ItemType;

public class LeftSidePanel extends JPanel {
	private static final long serialVersionUID = -5456412333616509059L;
	
	private JTextField searchBox;
	private JList<ItemData> listComponent;
	private DefaultListModel<ItemData> listModel;
	
	public ItemData newitemdata;
	
	public LeftSidePanel() {
		super();
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(250, ItemEditor.mainwindow.getHeight()));
		
		listModel = new DefaultListModel<ItemData>();
		
		for(ItemData data : ItemEditor.itemList) {
			listModel.addElement(data);
		}
		
		searchBox = new JTextField();
		searchBox.setPreferredSize(new Dimension(getWidth(), 30));
		
		listComponent = new JList<ItemData>(listModel);
		listComponent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listComponent.setLayoutOrientation(JList.VERTICAL);
		listComponent.setVisibleRowCount(-1);
		
		JScrollPane listScroller = new JScrollPane(listComponent);
		
		JButton addButton = new JButton("Add new item");
		addButton.setPreferredSize(new Dimension(getWidth(), 30));
		
		listScroller.setPreferredSize(new Dimension(getWidth(), getHeight() - searchBox.getHeight() - addButton.getHeight()));
		
		add(searchBox, BorderLayout.NORTH);
		add(listScroller, BorderLayout.CENTER);
		add(addButton, BorderLayout.SOUTH);
		
		searchBox.getDocument().addDocumentListener(new SearchBoxListener(ItemEditor.itemList, listModel, searchBox));
		listComponent.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ItemData selectdata = listComponent.getSelectedValue();
				if(newitemdata != null) {
					ItemEditor.itemList.remove(newitemdata);
					newitemdata = null;
					refreshListModel(selectdata);
				} else {
					ItemEditor.rightsidepanel.updateComponent(selectdata);
				}
			}
		});
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ItemData data = new ItemData();
				data.itemid = 0;
				data.type = ItemType.POTION;
				data.name = "_NewItem";
				data.maxstack = 1;
				data.requirelevel = 0;
				data.description = "Description";
				data.pathtoicon = "";
				data.pathtotexture = "";
				data.heal = 0;
				data.damage = 0;
				data.defense = 0;
				searchBox.setText("");
				ItemEditor.itemList.add(data);
				refreshListModel(data);
				ItemEditor.rightsidepanel.isEdit = true;
				ItemEditor.rightsidepanel.toggleEditable();
				ItemEditor.rightsidepanel.editButton.setText("Save");
				newitemdata = data;
			}
		});
	}
	
	public void refreshListModel() {
		listModel.clear();
		String filter = searchBox.getText();
		for(ItemData data : ItemEditor.itemList) {
			if(data.name.toLowerCase().startsWith(filter.toLowerCase())) {
				listModel.addElement(data);
			}
		}
	}
	
	public void refreshListModel(ItemData selectdata) {
		refreshListModel();
		listComponent.setSelectedValue(selectdata, true);
	}
}
