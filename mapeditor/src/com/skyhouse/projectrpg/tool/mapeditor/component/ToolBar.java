package com.skyhouse.projectrpg.tool.mapeditor.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.badlogic.gdx.Gdx;
import com.skyhouse.projectrpg.data.MapData;
import com.skyhouse.projectrpg.data.MapData.MapType;
import com.skyhouse.projectrpg.tool.mapeditor.MapEditor;
import com.skyhouse.projectrpg.tool.mapeditor.listener.ToolBarButtonListener;
import com.skyhouse.projectrpg.tool.mapeditor.utils.ImageUtils;

public class ToolBar extends JPanel {
	private static final long serialVersionUID = -6312164878475732918L;

	private ImageIcon structuretool, objecttool, npctool, mapsetting;
	private ImageIcon[] connectiontool, edit;
	private JButton structurebtn, objectbtn, npcbtn, connectionbtn, mapbtn, editbtn;
	
	public ToolBar() {
		super();
		setBackground(new Color(236f / 255f, 240f / 255f, 241f / 255f));
		setPreferredSize(new Dimension(this.getWidth(), 50));
		setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		prepareImageIcon();
		prepareButton();
	}

	private void prepareImageIcon() {
		structuretool = buildImageIcon("tool/mapeditor/structuretool.png");
		objecttool = buildImageIcon("tool/mapeditor/objecttool.png");
		npctool = buildImageIcon("tool/mapeditor/npctool.png");
		connectiontool = new ImageIcon[2];
		connectiontool[0] = buildImageIcon("tool/mapeditor/connectiontool.png");
		connectiontool[1] = buildImageIcon("tool/mapeditor/connectiontool_disabled.png");
		mapsetting = buildImageIcon("tool/mapeditor/mapsetting.png");
		edit = new ImageIcon[2];
		edit[0] = buildImageIcon("tool/mapeditor/edit.png");
		edit[1] = buildImageIcon("tool/mapeditor/edit_disabled.png");
	}
	
	private void prepareButton() {
		structurebtn = buildButton(structuretool, "Create structure");
		structurebtn.setActionCommand("select_tool_structure");
		objectbtn = buildButton(objecttool, "Create object");
		objectbtn.setActionCommand("select_tool_object");
		npcbtn = buildButton(npctool, "Create NPC");
		npcbtn.setActionCommand("select_tool_npc");
		connectionbtn = buildButton(connectiontool[0], "Setting Connection");
		connectionbtn.setDisabledIcon(connectiontool[1]);
		connectionbtn.setActionCommand("show_connection_setting");
		mapbtn = buildButton(mapsetting, "Setting map");
		mapbtn.setActionCommand("show_map_setting");
		editbtn = buildButton(edit[0], "Edit selected");
		editbtn.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
		editbtn.setDisabledIcon(edit[1]);
		editbtn.setActionCommand("show_current_setting");
		
		ToolBarButtonListener listener = new ToolBarButtonListener(structurebtn, objectbtn, npcbtn);
		structurebtn.addActionListener(listener);
		objectbtn.addActionListener(listener);
		npcbtn.addActionListener(listener);
		connectionbtn.addActionListener(listener);
		mapbtn.addActionListener(listener);
		editbtn.addActionListener(listener);
		
		add(structurebtn);
		add(objectbtn);
		add(npcbtn);
		add(editbtn);
		add(connectionbtn);
		add(mapbtn);
		
		editbtn.setEnabled(false);
		connectionbtn.setEnabled(false);
	}
	
	public JButton getStructurebtn() {
		return structurebtn;
	}

	public JButton getObjectbtn() {
		return objectbtn;
	}

	public JButton getNpcbtn() {
		return npcbtn;
	}

	public JButton getConnectionbtn() {
		return connectionbtn;
	}

	public JButton getMapbtn() {
		return mapbtn;
	}

	public JButton getEditbtn() {
		return editbtn;
	}
	
	public void updateToolBar() {
		MapData data = MapEditor.viewerapp.getMap().getData();
	
		if(data.type == MapType.TOWN) {
			connectionbtn.setEnabled(true);
		} else {
			connectionbtn.setEnabled(false);
		}
	}

	private ImageIcon buildImageIcon(String internalpath) {
		BufferedImage buffer = null;
		try {
			buffer = ImageIO.read(Gdx.files.internal(internalpath).read());
			buffer = ImageUtils.resizeImage(buffer, 45, 45);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ImageIcon(buffer);
	}
	
	private JButton buildButton(ImageIcon icon, String tooltiptext) {
		JButton button = new JButton(icon);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setContentAreaFilled(false);
		button.setPreferredSize(new Dimension(50, 50));
		button.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setToolTipText(tooltiptext);
		
		return button;
	}
	
}
