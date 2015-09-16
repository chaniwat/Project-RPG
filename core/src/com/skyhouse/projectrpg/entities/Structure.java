package com.skyhouse.projectrpg.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.skyhouse.projectrpg.entities.data.StructureData;
import com.skyhouse.projectrpg.graphics.TileTexture;

public class Structure extends StructureData {
	
	protected TileTexture tiletexture;

	public Structure(StructureData data, TileTexture tiletexture) {
		super(data);
		
		this.tiletexture = tiletexture;
	}
	
	public TileTexture getTileTexture() {
		return tiletexture;
	}
	
	public void render(SpriteBatch batch) {
		tiletexture.draw(batch, new Vector2(getPositionX(), getPositionY()), new Vector2(getWidth(), getHeight()));
	}
	
}
