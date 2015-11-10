package com.skyhouse.projectrpg.tool.mapeditor.listener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;

public class MainWindowListener extends WindowAdapter {

	private JFrame frame;
	
	public MainWindowListener(JFrame frame) {
		this.frame = frame;
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
//		if(JOptionPane.showConfirmDialog(frame, "Exit?", "Confirm exit", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
//			return;
//		}
		Gdx.app.exit();
//		System.exit(-1);
	}
	
}
