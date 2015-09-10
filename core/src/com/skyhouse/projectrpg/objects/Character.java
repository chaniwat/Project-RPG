package com.skyhouse.projectrpg.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Animation;
import com.skyhouse.projectrpg.physics.Box2DSquareShape;
import com.skyhouse.projectrpg.utils.spriter.SpriterPlayerListener;

public class Character {
	
	SpriterActor actor;
	Body characterBody;
	boolean isIdle,
			isProcessedIdle,
			isCrouch,
			isProcessedCrouch,
			isWalking,
			isProcessedWalk,
			isJumping,
			isFalling,
			isProcessedFall,
			isFlip;
	
	public Character(String pathtoscml, Vector2 position, float height) {		
		
		actor = new SpriterActor(pathtoscml);		
		float scale = height / actor.getPlayer().getBoundingRectangle(null).size.height;
		
		actor.getPlayer().setWeight(0.0f);
		actor.getPlayer().setScale(scale);
		
		// Character
		characterBody = new Box2DSquareShape((actor.getPlayer().getBoundingRectangle(null).size.width * scale) / 2f, height / 2f, getX() + position.x, getY() + position.y).getBody();
		characterBody.getFixtureList().first().setFriction(0);
		characterBody.getFixtureList().first().setRestitution(0);
		
		actor.addPlayerListener(new SpriterPlayerListener() {
			@Override
			public void animationFinished(Animation animation) {
				if(animation.name.equals("jump_start")) {
					actor.getFirstPlayer().setAnimation("jump_loop");
					actor.changeAnimationTo("jump_loop");
				}
				if(animation.name.equals("fall_start")) {
					actor.getFirstPlayer().setAnimation("fall_loop");
					actor.changeAnimationTo("fall_loop");
				}/*
				if(animation.name.equals("crouch_start")) {
					if(isCrouch) {
						actor.getFirstPlayer().setAnimation("crouch_loop");
						actor.changeAnimationTo("crouch_loop");						
					}
				}*/
			}
			
			@Override
			public void animationChanged(Animation oldAnim, Animation newAnim) {
				if(oldAnim.name.equals("fall_start")) {
					actor.getPlayer().setWeight(0.0f);
				}
				
				if(oldAnim.name.equals("walk")) {
					if(actor.getPlayer().getWeight() > 0.5f) actor.getFirstPlayer().setAnimation("walk");
					actor.getPlayer().setWeight(0.0f);
				}
				
				if(oldAnim.name.equals("idle")) {
					actor.getPlayer().setWeight(0.0f);
				}
				
				if(oldAnim.name.equals("crouch_loop")) {
					actor.getPlayer().setWeight(0.0f);
				}
			}
		});
	}
	
	public void update() {
		isIdle = true;
		
		if(isCrouch && !isWalking && !isJumping && !isFalling) {
			isIdle = false;
			if(!isProcessedCrouch) {
				actor.changeAnimationTo("crouch_loop");
				isProcessedCrouch = true;
			}
		} else {
			isProcessedCrouch = false;
		}
		
		if(isWalking) {
			isIdle = false;
			if(!isProcessedWalk && !isJumping && !isFalling) {
				actor.changeAnimationTo("walk");
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
				actor.changeAnimationTo("fall_start");
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
				actor.changeAnimationTo("idle");
				isProcessedIdle = true;
			}
		} else {
			isProcessedIdle = false;
		}
		
		actor.getPlayer().setPosition(characterBody.getPosition().x, characterBody.getPosition().y - 1f);
		actor.updateTweener();
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
	
	public void crouch() {
		isCrouch = true;
	}
	
	public void stand() {
		isCrouch = false;
	}
	
	public void stopWalk() {
		characterBody.setLinearVelocity(0, characterBody.getLinearVelocity().y);
		isWalking = false;
	}
	
	public void jump() {
		if(!isJumping && !isFalling) {
			characterBody.applyLinearImpulse(0, 5.0f, characterBody.getPosition().x, characterBody.getPosition().y, true);
			actor.changeAnimationTo("jump_start");
			isJumping = true;
		}
	}
	
	public float getX() {
		return actor.getPlayer().getX();
	}
	
	public float getY() {
		return actor.getPlayer().getY();
	}
	
	public String getCurrentAnimation() {
		return actor.c_animation;
	}
}
