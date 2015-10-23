package com.skyhouse.projectrpg.map;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.skyhouse.projectrpg.data.MapData;
import com.skyhouse.projectrpg.data.StructureData;
import com.skyhouse.projectrpg.data.MapData.MapStructureTexture;
import com.skyhouse.projectrpg.entity.Structure;
import com.skyhouse.projectrpg.graphics.TileTexture;
import com.skyhouse.projectrpg.graphics.TileTexture.TileTexturePosition;

public class Map {

	private MapData data;
	private HashMap<String, Structure> structures;
	private Texture background;
	
	public Map(World world, MapData data) {	
		this(world, data, 
				new TextureAtlas(Gdx.files.internal("texture/structure/" + data.mapTextureAtlasPath + ".pack")), 
				new Texture(Gdx.files.internal("texture/background" + data.mapBackgroundPath + ".png")));
	}
	
	public Map(World world, MapData data, TextureAtlas mapTexture, Texture mapBackground) {
		structures = new HashMap<String, Structure>();
		this.data = data;
		this.background = mapBackground;
		
		for(Entry<String, StructureData> entry : data.structures.entrySet()) {
			String sName = entry.getKey();
			StructureData sData = entry.getValue();
			
			MapStructureTexture texture = data.structureTextureMapper.get(sName);
			TileTexture tiletexture = new TileTexture();
			for(Entry<TileTexturePosition, String> region : texture.region.entrySet()) {
				tiletexture.setRegionPosition(mapTexture.findRegion(region.getValue()), region.getKey());
			}
			
			structures.put(sName, new Structure(world, sData, tiletexture));

		}
	}
	
	public void draw(SpriteBatch batch) {
		for(Structure s : structures.values()) {
			s.draw(batch);
		}
	}
	
	public Texture getBackground() {
		return background;
	}
	
	public MapData getData() {
		return data;
	}
	
}
