package com.skyhouse.projectrpg.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.skyhouse.projectrpg.entities.data.StructureData;

public class StructureBody extends BodyTemplate{

	StructureData data;
	
	public StructureBody(StructureData data, BodyType type) {
		super(new Vector2(data.getPositionX() + (data.getWidth() / 2f), data.getPositionY() - (data.getHeight() / 2f)), new Vector2(data.getWidth(), data.getHeight()), type, 0.0f);
	}

	@Override
	public void update() {
				
	}
	
}
