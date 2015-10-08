package com.skyhouse.projectrpg.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.skyhouse.projectrpg.entities.data.CharacterData;
import com.skyhouse.projectrpg.entities.data.CharacterData.CharacterActionState;

public class CharacterBody extends BodyTemplate {

	private CharacterData character;
	private boolean walkflag,
								   jumpflag,
								   fallflag;
	
	public CharacterBody(CharacterData character) {
		super(new Vector2(character.getPositionX(), character.getPositionY()), new Vector2(0.93f, 2f), BodyType.DynamicBody, 0.5f);
		body.setFixedRotation(true);
		fixture.setFriction(0);
		fixture.setRestitution(0);
		
		Filter filterdef = new Filter();
		filterdef.groupIndex = -1;
		fixture.setFilterData(filterdef);
		
		this.character = character;
	}
	
	public CharacterData getData() {
		return character;
	}
	
	@Override
	public void update() {
		float speed = 2.5f;
		
		character.actionstate = CharacterActionState.IDLE;
		
		if(character.inputstate.left_flag || character.inputstate.right_flag) {
			if(character.inputstate.left_flag) character.setFilpX(true);
			else if(character.inputstate.right_flag) character.setFilpX(false);
			float x_value = character.inputstate.x_value;
			if(x_value > 1) x_value = 1f;
			move(speed * x_value);
			character.actionstate = CharacterActionState.WALK;
		} else {
			stop();
		}
		
		if(character.inputstate.jump_flag) {
			if(!jumpflag && !fallflag) {
				jump();
				character.actionstate = CharacterActionState.JUMP;
			}
		}
		
		if(jumpflag) {
			character.actionstate = CharacterActionState.JUMP;
		}
		
		if(body.getLinearVelocity().y < -1f) {
			jumpflag = false;
			fallflag = true;
			character.actionstate = CharacterActionState.FALL;
		}
		
		if(body.getLinearVelocity().y > -1f && body.getLinearVelocity().y < 1f  && !jumpflag) {
			fallflag = false;
			if(walkflag) character.actionstate = CharacterActionState.WALK;
			else character.actionstate = CharacterActionState.IDLE;
		}
		
		character.setPosition(body.getPosition().x, body.getPosition().y);
	}
	
	private void move(float speed, boolean stop) {
		if(character.isFlipX()) body.setLinearVelocity(-speed, body.getLinearVelocity().y);
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
	
}
