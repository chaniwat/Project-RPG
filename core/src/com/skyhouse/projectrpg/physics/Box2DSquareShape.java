package com.skyhouse.projectrpg.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Box2DSquareShape {
	
	Body body;
	Fixture fixture;
	
	public Box2DSquareShape(float width, float height, float posX, float posY) {
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.DynamicBody;
		bodydef.fixedRotation = true;
		bodydef.position.set(posX , posY + height);
		
		body = PhysicGlobal.getWorld().createBody(bodydef);
		
		PolygonShape bodybox = new PolygonShape();
		bodybox.setAsBox(0.465f, 1f);
		body.createFixture(bodybox, 0.5f);
		
		bodybox.dispose();
	}
	
	public Box2DSquareShape(float width, float height, Vector2 position) {
		this(width, height, position.x, position.y);
	}
	
	public Box2DSquareShape(Vector2 size, float posX, float posY) {
		this(size.x, size.y, posX, posY);
	}
	
	public Box2DSquareShape(Vector2 size, Vector2 position) {
		this(size.x, size.y, position.x, position.y);
	}
	
	public Body getBody() {
		return body;
	}
}
