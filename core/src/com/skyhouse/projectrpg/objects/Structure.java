package com.skyhouse.projectrpg.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.skyhouse.projectrpg.graphics.TileTexture;
import com.skyhouse.projectrpg.physics.StructureBody;

public class Structure {
	
	protected StructureBody body;
	protected TileTexture tiletexture;
	
	protected Vector2 position;
	protected Vector2 size;

	public Structure(Vector2 position, Vector2 size, TileTexture tiletexture) {
		this.position = position;
		this.size = size;
		body = new StructureBody(new Vector2(position.x + (size.x / 2f), position.y - (size.y / 2f)), size, BodyType.StaticBody);
		this.tiletexture = tiletexture;
	}
	
	public TileTexture getTileTexture() {
		return tiletexture;
	}
	
	public void render(SpriteBatch batch) {
		tiletexture.draw(batch, position, size);
	}
	
}
