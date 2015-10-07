package com.skyhouse.projectrpg.map;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.skyhouse.projectrpg.entities.Structure;
import com.skyhouse.projectrpg.entities.data.StructureData;
import com.skyhouse.projectrpg.graphics.TileTexture;
import com.skyhouse.projectrpg.graphics.TileTexture.TileTexturePosition;
import com.skyhouse.projectrpg.objects.BackgroundGlobal;
import com.skyhouse.projectrpg.physics.StructureBody;

public class Map {

	Element root, head, backgrounddata;
	Array<Element> structuredata, npcs, tiggerareas, connections, textureregions;
	TextureAtlas texture;
	
	HashMap<String, Structure> structures;
	HashMap<String, StructureBody> structuresbody;
	String name, mapname;
	
	public Map(FileHandle files, AssetManager assetmanager) throws IOException  {
		XmlReader reader = new XmlReader();
		root = reader.parse(files);
		head = root.getChildByName("head");

		structures = new HashMap<String, Structure>();
		structuresbody = new HashMap<String, StructureBody>();
		
		name = head.getChildByName("name").getText();
		mapname = head.getChildByName("mapname").getText();
				
		if(assetmanager != null) {
			String pathtotexture = "textures/tilemap/" + head.getChildByName("texture").getText() + ".pack";
			assetmanager.load(pathtotexture, TextureAtlas.class);
			texture = assetmanager.get(pathtotexture, TextureAtlas.class);
			
			backgrounddata = head.getChildByName("background");
			
			String pathtobg = "textures/background/" + backgrounddata.getChildByName("url").getText() + ".png";
			String width = backgrounddata.getChildByName("width").getText();
			String height = backgrounddata.getChildByName("height").getText();
			
			BackgroundGlobal.setBackground(assetmanager.get(pathtobg, Texture.class));
			if(!width.equals("auto") && !height.equals("auto")) {
				BackgroundGlobal.setSize(Float.parseFloat(width), Float.parseFloat(height));
			} else if(width.equals("auto") && !height.equals("auto")) {
				BackgroundGlobal.setSizeByHeight(Float.parseFloat(height));
			} else if(!width.equals("auto") && height.equals("auto")) {
				BackgroundGlobal.setSizeByWidth(Float.parseFloat(width));
			} else {
				BackgroundGlobal.setSizeByHeight(20f);
			}
		}
		
		structuredata = root.getChildByName("structures").getChildrenByName("structure");
		
		for(Element structure : structuredata) {
			
			StructureData data = new StructureData(
					Float.parseFloat(structure.getChildByName("position_x").getText()), 
					Float.parseFloat(structure.getChildByName("position_y").getText()), 
					Float.parseFloat(structure.getChildByName("width").getText()), 
					Float.parseFloat(structure.getChildByName("height").getText())
			);
			
			if(assetmanager != null) {
				TileTexture tiletexture  = new TileTexture();
				textureregions = structure.getChildByName("textureregions").getChildrenByName("region");
				for(Element region : textureregions) {
					tiletexture.setRegionPosition(texture.findRegion(region.getText()), TileTexturePosition.valueOf(( region.getAttribute("position"))));
				}
				textureregions.clear();

				structures.put(structure.getChildByName("name").getText(), new Structure(data, tiletexture));				
			} else {
				structuresbody.put(structure.getChildByName("name").getText(), new StructureBody(data, BodyType.StaticBody));
			}
		}
	}
	
	public void draw(SpriteBatch batch) {
		for(Structure structure : structures.values()) {
			structure.render(batch);
		}
	}
	
}
