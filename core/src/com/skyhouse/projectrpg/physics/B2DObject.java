package com.skyhouse.projectrpg.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Box2D Base object.
 * @author Meranote
 */
public abstract class B2DObject {
	
	protected World world;
	protected Body body;
	protected Fixture fixture;
	
	/**
	 * Construct a new Box2D Object.
	 * @param world
	 * @param position
	 * @param size
	 * @param type
	 * @param density
	 */
	public B2DObject(World world, Vector2 position, Vector2 size, BodyType type, float density) {
		this.world = world;
		BodyDef bodydef = new BodyDef();
		bodydef.type = type;
		bodydef.position.set(position.x , position.y);
		
		body = world.createBody(bodydef);
		
		PolygonShape box = new PolygonShape();
		box.setAsBox(size.x / 2f, size.y / 2f);
		fixture = body.createFixture(box, density);
		
		box.dispose();
	}
	
	/**
	 * Get this object body.
	 * @return {@link Body}
	 */
	public Body getBody() {
		return body;
	}
	
	/** Update this object. */
	public abstract void update();
	
	/**
	 * Destroy this object.
	 */
	public void dispose() {
		world.destroyBody(body);
	}
	
}
