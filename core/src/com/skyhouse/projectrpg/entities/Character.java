package com.skyhouse.projectrpg.entities;

import com.brashmonkey.spriter.PlayerTweener;
import com.skyhouse.projectrpg.actor.SpriterActor;
import com.skyhouse.projectrpg.entities.data.CharacterData;

public class Character extends CharacterData {
	
	private SpriterActor actor;
	
	private boolean idleflag,
								   walkflag,
								   jumpflag,
								   fallflag,
								   flipflag = false;
	
	public Character(CharacterData data) {
		super(data);
		
		actor = new SpriterActor("entities/GreyGuy/player.scml");
		actor.getPlayer().setScale(2.7f / actor.getPlayer().getBoundingRectangle(null).size.height);
	}
	
	public void update(float deltaTime) {		
		switch(actionstate) {
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
		
		if(flipflag != isFlipX()) {
			flipflag = isFlipX();
			actor.getPlayer().flipX();
		}
		
		actor.updateTweener(deltaTime);
		actor.getPlayer().setPosition(getPositionX(), getPositionY());
	}
	
	public PlayerTweener getSpriterPlayer() {
		return actor.getPlayer();
	}
	
	public void dispose() {
		actor.dispose();
	}
}