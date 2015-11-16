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
	private boolean walkflag, jumpflag, fallflag, jumprelease = false, dashflag = false, dashProcessed = false, enablesecondaryjump = false, processedsecondaryjump = false;
	
	private float speed = 2.5f;
	
	private float accumulatordashspeed = 0f;
	
	/**
	 * Construct a new character object.
	 * @param world
	 * @param data
	 */
	public B2DCharacter(World world, CharacterData data) {
		super(world, new Vector2(data.x, data.y), new Vector2(0.7f, 1.85f), BodyType.DynamicBody, 0.5f);
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
		data.state = ActionState.IDLE;
		
		if(input.left || input.right) {
			if(input.left) data.flipX = true;
			else if(input.right) data.flipX = false;
			float x_value = input.xAxisValue;
			if(x_value > 1) x_value = 1f;
			move(speed * x_value);
			data.state = ActionState.WALK;
		} else {
			stop();
		}
		
		if(input.jump) {
			if(!jumpflag && !fallflag) {
				jump();
				data.state = ActionState.JUMP;
			}
			if(enablesecondaryjump && !processedsecondaryjump && jumprelease) {
				jump();
				data.state = ActionState.JUMP;
				processedsecondaryjump = true;
			}
			jumprelease = false;
		} else {
			jumprelease = true;
		}
		
		if(input.dash && !dashflag) {
			if(!dashProcessed) {
				dash();
			}
		} else {
			dashProcessed = false;
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
			if(!input.jump) {
				fallflag = false;
			}
			enablesecondaryjump = false;
			processedsecondaryjump = false;
			if(walkflag) data.state = ActionState.WALK;
			else data.state = ActionState.IDLE;
		}
		
		if(body.getLinearVelocity().y >= 1f || body.getLinearVelocity().y <= -1f) {
			enablesecondaryjump = true;
		}
		
		if(dashflag) {
			if(data.flipX) body.setLinearVelocity(body.getLinearVelocity().x - accumulatordashspeed, body.getLinearVelocity().y);
			else body.setLinearVelocity(body.getLinearVelocity().x + accumulatordashspeed, body.getLinearVelocity().y);
			data.state = ActionState.DASH;
			accumulatordashspeed -= 0.35f;
			if(accumulatordashspeed < 0f) {
				if(!input.dash) {
					dashflag = false;
				}
				accumulatordashspeed = 0f;
				data.state = ActionState.IDLE;
			}
		}
		
		data.x = body.getPosition().x;
		data.y = body.getPosition().y - (1.85f / 2f);
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
		move(0, true);
	}
	
	private void jump() {
		body.setLinearVelocity(body.getLinearVelocity().x, 0f);
		body.applyLinearImpulse(0f, 3.8f, body.getPosition().x, body.getPosition().y, true);
		jumpflag = true;
	}
	
	private void dash() {
		accumulatordashspeed = speed * 4.5f;
		dashProcessed = true;
		dashflag = true;
	}
	
	/**
	 * @return {@link CharacterData}
	 */
	public CharacterData getData() {
		return data;
	}
	
}
