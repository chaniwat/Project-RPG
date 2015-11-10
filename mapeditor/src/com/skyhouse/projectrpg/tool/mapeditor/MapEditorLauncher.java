package com.skyhouse.projectrpg.tool.mapeditor;

import javax.swing.SwingUtilities;

import com.skyhouse.projectrpg.tool.mapeditor.component.MainWindow;

public class MapEditorLauncher {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run () {
				new MainWindow();
			}
		});
	}
	
}
