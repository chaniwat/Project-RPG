package com.skyhouse.projectrpg.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.skyhouse.projectrpg.data.StructureData;
import com.skyhouse.projectrpg.graphics.TileTexture;
import com.skyhouse.projectrpg.physics.B2DStructure;

/**
 * Structure class. Use in map.
 * @author Meranote
 */
public class Structure extends Actor {
	
	private StructureData data;
	private B2DStructure body;
	private TileTexture tiletexture;
	
	/**
	 * Construct a new structure.
	 * @param world
	 * @param data
	 * @param tiletexture
	 */
	public Structure(World world, StructureData data, TileTexture tiletexture) {
		body = new B2DStructure(world, data, BodyType.StaticBody);
		this.data = data;
		this.tiletexture = tiletexture;
	}
	
	@Override
	public void update(float deltatime) { }
	
	@Override
	public void draw(SpriteBatch batch) {
		tiletexture.draw(batch, new Vector2(data.x, data.y), new Vector2(data.width, data.height));
	}
	
	/**
	 * Get texture of this structure.
	 * @return {@link TileTexture}
	 */
	public TileTexture getTileTexture() {
		return tiletexture;
	}
	
	/**
	 * Get data of this structure.
	 * @return {@link StructureData}
	 */
	public StructureData getData() {
		return data;
	}
	
	@Override
	public void dispose() {
		body.dispose();
	}
	
}
