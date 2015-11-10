package com.skyhouse.projectrpg.tool.itemeditor;

import javax.swing.SwingUtilities;

public class ItemEditorLauncher {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow();
			}
		});
	}
	
}
