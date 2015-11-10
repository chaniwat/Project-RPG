package com.skyhouse.projectrpg.tool.mapeditor.listener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

import com.skyhouse.projectrpg.tool.mapeditor.MapEditor;

public class ToolBarButtonListener implements ActionListener {

	private JButton structurebtn, objectbtn, npcbtn;
	
	private Border emBorder, mtBorder;
	
	public ToolBarButtonListener(JButton structurebtn, JButton objectbtn, JButton npcbtn) {
		this.structurebtn = structurebtn;
		this.objectbtn = objectbtn;
		this.npcbtn = npcbtn;
		
		emBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);
		mtBorder = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.MAGENTA);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("select_tool_structure")) {
			if(!MapEditor.isStructureToolSelected) {
				structurebtn.setBorder(mtBorder);
				objectbtn.setBorder(emBorder);
				npcbtn.setBorder(emBorder);
				MapEditor.isStructureToolSelected = true;
				MapEditor.isObjectToolSelected = false;
				MapEditor.isNpcToolSelected = false;
				MapEditor.isSelectObject = false;
				MapEditor.window.getStatusBar().updateWorkStatus("Structure tool active", Color.BLACK, Color.YELLOW);
			} else {
				structurebtn.setBorder(emBorder);
				MapEditor.isStructureToolSelected = false;
				MapEditor.window.getStatusBar().updateWorkStatus("Tool deactive");
			}
		} else if(e.getActionCommand().equals("select_tool_object")) {
			if(!MapEditor.isObjectToolSelected) {
				structurebtn.setBorder(emBorder);
				objectbtn.setBorder(mtBorder);
				npcbtn.setBorder(emBorder);
				MapEditor.isStructureToolSelected = false;
				MapEditor.isObjectToolSelected = true;
				MapEditor.isNpcToolSelected = false;
				MapEditor.isSelectObject = false;
				MapEditor.window.getStatusBar().updateWorkStatus("Object tool active", Color.BLACK, Color.YELLOW);
			} else {
				objectbtn.setBorder(emBorder);
				MapEditor.isObjectToolSelected = false;
				MapEditor.window.getStatusBar().updateWorkStatus("Tool deactive");
			}
		} else if(e.getActionCommand().equals("select_tool_npc")) {
			if(!MapEditor.isNpcToolSelected) {
				structurebtn.setBorder(emBorder);
				objectbtn.setBorder(emBorder);
				npcbtn.setBorder(mtBorder);
				MapEditor.isStructureToolSelected = false;
				MapEditor.isObjectToolSelected = false;
				MapEditor.isNpcToolSelected = true;
				MapEditor.isSelectObject = false;
				MapEditor.window.getStatusBar().updateWorkStatus("NPC tool active", Color.BLACK, Color.YELLOW);
			} else {
				npcbtn.setBorder(emBorder);
				MapEditor.isNpcToolSelected = false;
				MapEditor.window.getStatusBar().updateWorkStatus("Tool deactive");
			}
		}
	}

}
