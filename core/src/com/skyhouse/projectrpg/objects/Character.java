package com.skyhouse.projectrpg.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.brashmonkey.spriter.Animation;
import com.skyhouse.projectrpg.graphics.SpriterActor;
import com.skyhouse.projectrpg.physics.BodyTemplate;
import com.skyhouse.projectrpg.utils.spriter.SpriterPlayerListener;

public class Character {
	
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
		
		// Character body
		characterBody = new BodyTemplate(position, new Vector2(0.93f, 2f), BodyType.DynamicBody, 0.5f).getBody();
		characterBody.setFixedRotation(true);
		characterBody.getFixtureList().first().setFriction(0);
		characterBody.getFixtureList().first().setRestitution(0);
		
		Filter filterDef = new Filter();
		filterDef.groupIndex = -1;
		
		characterBody.getFixtureList().first().setFilterData(filterDef);
		
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
		return actor.getPlayer().getX();
	}
	
	public float getY() {
		return actor.getPlayer().getY();
	}
}