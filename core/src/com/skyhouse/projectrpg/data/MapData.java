package com.skyhouse.projectrpg.data;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.skyhouse.projectrpg.data.StructureData.StructureBehavior;
import com.skyhouse.projectrpg.graphics.TileTexture;
import com.skyhouse.projectrpg.graphics.TileTexture.TileTexturePosition;

public class MapData extends Data {
	
	public static enum MapType {
		WORLD, DUNGEON, PVP
	}
	
	public static class MapStructureTexture {
		
		public HashMap<TileTexturePosition, String> region;
		
		public MapStructureTexture() {
			region = new HashMap<TileTexture.TileTexturePosition, String>();
		}
		
	}
	
	public String name;
	public MapType type;
	public String mapTexture, mapBackground;
	public HashMap<String, StructureData> structures;
	public HashMap<String, MapStructureTexture> structureTextureMapper; 
	public HashMap<String, StructureBehavior> structureBehaviorMapper;
	
	public MapData(FileHandle handle) {
		structures = new HashMap<String, StructureData>();
		structureTextureMapper = new HashMap<String, MapData.MapStructureTexture>();
		structureBehaviorMapper = new HashMap<String, StructureData.StructureBehavior>();
		
		// Make XML reader
		XmlReader reader = new XmlReader();
		Element root;
		try {
			root = reader.parse(handle);
		} catch (IOException e) {
			throw new GdxRuntimeException(e);
		}
		
		getMapData(root);
		getStructureData(root);
		getMapObjectData(root);
		getNPCData(root);
		if(type.equals(MapType.WORLD)) getConnection(root);
	}
	
	private void getMapData(Element root) {
		Element data = root.getChildByName("mapdata");
		name = data.getChildByName("name").getText();
		type = MapType.valueOf(data.getChildByName("type").getText().toUpperCase());
		mapTexture = data.getChildByName("texture").getText();
		mapBackground = data.getChildByName("background").getText();
	}
	
	private void getStructureData(Element root) {
		for(Element structure : root.getChildByName("structures").getChildrenByName("structure")) {
			String sName = structure.getChildByName("name").getText();
			StructureData sData = new StructureData();
			sData.x = Float.parseFloat(structure.getChildByName("positionx").getText());
			sData.y = Float.parseFloat(structure.getChildByName("positiony").getText());
			sData.width = Float.parseFloat(structure.getChildByName("width").getText());
			sData.height = Float.parseFloat(structure.getChildByName("height").getText());
			structures.put(sName, sData);
			MapStructureTexture st = new MapStructureTexture();
			for(Element region : structure.getChildByName("textureregions").getChildrenByName("region")) {
				st.region.put(TileTexturePosition.valueOf(region.getAttribute("position")), region.getText());
			}
			structureTextureMapper.put(sName, st);
		}
	}
	
	private void getMapObjectData(Element root) {
		
	}
	
	private void getNPCData(Element root) {
		
	}
	
	private void getConnection(Element root) {
		
	}
	
}
