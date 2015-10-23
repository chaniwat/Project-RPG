package com.skyhouse.projectrpg.entity;

import com.badlogic.gdx.physics.box2d.World;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.physics.B2DCharacter;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;

public class Character {
	
	private SpriterPlayer player;
	private CharacterData data;
	private B2DCharacter body;
	private boolean idleflag,
					walkflag,
					jumpflag,
					fallflag,
					flipflag = false;
	
	public Character(World world, SpriterPlayer player, CharacterData data) {
		body = new B2DCharacter(world, data);
		this.data = data;
		this.player = player;
		this.player.setScale(2.7f / player.getBoundingRectangle(null).size.height);
	}
	
	public void update(float deltaTime) {
		body.update();
		
		switch(data.actionstate) {
			case IDLE:
				if(!idleflag) {
					player.setNewAnimation("idle");
					idleflag = true;
					walkflag = false;
					jumpflag = false;
					fallflag = false;
				}
				break;
			case WALK:
				if(!walkflag) {
					player.setNewAnimation("walk");
					idleflag = false;
					walkflag = true;
					jumpflag = false;
					fallflag = false;
				}
				break;
			case JUMP:
				if(!jumpflag) {
					player.setNewAnimation("jump");
					idleflag = false;
					walkflag = false;
					jumpflag = true;
					fallflag = false;
				}
				break;
			case FALL:
				if(!fallflag) {
					player.setNewAnimation("fall");
					idleflag = false;
					walkflag = false;
					jumpflag = false;
					fallflag = true;
				}
				break;
			default:
				break;
		}
		
		if(flipflag != data.flipX) {
			flipflag = data.flipX;
			player.flipX();
		}
		
		player.update(deltaTime);
		player.setPosition(data.x, data.y - 1f);
	}
	
	public void draw() {
		player.draw();
	}
	
	public B2DCharacter getBody() {
		return body;
	}
	
	public CharacterData getData() {
		return data;
	}
	
	public void dispose() {
		player.dispose();
	}
}