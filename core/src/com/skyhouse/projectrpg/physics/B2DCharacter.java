package com.skyhouse.projectrpg.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.skyhouse.projectrpg.data.ActorData.ActionState;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.data.InputData;

/**
 * Box2D Character object.
 * @author Meranote
 *
 */
public class B2DCharacter extends B2DObject {

	private CharacterData data;
	private boolean walkflag, jumpflag, fallflag;
	
	/**
	 * Construct a new character object.
	 * @param world
	 * @param data
	 */
	public B2DCharacter(World world, CharacterData data) {
		super(world, new Vector2(data.x, data.y), new Vector2(0.93f, 2f), BodyType.DynamicBody, 0.5f);
		body.setFixedRotation(true);
		fixture.setFriction(0);
		fixture.setRestitution(0);
		
		Filter filterdef = new Filter();
		filterdef.groupIndex = -1;
		fixture.setFilterData(filterdef);
		
		this.data = data;
	}
	
	/**
	 * Don't use this update method, use {@link #update(InputData)} instead.
	 */
	@Override @Deprecated
	public void update() {
		new GdxRuntimeException("B2DCharacter update called, use update(InputData) instead.").printStackTrace();
	}
	
	/**
	 * Update the character object with input data.
	 * @param input
	 */
	public void update(InputData input) {
		float speed = 2.5f;
		
		data.state = ActionState.IDLE;
		
		if(input.leftPressed || input.rightPressed) {
			if(input.leftPressed) data.flipX = true;
			else if(input.rightPressed) data.flipX = false;
			float x_value = input.xAxisValue;
			if(x_value > 1) x_value = 1f;
			move(speed * x_value);
			data.state = ActionState.WALK;
		} else {
			stop();
		}
		
		if(input.jumpPressed) {
			if(!jumpflag && !fallflag) {
				jump();
				data.state = ActionState.JUMP;
			}
		}
		
		if(jumpflag) {
			data.state = ActionState.JUMP;
		}
		
		if(body.getLinearVelocity().y < -1f) {
			jumpflag = false;
			fallflag = true;
			data.state = ActionState.FALL;
		}
		
		if(body.getLinearVelocity().y > -1f && body.getLinearVelocity().y < 1f  && !jumpflag) {
			fallflag = false;
			if(walkflag) data.state = ActionState.WALK;
			else data.state = ActionState.IDLE;
		}
		
		data.x = body.getPosition().x;
		data.y = body.getPosition().y - 1;
	}
	
	private void move(float speed, boolean stop) {
		if(data.flipX) body.setLinearVelocity(-speed, body.getLinearVelocity().y);
		else body.setLinearVelocity(speed, body.getLinearVelocity().y);
		walkflag = !stop;
	}
	
	private void move(float speed) {
		move(speed, false);
	}
	
	private void stop() {
		move(0f, true);
	}
	
	private void jump() {
		body.applyLinearImpulse(0f, 5f, body.getPosition().x, body.getPosition().y, true);
		jumpflag = true;
	}
	
	/**
	 * @return {@link CharacterData}
	 */
	public CharacterData getData() {
		return data;
	}
	
}
