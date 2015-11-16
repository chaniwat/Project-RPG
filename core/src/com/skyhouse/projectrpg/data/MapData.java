package com.skyhouse.projectrpg.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.skyhouse.projectrpg.data.StructureData.StructureBehavior;
import com.skyhouse.projectrpg.entity.Structure;
import com.skyhouse.projectrpg.graphics.TileTexture;
import com.skyhouse.projectrpg.graphics.TileTexture.TileTexturePosition;

/**
 * Map data. Also a ".map" reader.
 * Contain a map data. 
 * @author Meranote
 */
public class MapData extends Data {
	
	/**
	 * Map type.
	 * @author Meranote
	 */
	public static enum MapType {
		TOWN, DUNGEON, PVP
	}
	
	/**
	 * Collection of {@link TileTexturePosition} pair with region name of {@link TextureAtlas} to set a texture of {@link Structure} by using {@link TileTexture}..
	 * @author Meranote
	 */
	public static class MapStructureTexture {
		public HashMap<TileTexturePosition, String> region = new HashMap<TileTexture.TileTexturePosition, String>();
	}
	
	public String path;
	public String name;
	public Vector2 spawningPoint;
	public int mapWidth, mapHeight;
	public int mapBottomPoint, mapTopPoint, mapLeftPoint, MapRightPoint;
	public MapType type;
	public String mapTexturePackPath, mapBackgroundPath;
	public HashMap<String, StructureData> structures;
	public HashMap<String, MapStructureTexture> structureTextureMapper;
	public HashMap<String, StructureBehavior> structureBehaviorMapper;
	public ArrayList<NpcData> npcData;
	
	/**
	 * @param handle
	 */
	public MapData(FileHandle handle) {
		structures = new HashMap<String, StructureData>();
		structureTextureMapper = new HashMap<String, MapData.MapStructureTexture>(); 
		structureBehaviorMapper = new HashMap<String, StructureData.StructureBehavior>();
		npcData = new ArrayList<NpcData>();
		
		// Save internal map path
		path = handle.path();
		
		// Open xml reader and get root element
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
		
		calculateMapSize();
		calculateMapPoint();
	}
	
	/**
	 * Read map data (name, type, path to texture pack and background).
	 */
	private void getMapData(Element root) {
		Element data = root.getChildByName("mapdata");
		name = data.getChildByName("name").getText();
		type = MapType.valueOf(data.getChildByName("type").getText().toUpperCase());
		mapTexturePackPath = "texture/structure/" + data.getChildByName("texture").getText() + ".pack";
		mapBackgroundPath = "texture/background/" + data.getChildByName("background").getText() + ".png";
		
		Element spawnpoint = data.getChildByName("spawnpoint");
		spawningPoint = new Vector2(Float.parseFloat(spawnpoint.getChildByName("x").getText()), Float.parseFloat(spawnpoint.getChildByName("y").getText()));
	}
	
	/**
	 * Create a collection of {@link StructureData} and {@link MapStructureTexture} both pair with structure name.
	 */
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
	
	/**
	 * Create a collection of object data . 
	 */
	private void getMapObjectData(Element root) {
		
	}
	
	/**
	 * Create a collection of NPC data.
	 */
	private void getNPCData(Element root) {
		for(Element npc : root.getChildByName("npcs").getChildrenByName("npc")) {
			NpcData data = new NpcData();
			data.id = Integer.parseInt(npc.getChildByName("id").getText());
			data.x = Float.parseFloat(npc.getChildByName("positionx").getText());
			data.y = Float.parseFloat(npc.getChildByName("positiony").getText());
			npcData.add(data);
		}
	}
	
	private void calculateMapSize() {
		ArrayList<Integer> x1, y1, x2, y2;
		x1 = new ArrayList<Integer>();
		y1 = new ArrayList<Integer>();
		x2 = new ArrayList<Integer>();
		y2 = new ArrayList<Integer>();
		
		for(StructureData s : structures.values()) {
			x1.add((int)(s.x));
			y1.add((int)(s.y - s.height));
			x2.add((int)(s.x + s.width));
			y2.add((int)(s.y));
		}
		
		Collections.sort(x1);
		Collections.sort(y1);
		Collections.sort(x2);
		Collections.sort(y2);
		
		mapWidth = Math.abs(x1.get(0) - x2.get(x2.size() - 1));
		mapHeight = Math.abs(y1.get(0) - y2.get(y2.size() - 1));
		
		mapTopPoint = y2.get(y2.size() - 1);
		mapBottomPoint = y1.get(0);
		mapLeftPoint = x1.get(0);
		MapRightPoint = x2.get(x2.size() - 1);
	}
	
	private void calculateMapPoint() {
		ArrayList<Integer> x, y;
		x = new ArrayList<Integer>();
		y = new ArrayList<Integer>();
		
		for(StructureData s : structures.values()) {
			x.add((int)(s.x));
			y.add((int)(s.y));
		}
		
		Collections.sort(x);
		Collections.sort(y);
		
		
	}
	
}
