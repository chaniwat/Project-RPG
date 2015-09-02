package com.skyhouse.projectrpg.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Box2DBallTest {
	
	World world;
	
	public Box2DBallTest(World world, float x, float y) {
		// Ball
		this.world = world;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		
		Body body = world.createBody(bodyDef);
		
		CircleShape circle = new CircleShape();
		circle.setRadius(0.5f);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.02f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
		
		@SuppressWarnings("unused")
		Fixture fixture = body.createFixture(fixtureDef);
		
		circle.dispose();
	}
}
