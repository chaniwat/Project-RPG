package com.skyhouse.projectrpg.tool.mapviewer;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.skyhouse.projectrpg.ProjectRPG;

public class MapViewerLauncher extends JFrame {
	private static final long serialVersionUID = -4619772334876048463L;
	
	private JFrame frame;
	public static LwjglAWTCanvas mapView;
	
	public MapViewerLauncher() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		frame = this;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(JOptionPane.showConfirmDialog(frame, "Exit?", "Confirm exit", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
					return;
				}
				Gdx.app.exit();
			}
		});
		setTitle(ProjectRPG.TITLE + " - MapViewer");
		
		Container container = getContentPane();
		mapView = new LwjglAWTCanvas(new MapViewer());
		container.add(mapView.getCanvas());
		
		buildMenuBar();
		
		pack();
		setVisible(true);
		setSize(1280, 720);
	}
	
	private void buildMenuBar() {
		JMenuBar menubar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem openMenuItem = new JMenuItem("open");
		openMenuItem.setActionCommand("openfile");
		JMenuItem exitMenuItem = new JMenuItem("exit");
		exitMenuItem.setActionCommand("exitprogram");
		
		fileMenu.add(openMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
		
		menubar.add(fileMenu);
		
		setJMenuBar(menubar);
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
