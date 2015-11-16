package com.skyhouse.projectrpg.map;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.skyhouse.projectrpg.data.MapData;
import com.skyhouse.projectrpg.data.StructureData;
import com.skyhouse.projectrpg.data.MapData.MapStructureTexture;
import com.skyhouse.projectrpg.data.StructureData.StructureBehavior;
import com.skyhouse.projectrpg.entity.Structure;
import com.skyhouse.projectrpg.graphics.TileTexture;
import com.skyhouse.projectrpg.graphics.TileTexture.TileTexturePosition;

/**
 * Map. Contain map data and collection of {@link Structure}.
 * @author Meranote
 */
public class Map implements Disposable {

	private MapData data;
	private HashMap<String, Structure> structures;
	private Texture background;
	
	/**
	 * Construct a new {@link Map} with given {@link World} and {@link MapData}.
	 */
	public Map(World world, MapData data) {	
		this(world, data, 
				new TextureAtlas(Gdx.files.internal(data.mapTexturePackPath)), 
				new Texture(Gdx.files.internal(data.mapBackgroundPath)));
	}
	
	/**
	 * Construct a new {@link Map} with given {@link World}, {@link MapData}, map texture and background.
	 */
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
			
			sData.behavior = StructureBehavior.SOLID;
			
			structures.put(sName, new Structure(world, sData, tiletexture));
		}
		
		StructureData edgewallleft = new StructureData();
		edgewallleft.x = -2f;
		edgewallleft.y = data.mapTopPoint + 10f;
		edgewallleft.width = 2f;
		edgewallleft.height = data.mapHeight + 10f;
		edgewallleft.behavior = StructureBehavior.SOLID;
		structures.put("edge_wall_left", new Structure(world, edgewallleft, null));
		
		StructureData edgewallright = new StructureData();
		edgewallright.x = data.mapWidth;
		edgewallright.y = data.mapTopPoint + 10f;
		edgewallright.width = 2f;
		edgewallright.height = data.mapHeight + 10f;
		edgewallright.behavior = StructureBehavior.SOLID;
		structures.put("edge_wall_right", new Structure(world, edgewallright, null));
	}
	
	/**
	 * Draw the {@link Structure} of this map.
	 * @param batch
	 */
	public void draw(SpriteBatch batch) {
		for(Structure s : structures.values()) {
			s.draw(batch);
		}
	}
	
	/**
	 * Get background.
	 * @return {@link Texture}
	 */
	public Texture getBackground() {
		return background;
	}
	
	/**
	 * Get the map data.
	 * @return {@link MapData}
	 */
	public MapData getData() {
		return data;
	}

	/**
	 * Release the resource.
	 */
	@Override
	public void dispose() {
		for(Structure structure : structures.values()) {
			structure.dispose();
		}
		structures.clear();
	}
	
}
