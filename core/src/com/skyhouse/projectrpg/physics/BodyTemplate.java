package com.skyhouse.projectrpg.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class BodyTemplate {
	
	Body body;
	Fixture fixture;
	
	public BodyTemplate(Vector2 position, Vector2 size, BodyType type, float density) {
		BodyDef bodydef = new BodyDef();
		bodydef.type = type;
		bodydef.position.set(position.x , position.y);
		
		body = PhysicGlobal.getWorld().createBody(bodydef);
		
		PolygonShape box = new PolygonShape();
		box.setAsBox(size.x / 2f, size.y / 2f);
		body.createFixture(box, density);
		
		box.dispose();
	}
	
	public Body getBody() {
		return body;
	}
	
}
