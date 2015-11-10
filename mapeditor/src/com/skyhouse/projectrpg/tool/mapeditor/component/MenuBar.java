package com.skyhouse.projectrpg.tool.mapeditor.component;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.skyhouse.projectrpg.tool.mapeditor.MapEditor;

public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = -2262259850725162261L;

	public MenuBar() {
		super();
		setBackground(Color.WHITE);
		
		addFileMenu();
		addEditMenu();
		addViewMenu();
	}
	
	private void addFileMenu() {
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem openMenuItem = new JMenuItem("open");
		fileMenu.add(openMenuItem);		
		
		fileMenu.addSeparator();
		
		JMenuItem exitMenuItem = new JMenuItem("exit");
		fileMenu.add(exitMenuItem);
		
		openMenuItem.setActionCommand("openfile");
		exitMenuItem.setActionCommand("exitprogram");
		
		add(fileMenu);
	}
	
	private void addEditMenu() {
		JMenu editMenu = new JMenu("Edit");
		
		add(editMenu);
	}
	
	private void addViewMenu() {
		JMenu viewMenu = new JMenu("View");
		
		JMenuItem reloadMapViewMenuItem = new JMenuItem("Reload map");
		viewMenu.add(reloadMapViewMenuItem);
		
		reloadMapViewMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MapEditor.viewerapp.reloadMap();
			}
		});
		
		add(viewMenu);
	}
	
}
