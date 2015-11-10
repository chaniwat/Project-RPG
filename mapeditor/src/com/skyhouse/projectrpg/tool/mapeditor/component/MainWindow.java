package com.skyhouse.projectrpg.tool.mapeditor.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.tool.mapeditor.MapEditor;
import com.skyhouse.projectrpg.tool.mapeditor.listener.MainWindowListener;
import com.skyhouse.projectrpg.tool.mapeditor.viewer.MapViewer;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 5586513641358247461L;
	
	private MenuBar menuBar;
	private ToolBar toolBar;
	private StatusBar statusBar;

	boolean flipflop = false;
	
	public MainWindow() {
		super();
		MapEditor.window = this;
		
		setTitle(ProjectRPG.TITLE + " - Map editor");
		setLayout(new BorderLayout());
		addWindowListener(new MainWindowListener(this));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		menuBar = new MenuBar();
		setJMenuBar(menuBar);
		
		Container container = getContentPane();
		
		MapViewer viewerapp = new MapViewer();
		MapEditor.viewerapp = viewerapp;
		LwjglAWTCanvas viewercanvas = new LwjglAWTCanvas(viewerapp); 
		MapEditor.viewercanvas = viewercanvas;
		container.add(viewercanvas.getCanvas(), BorderLayout.CENTER);
		
		toolBar = new ToolBar();
		container.add(toolBar, BorderLayout.NORTH);
		
		statusBar = new StatusBar();
		container.add(statusBar, BorderLayout.SOUTH);
		
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				toolBar.updateToolBar();
			}
		});
		
		pack();
		setSize(1280, 720);
		setVisible(true);
	}
	
	public ToolBar getToolBar() {
		return toolBar;
	}
	
	public StatusBar getStatusBar() {
		return statusBar;
	}
	
}
