package com.skyhouse.projectrpg.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Disposable;
import com.skyhouse.projectrpg.data.Data;
import com.skyhouse.projectrpg.data.StructureData;
import com.skyhouse.projectrpg.graphics.TileTexture;
import com.skyhouse.projectrpg.physics.B2DStructure;

public class Structure {
	
	private StructureData data;
	private B2DStructure body;
	private TileTexture tiletexture;

	public Structure(World world, StructureData data, TileTexture tiletexture) {
		body = new B2DStructure(world, data, BodyType.StaticBody);
		this.data = data;
		this.tiletexture = tiletexture;
	}
	
	public TileTexture getTileTexture() {
		return tiletexture;
	}
	
	public void draw(SpriteBatch batch) {
		tiletexture.draw(batch, new Vector2(data.x, data.y), new Vector2(data.width, data.height));
	}
	
	public B2DStructure getBody() {
		return body;
	}
	
	public StructureData getData() {
		return data;
	}
	
	
}
