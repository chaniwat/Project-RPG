package com.skyhouse.projectrpg.tool.itemeditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

import javax.swing.JFrame;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.item.BaseItem.ItemBaseType;
import com.sun.org.apache.xerces.internal.parsers.XMLParser;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 8361435069801727415L;

	public MainWindow() {
		super(ProjectRPG.TITLE + " - Item Editor");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(1000, 500);
		setResizable(false);
		
		ItemEditor.mainwindow = this;
		ItemEditor.itemList = new TreeSet<ItemData>();
		
		try {
			readItemData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		LeftSidePanel leftSide = new LeftSidePanel();
		leftSide.setBackground(Color.ORANGE);
		add(leftSide, BorderLayout.WEST);
		ItemEditor.leftsidepanel = leftSide;
		
		RightSidePanel rightSide = new RightSidePanel();
		add(rightSide, BorderLayout.CENTER);
		ItemEditor.rightsidepanel = rightSide;
		
		pack();
		setVisible(true);
	}

	private void readItemData() throws IOException {
		String pathtoitemdata = ItemEditor.WORKINGDIR.replace("itemeditor\\", "").replace("\\", "/") + "core/assets/data/itemdata.xml";
		File file = new File(pathtoitemdata);
		
		XmlReader reader = new XmlReader();
		Element root = reader.parse(new FileHandle(file));
		
		Array<Element> items = root.getChildrenByName("item");
		
		for(Element item : items) {
			ItemData data = new ItemData();
			data.itemid = Integer.parseInt(item.getChildByName("id").getText());
			data.type = ItemBaseType.valueOf(item.getChildByName("type").getText().toUpperCase());
			data.name = item.getChildByName("name").getText();
			data.maxstack = Integer.parseInt(item.getChildByName("maxstack").getText());
			data.requirelevel = Integer.parseInt(item.getChildByName("requirelv").getText());
			data.description = item.getChildByName("description").getText();
			data.pathtoicon = ItemEditor.WORKINGDIR.replace("itemeditor\\", "").replace("\\", "/") + "core/assets/texture/item/" + item.getChildByName("icon").getText();
			data.pathtotexture = ItemEditor.WORKINGDIR.replace("itemeditor\\", "").replace("\\", "/") + "core/assets/texture/item/" + item.getChildByName("sprite").getText();
			data.heal = Integer.parseInt(item.getChildByName("properties").getChildByName("heal").getText());
			data.damage = Integer.parseInt(item.getChildByName("properties").getChildByName("damage").getText());
			data.defense = Integer.parseInt(item.getChildByName("properties").getChildByName("defense").getText());
			ItemEditor.itemList.add(data);
		}
	}
	
}
