package com.skyhouse.projectrpg.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class StructureBody extends BodyTemplate{

	public StructureBody(Vector2 position, Vector2 size, BodyType type, float density) {
		super(position, size, type, density);
	}
	
	public StructureBody(Vector2 position, Vector2 size, BodyType type) {
		super(position, size, type, 0.0f);
	}
	
}
