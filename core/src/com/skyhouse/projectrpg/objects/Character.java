package com.skyhouse.projectrpg.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.brashmonkey.spriter.Animation;
import com.skyhouse.projectrpg.ProjectRPGGame.ProjectRPG;
import com.skyhouse.projectrpg.graphics.SpriterActor;
import com.skyhouse.projectrpg.physics.BodyTemplate;
import com.skyhouse.projectrpg.physics.CharacterBody;
import com.skyhouse.projectrpg.utils.spriter.SpriterPlayerListener;

public class Character extends CharacterData {
	
	private SpriterActor actor;
	private CharacterBody body;
	
	private boolean idleflag,
								   walkflag,
								   jumpflag,
								   fallflag,
								   flipflag = false;
	
	public Character(int id, Vector2 position, float height) {	
		super(id, position.x, position.y, CharacterState.IDLE);
		
		actor = new SpriterActor("entities/GreyGuy/player.scml");
		actor.getPlayer().setScale(height / actor.getPlayer().getBoundingRectangle(null).size.height);
		
		body = new CharacterBody(this);
	}
	
	public void update(float deltaTime) {
		body.update();
		
		switch(getState()) {
			case IDLE:
				if(!idleflag) {
					actor.setNewAnimation("idle");
					idleflag = true;
					walkflag = false;
					jumpflag = false;
					fallflag = false;
				}
				break;
			case WALK:
				if(!walkflag) {
					actor.setNewAnimation("walk");
					idleflag = false;
					walkflag = true;
					jumpflag = false;
					fallflag = false;
				}
				break;
			case JUMP:
				if(!jumpflag) {
					actor.setNewAnimation("jump");
					idleflag = false;
					walkflag = false;
					jumpflag = true;
					fallflag = false;
				}
				break;
			case FALL:
				if(!fallflag) {
					actor.setNewAnimation("fall");
					idleflag = false;
					walkflag = false;
					jumpflag = false;
					fallflag = true;
				}
				break;
			default:
				break;
		}
		
		if(isFlipX() != flipflag) {
			flipflag = isFlipX();
			actor.getPlayer().flipX();
		}
		
		actor.updateTweener(deltaTime);
		actor.getPlayer().setPosition(getPositionX(), getPositionY());
	}
}