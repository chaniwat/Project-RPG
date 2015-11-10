package com.skyhouse.projectrpg.tool.itemeditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.skyhouse.projectrpg.data.item.BaseItem.ItemType;

import layout.SpringUtilities;

public class RightSidePanel extends JPanel {
	private static final long serialVersionUID = 715484521847657349L;
	
	public HashMap<String, JTextField> component;
	public JComboBox<String> combocomponent;
	
	public ItemData lastdata;
	
	public boolean isEdit = false;
	public JButton editButton;
	
	private String[] textboxname = new String[] {"id", "type", "name", "maxstack", "requirelevel", "description", "pathtoicon", "pathtotexture", "heal", "damage", "defense"};

	public RightSidePanel() {
		super();
		component = new HashMap<String, JTextField>();
		
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		setPreferredSize(new Dimension(ItemEditor.mainwindow.getWidth() - 250, ItemEditor.mainwindow.getHeight()));
		
		for(int i = 0; i < textboxname.length; i++) {
			if(i == 1) {
				addComponent(textboxname[i], true);				
			} else {
				addComponent(textboxname[i], false);
			}
		}
		
		JLabel mocklabel = new JLabel("");
		add(mocklabel);
		
		editButton = new JButton("Edit");
		add(editButton);
		
		SpringUtilities.makeCompactGrid(this, 12, 2, 6, 6, 6, 6);
		
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(component.get("id").getText().equals("")) {
					JOptionPane.showMessageDialog(ItemEditor.mainwindow,
						    "Please select item first",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(!isEdit) {
					isEdit = true;
					toggleEditable();
					editButton.setText("Save");
				} else {
					isEdit = false;
					ItemData data = updateData();
					toggleEditable();
					ItemEditor.leftsidepanel.refreshListModel(data);
					editButton.setText("Edit");
				}
			}
		});
		
	}
	
	public void addComponent(String name, boolean isType) {
		add(new JLabel(name));
		if(isType) {
			String[] type = new String[]{ "", "POTION", "QUEST", "WEAPON", "OFFHAND", "HELMET", "ARMOR", "PANT", "GLOVE", "BOOT", "NECK", "EAR", "RING", "SWORD", "BOW", "STAFF", "SHIELD", "QUIVER", "BOOK"};
			combocomponent = new JComboBox<String>(type);
			combocomponent.setEnabled(false);
			add(combocomponent);
		} else {
			JTextField field = new JTextField("");
			field.setEditable(false);
			component.put(name, field);
			add(field);
		}
	}
	
	public void updateComponent(ItemData data) {
		lastdata = data;
		
		for(int i = 0; i < textboxname.length; i++) {
			if(i == 1) {
				combocomponent.setEnabled(false);
			} else {
				component.get(textboxname[i]).setEditable(false);
			}
		}
		
		editButton.setText("Edit");
		
		if(data == null) {
			component.get("id").setText("");
			combocomponent.setSelectedIndex(0);
			component.get("name").setText("");
			component.get("maxstack").setText("");
			component.get("requirelevel").setText("");
			component.get("description").setText("");
			component.get("pathtoicon").setText("");
			component.get("pathtotexture").setText("");
			component.get("heal").setText("");
			component.get("damage").setText("");
			component.get("defense").setText("");
		} else {
			component.get("id").setText(String.valueOf(data.itemid));
			combocomponent.setSelectedItem(data.type.name());
			component.get("name").setText(String.valueOf(data.name));
			component.get("maxstack").setText(String.valueOf(data.maxstack));
			component.get("requirelevel").setText(String.valueOf(data.requirelevel));
			component.get("description").setText(String.valueOf(data.description));
			component.get("pathtoicon").setText(String.valueOf(data.pathtoicon));
			component.get("pathtotexture").setText(String.valueOf(data.pathtotexture));
			component.get("heal").setText(String.valueOf(data.heal));
			component.get("damage").setText(String.valueOf(data.damage));
			component.get("defense").setText(String.valueOf(data.defense));			
		}
	}
	
	public void toggleEditable() {
		if(isEdit) {
			for(int i = 0; i < textboxname.length; i++) {
				if(i == 0) {
					continue;
				} else if(i == 1) {
					combocomponent.setEnabled(true);
				} else {
					component.get(textboxname[i]).setEditable(true);
				}
				component.get("pathtoicon").setText(component.get("pathtoicon").getText().replace(ItemEditor.WORKINGDIR.replace("itemeditor\\", "").replace("\\", "/") + "core/assets/texture/item/", ""));
				component.get("pathtotexture").setText(component.get("pathtotexture").getText().replace(ItemEditor.WORKINGDIR.replace("itemeditor\\", "").replace("\\", "/") + "core/assets/texture/item/", ""));
			}
		} else {
			for(int i = 0; i < textboxname.length; i++) {
				if(i == 1) {
					combocomponent.setEnabled(false);
				} else {
					component.get(textboxname[i]).setEditable(false);
				}
			}
			component.get("pathtoicon").setText(ItemEditor.WORKINGDIR.replace("itemeditor\\", "").replace("\\", "/") + "core/assets/texture/item/" + component.get("pathtoicon").getText());
			component.get("pathtotexture").setText(ItemEditor.WORKINGDIR.replace("itemeditor\\", "").replace("\\", "/") + "core/assets/texture/item/" + component.get("pathtotexture").getText());
		}
	}
	
	public ItemData updateData() {
		ItemData data = new ItemData();
		data.itemid = Integer.parseInt(component.get("id").getText());
		data.type = ItemType.valueOf((String) combocomponent.getSelectedItem());
		data.name = component.get("name").getText();
		data.maxstack = Integer.parseInt(component.get("maxstack").getText());
		data.requirelevel = Integer.parseInt(component.get("requirelevel").getText());
		data.description = component.get("description").getText();
		data.pathtoicon = component.get("pathtoicon").getText();
		data.pathtotexture = component.get("pathtotexture").getText();
		data.heal = Integer.parseInt(component.get("heal").getText());
		data.damage = Integer.parseInt(component.get("damage").getText());
		data.defense = Integer.parseInt(component.get("defense").getText());
		
		String pathtoitemdata = ItemEditor.WORKINGDIR.replace("itemeditor\\", "").replace("\\", "/") + "core/assets/data/itemdata.xml";
		File inputFile = new File(pathtoitemdata);
		SAXBuilder saxBuilder = new SAXBuilder();
		Document document = null;
		try {
			document = saxBuilder.build(inputFile);
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Element rootElement = document.getRootElement();
		
		if(ItemEditor.itemList.contains(lastdata) && !lastdata.name.startsWith("_NewItem")) {
			List<Element> items = rootElement.getChildren();
			
			for (int temp = 0; temp < items.size(); temp++) {
				Element item = items.get(temp);
				if(data.itemid == Integer.parseInt(item.getChild("id").getText())){
					item.getChild("type").setText(data.type.name());
					item.getChild("name").setText(data.name);
					item.getChild("maxstack").setText(String.valueOf(data.maxstack));
					item.getChild("requirelv").setText(String.valueOf(data.requirelevel));
					item.getChild("description").setText(data.description);
					item.getChild("icon").setText(data.pathtoicon);
					item.getChild("sprite").setText(data.pathtotexture);
					item.getChild("properties").getChild("heal").setText(String.valueOf(data.heal));
					item.getChild("properties").getChild("damage").setText(String.valueOf(data.damage));
					item.getChild("properties").getChild("defense").setText(String.valueOf(data.defense));
				}
			}
			
			XMLOutputter xmlOutput = new XMLOutputter();
			
			xmlOutput.setFormat(Format.getPrettyFormat());
			try {
				xmlOutput.output(document, new FileWriter(pathtoitemdata));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Element item = new Element("item");
			
			item.addContent(new Element("id").setText(String.valueOf(ItemEditor.itemList.size())));
			data.itemid = ItemEditor.itemList.size();
			item.addContent(new Element("type").setText(data.type.name()));
			item.addContent(new Element("name").setText(data.name));
			item.addContent(new Element("maxstack").setText(String.valueOf(data.maxstack)));
			item.addContent(new Element("requirelv").setText(String.valueOf(data.requirelevel)));
			item.addContent(new Element("description").setText(data.description));
			item.addContent(new Element("icon").setText(data.pathtoicon));
			item.addContent(new Element("sprite").setText(data.pathtotexture));
			
			Element properties = new Element("properties");
			
			properties.addContent(new Element("heal").setText(String.valueOf(data.heal)));
			properties.addContent(new Element("damage").setText(String.valueOf(data.damage)));
			properties.addContent(new Element("defense").setText(String.valueOf(data.defense)));
			
			item.addContent(properties);
			
			rootElement.addContent(item);
			
			XMLOutputter xmlOutput = new XMLOutputter();
			
			xmlOutput.setFormat(Format.getPrettyFormat());
			try {
				xmlOutput.output(document, new FileWriter(pathtoitemdata));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			ItemEditor.leftsidepanel.newitemdata = null;
		}
		ItemEditor.itemList.remove(lastdata);
		ItemEditor.itemList.add(data);
		
		return data;
	}
	
}
