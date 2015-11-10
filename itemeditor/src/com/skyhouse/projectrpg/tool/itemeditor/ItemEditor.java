package com.skyhouse.projectrpg.tool.itemeditor;

import java.io.File;
import java.util.TreeSet;

public class ItemEditor {

	private ItemEditor() {}
	
	public static final String WORKINGDIR = new File("").getAbsolutePath() + File.separator;
	
	public static MainWindow mainwindow;
	public static LeftSidePanel leftsidepanel;
	public static RightSidePanel rightsidepanel;
	
	public static TreeSet<ItemData> itemList;
}
