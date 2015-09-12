package com.skyhouse.projectrpg.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.brashmonkey.spriter.Animation;
import com.skyhouse.projectrpg.graphics.SpriterActor;
import com.skyhouse.projectrpg.physics.BodyTemplate;
import com.skyhouse.projectrpg.utils.spriter.SpriterPlayerListener;

public class Character {
	
	public enum CharacterState {
		IDLE,
		WALKING,
		JUMPING,
		FALLING,
	}
	
	SpriterActor actor;
	Body characterBody;
	boolean isIdle,
			isProcessedIdle,
			isWalking,
			isProcessedWalk,
			isJumping,
			isFalling,
			isProcessedFall,
			isFlip;
	
	public Character(String pathtoscml, Vector2 position, float height) {	
		actor = new SpriterActor(pathtoscml);
		actor.getPlayer().setScale(height / actor.getPlayer().getBoundingRectangle(null).size.height);
		
		characterBody = new BodyTemplate(position, new Vector2(0.93f, 2f), BodyType.DynamicBody, 0.5f).getBody();
		characterBody.setFixedRotation(true);
		characterBody.getFixtureList().first().setFriction(0);
		characterBody.getFixtureList().first().setRestitution(0);
		characterBody.getFixtureList().first().getFilterData().groupIndex = -1;
		
		actor.getPlayer().getSecondPlayer().addListener(new SpriterPlayerListener() {
			@Override
			public void animationChanged(Animation oldAnim, Animation newAnim) {
				if(oldAnim.name.equals("fall")) {
					actor.getPlayer().setWeight(0.0f);
				}
			}
		});
	}
	
	public void update(float deltaTime) {
		isIdle = true;
		
		if(isWalking) {
			isIdle = false;
			if(!isProcessedWalk && !isJumping && !isFalling) {
				actor.setNewAnimation("walk");
				isProcessedWalk = true;
			}
			if(isFlip) characterBody.setLinearVelocity(-2.5f, characterBody.getLinearVelocity().y);
			else characterBody.setLinearVelocity(2.5f, characterBody.getLinearVelocity().y);
		} else {
			characterBody.setLinearVelocity(0, characterBody.getLinearVelocity().y);
			isProcessedWalk = false;
		}
		
		if(characterBody.getLinearVelocity().y < -1f) {
			isFalling = true;
			isJumping = false;
		}
				
		if(isFalling && !isJumping) {
			if(!isProcessedFall) {
				actor.setNewAnimation("fall");
				isProcessedFall = true;
			}
			if(characterBody.getLinearVelocity().y < 1f && characterBody.getLinearVelocity().y > -1f) {
				isFalling = false;
				isProcessedWalk = false;
				actor.getPlayer().setPosition(getX(), 0f);
			}
		}
		else isProcessedFall = false;
		
		if(isJumping || isFalling) {
			isIdle = false;
		}
		
		if(isIdle) {
			if(!isProcessedIdle) {
				actor.setNewAnimation("idle");
				isProcessedIdle = true;
			}
		} else {
			isProcessedIdle = false;
		}
		
		actor.getPlayer().setPosition(characterBody.getPosition().x, characterBody.getPosition().y - 1f);
		actor.updateTweener(deltaTime);
	}
	
	public void walkLeft() {
		isWalking = true;
		if(!isFlip) {
			actor.getPlayer().flipX();
			characterBody.setLinearVelocity(0, characterBody.getLinearVelocity().y);
			isFlip = true;
		}
	}
	
	public void walkRight() {
		isWalking = true;
		if(isFlip) {
			actor.getPlayer().flipX();
			characterBody.setLinearVelocity(0, characterBody.getLinearVelocity().y);
			isFlip = false;
		}
	}
	
	public void stopWalk() {
		characterBody.setLinearVelocity(0, characterBody.getLinearVelocity().y);
		isWalking = false;
	}
	
	public void jump() {
		if(!isJumping && !isFalling) {
			characterBody.applyLinearImpulse(0, 5.0f, characterBody.getPosition().x, characterBody.getPosition().y, true);
			actor.setNewAnimation("jump");
			isJumping = true;
		}
	}
	
	public float getX() {
		return characterBody.getTransform().getPosition().x;
	}
	
	public float getY() {
		return characterBody.getTransform().getPosition().y - 1;
	}
	
	public void setPosition(float x, float y) {
		 characterBody.setTransform(x, y + 1, 0);
	}
	
	public CharacterState getCharacterState() {
		if(isWalking) return CharacterState.WALKING;
		else if(isJumping) return CharacterState.JUMPING;
		else if(isFalling) return CharacterState.FALLING;
		else return CharacterState.IDLE;
	}
	
	public int getCharacterStateIndex() {
		return getCharacterState().ordinal();
	}
	
	public void setCharacterState(CharacterState state) {
		resetState();
		switch(state) {
			case IDLE:
				break;
			case WALKING:
				isWalking = true;
				break;
			case JUMPING:
				isJumping = true;
				break;
			case FALLING:
				isFalling = true;
				break;
		}
	}
	
	public void setCharacterStateIndex(int state) {
		setCharacterState(CharacterState.values()[state]);
	}
	
	private void resetState() {
		isWalking = false;
		isFalling = false;
		isJumping = false;
	}
}