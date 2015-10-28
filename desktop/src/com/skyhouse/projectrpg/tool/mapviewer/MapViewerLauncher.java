package com.skyhouse.projectrpg.tool.mapviewer;

import java.awt.Canvas;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.MenuBar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.skyhouse.projectrpg.ProjectRPG;

public class MapViewerLauncher extends JFrame {
	private static final long serialVersionUID = -4619772334876048463L;
	
	public static Canvas mapView;
	private LwjglAWTCanvas canvas;
	
	public MapViewerLauncher() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		final JFrame frame = this;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(JOptionPane.showConfirmDialog(frame, "Exit?", "Confirm exit", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
					return;
				}
				Gdx.app.log(ProjectRPG.TITLE, "exit.");
				Gdx.app.exit();
			}
		});
		setTitle(ProjectRPG.TITLE + " - MapViewer");
		
		Container container = getContentPane();
		canvas = new LwjglAWTCanvas(new MapViewer());
		mapView = canvas.getCanvas();
		container.add(mapView);
		
		canvas.getCanvas().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3) {
					canvas.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3) {
					canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
			
		});
		
		JMenuBar menubar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menubar.add(fileMenu);
		setJMenuBar(menubar);
		
		pack();
		setVisible(true);
		setSize(1280, 720);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run () {
				new MapViewerLauncher();
			}
		});
	}
	
}
