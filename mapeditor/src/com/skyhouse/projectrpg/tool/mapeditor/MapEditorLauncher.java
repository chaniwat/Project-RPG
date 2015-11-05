package com.skyhouse.projectrpg.tool.mapeditor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableColumn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.skyhouse.projectrpg.ProjectRPG;

public class MapEditorLauncher extends JFrame {
	private static final long serialVersionUID = -4619772334876048463L;
	
	private JFrame frame;
	public static LwjglAWTCanvas mapView;
	
	private static MapViewer mapViewerApp;
	
	private static JLabel positionStatusLabel;
	private static JLabel mapSizeStatusLabel;
	
	public MapEditorLauncher() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());
		
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
		mapViewerApp = new MapViewer();
		mapView = new LwjglAWTCanvas(mapViewerApp);
		container.add(mapView.getCanvas());
		
		buildMenuBar();
		
		JPanel statusPanel = new JPanel();
		statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 25));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		add(statusPanel, BorderLayout.SOUTH);
		
		mapSizeStatusLabel = new JLabel("");
		mapSizeStatusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		mapSizeStatusLabel.setSize(180, 20);
		statusPanel.add(mapSizeStatusLabel);
		
		positionStatusLabel = new JLabel("");
		positionStatusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		positionStatusLabel.setSize(180, 20);
		statusPanel.add(positionStatusLabel);
		
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
		
		JMenu editMenu = new JMenu("Edit");
		
		JMenu viewMenu = new JMenu("View");
		JMenuItem reloadMapViewMenuItem = new JMenuItem("Reload map");
		reloadMapViewMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapViewerApp.reloadMap();
			}
		});
		
		viewMenu.add(reloadMapViewMenuItem);
		
		menubar.add(fileMenu);
		menubar.add(editMenu);
		menubar.add(viewMenu);
		
		setJMenuBar(menubar);
		
		
	}
	
	public static void updatePositionStatus(float x, float y) {
		positionStatusLabel.setText(String.format("  Position : %.2f, %.2f", x, y));
	}
	
	public static void updateMapSizeStatus(int width, int height) {
		mapSizeStatusLabel.setText(String.format("  Map size : %dx%d  |", width, height));
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run () {
				new MapEditorLauncher();
			}
		});
	}
	
}
