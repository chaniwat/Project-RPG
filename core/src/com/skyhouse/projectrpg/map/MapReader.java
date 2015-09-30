package com.skyhouse.projectrpg.map;

import java.io.IOException;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.skyhouse.projectrpg.entities.Structure;
import com.skyhouse.projectrpg.entities.data.StructureData;
import com.skyhouse.projectrpg.graphics.TileTexture;
import com.skyhouse.projectrpg.graphics.TileTexture.TileTexturePosition;
import com.skyhouse.projectrpg.physics.StructureBody;

public class MapReader {

	Map map;
	MapBody mapbody;
	Element root, head;
	Array<Element> structures, npcs, tiggerareas, connections, textureregions;
	TextureAtlas texture;
	
	public MapReader(FileHandle files, AssetManager assetmanager) throws IOException {
		XmlReader reader = new XmlReader();
		root = reader.parse(files);
		head = root.getChildByName("head");
		
		if(assetmanager != null) {
			String pathtotexture = "textures/tilemap/" + head.getChildByName("texture").getText() + ".pack";
			if(!assetmanager.isLoaded(pathtotexture, TextureAtlas.class)) {
				assetmanager.load(pathtotexture, TextureAtlas.class);
				assetmanager.finishLoadingAsset(pathtotexture);
			}
			texture = assetmanager.get(pathtotexture, TextureAtlas.class);			
		}
	}
	
	public Map getMap() {
		if(map == null) {
			map = new Map(head.getChildByName("name").getText(), head.getChildByName("mapname").getText());			
		}
		
		structures = root.getChildByName("structures").getChildrenByName("structure");
		for(Element structure : structures) {
			
			TileTexture tiletexture  = new TileTexture();
			textureregions = structure.getChildByName("textureregions").getChildrenByName("region");
			for(Element region : textureregions) {
				tiletexture.setRegionPosition(texture.findRegion(region.getText()), TileTexturePosition.valueOf(( region.getAttribute("position"))));
			}
			textureregions.clear();
			
			StructureData data = new StructureData(
					Float.parseFloat(structure.getChildByName("position_x").getText()), 
					Float.parseFloat(structure.getChildByName("position_y").getText()), 
					Float.parseFloat(structure.getChildByName("width").getText()), 
					Float.parseFloat(structure.getChildByName("height").getText())
			);
			
			map.addStructures(structure.getChildByName("name").getText(), new Structure(data, tiletexture));
		}
		structures.clear();
		
		return map;
	}
	
	public MapBody getMapBody() {
		if(mapbody == null) {
			mapbody = new MapBody(head.getChildByName("name").getText(), head.getChildByName("mapname").getText());
		}
		
		structures = root.getChildByName("structures").getChildrenByName("structure");
		for(Element structure : structures) {
			
			StructureData data = new StructureData(
					Float.parseFloat(structure.getChildByName("position_x").getText()), 
					Float.parseFloat(structure.getChildByName("position_y").getText()), 
					Float.parseFloat(structure.getChildByName("width").getText()), 
					Float.parseFloat(structure.getChildByName("height").getText())
			);
			
			mapbody.addStructures(structure.getChildByName("name").getText(), new StructureBody(data, BodyType.StaticBody));
		}
		structures.clear();
		
		return mapbody;
	}
	
}
