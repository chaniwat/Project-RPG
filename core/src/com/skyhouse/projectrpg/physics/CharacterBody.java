package com.skyhouse.projectrpg.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.skyhouse.projectrpg.ProjectRPGGame.ProjectRPG;
import com.skyhouse.projectrpg.objects.CharacterData;
import com.skyhouse.projectrpg.objects.CharacterData.CharacterState;

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
		fixture.getFilterData().groupIndex = -1;
		
		this.character = character;
	}
	
	@Override
	public void update() {
		float speed = 2.5f;
		
		switch(character.getState()) {
			case IDLE:
				stop();
				break;
			case WALK:
				move(speed);
				break;
			case JUMP:
				if(!jumpflag && !fallflag) {
					body.applyLinearImpulse(0f, 5f, body.getPosition().x, body.getPosition().y, true);
					jumpflag = true;
				}
				if(walkflag) move(speed);
				break;
			case FALL:
				if(walkflag) move(speed);
				fallflag = true;
				break;
			default: break;
		}
		
		if(body.getLinearVelocity().y < -1f) {
			jumpflag = false;
			character.setState(CharacterState.FALL);
		}
		
		if(body.getLinearVelocity().y > -1f && body.getLinearVelocity().y < 1f  && !jumpflag) {
			fallflag = false;
			if(walkflag) character.setState(CharacterState.WALK);
			else character.setState(CharacterState.IDLE);
		}
		
		character.setPosition(body.getPosition().x, body.getPosition().y);
	}
	
	private void move(float speed, boolean stop) {
		if(jumpflag) {
			character.setState(CharacterState.JUMP);
		} else if(fallflag) {
			character.setState(CharacterState.FALL);
		}
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

}
