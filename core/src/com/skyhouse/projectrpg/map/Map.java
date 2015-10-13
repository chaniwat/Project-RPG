package com.skyhouse.projectrpg.map;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.skyhouse.projectrpg.entities.Structure;
import com.skyhouse.projectrpg.entities.data.StructureData;
import com.skyhouse.projectrpg.graphics.TileTexture;
import com.skyhouse.projectrpg.graphics.TileTexture.TileTexturePosition;
import com.skyhouse.projectrpg.physics.StructureBody;

public class Map {
	
	public class BackgroundData {
		public String path;
		public float width, height;
		
		public BackgroundData(String path, float width, float height) {
			this.path = path;
			this.width = width;
			this.height = height;
		}
	}
	
	String name, mapname;
	
	HashMap<String, Structure> structures;
	HashMap<String, StructureBody> structuresbody;
	
	String pathtobg;
	float bgwidth, bgheight;
	
	public Map(FileHandle files, AssetManager assetmanager) throws IOException  {
		structures = new HashMap<String, Structure>();
		structuresbody = new HashMap<String, StructureBody>();
		
		XmlReader reader = new XmlReader();
		Element root = reader.parse(files);
		Element head = root.getChildByName("head");
		
		name = head.getChildByName("name").getText();
		mapname = head.getChildByName("mapname").getText();
		
		TextureAtlas texture = null;
		
		if(assetmanager != null) {
			String pathtotexture = "textures/tilemap/" + head.getChildByName("texture").getText() + ".pack";
			texture = assetmanager.get(pathtotexture, TextureAtlas.class);
			
			Element backgrounddata = head.getChildByName("background");
			
			pathtobg = "textures/background/" + backgrounddata.getChildByName("url").getText() + ".png";
			String width = backgrounddata.getChildByName("width").getText();
			String height = backgrounddata.getChildByName("height").getText();
			
			if(!width.equals("auto") && !height.equals("auto")) {
				bgwidth = Float.parseFloat(width);
				bgheight = Float.parseFloat(height);
			} else if(width.equals("auto") && !height.equals("auto")) {
				bgwidth = Float.parseFloat(height) * ( assetmanager.get(pathtobg, Texture.class).getWidth() / assetmanager.get(pathtobg, Texture.class).getHeight());
				bgheight = Float.parseFloat(height);
			} else if(!width.equals("auto") && height.equals("auto")) {
				bgwidth = Float.parseFloat(width);
				bgheight = Float.parseFloat(width) * ( assetmanager.get(pathtobg, Texture.class).getHeight() / assetmanager.get(pathtobg, Texture.class).getWidth());
			} else {
				bgwidth = Float.parseFloat(height) * ( assetmanager.get(pathtobg, Texture.class).getWidth() / assetmanager.get(pathtobg, Texture.class).getHeight());
				bgheight = Float.parseFloat(height);
			}
		}
		
		Array<Element> structuredata = root.getChildByName("structures").getChildrenByName("structure");
		
		for(Element structure : structuredata) {
			StructureData data = new StructureData(
					Float.parseFloat(structure.getChildByName("position_x").getText()), 
					Float.parseFloat(structure.getChildByName("position_y").getText()), 
					Float.parseFloat(structure.getChildByName("width").getText()), 
					Float.parseFloat(structure.getChildByName("height").getText())
			);
			if(assetmanager != null) {
				TileTexture tiletexture  = new TileTexture();
				Array<Element> textureregions = structure.getChildByName("textureregions").getChildrenByName("region");
				for(Element region : textureregions) {
					tiletexture.setRegionPosition(texture.findRegion(region.getText()), TileTexturePosition.valueOf(( region.getAttribute("position"))));
				}
				structures.put(structure.getChildByName("name").getText(), new Structure(data, tiletexture));				
				textureregions.clear();
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
	
	public BackgroundData getBackgroundData() {
		return new BackgroundData(pathtobg, bgwidth, bgheight);
	}
	
	public String getMapName() {
		return mapname;
	}
	
	public String getName() {
		return name;
	}
	
}
