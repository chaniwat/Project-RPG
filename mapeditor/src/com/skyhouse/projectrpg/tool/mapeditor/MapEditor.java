package com.skyhouse.projectrpg.tool.mapeditor;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.skyhouse.projectrpg.tool.mapeditor.component.MainWindow;
import com.skyhouse.projectrpg.tool.mapeditor.viewer.MapViewer;

public class MapEditor {
	
	private MapEditor() {}
	
	public static MainWindow window;
	public static LwjglAWTCanvas viewercanvas;
	public static MapViewer viewerapp;
	
	public static boolean isStructureToolSelected;
	public static boolean isObjectToolSelected;
	public static boolean isNpcToolSelected;
	
	public static boolean isAnyToolSelected() {
		return isStructureToolSelected || isObjectToolSelected || isNpcToolSelected;
	}
	
	public static boolean isSelectObject;
	public static String selectName;
	public static String selectType;
	public static float selectX, selectY, selectWidth, selectHeight;

}
