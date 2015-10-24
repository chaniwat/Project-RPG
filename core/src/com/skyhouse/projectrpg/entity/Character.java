package com.skyhouse.projectrpg.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.data.Data.ActionState;
import com.skyhouse.projectrpg.physics.B2DCharacter;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;

/**
 * Character class. Contain a sprtier and physic body.
 * @author Meranote
 */
public class Character extends Actor {
	
	private CharacterData data;
	private B2DCharacter body;
	private SpriterPlayer player;
	private boolean isControlled = false;
	private boolean flipflag = false;
	
	/**
	 * Construct a new {@link Character} that not control by player.
	 * @param spriter
	 * @param data
	 */
	public Character(SpriterPlayer spriter, CharacterData data) {
		this(false, null, spriter, data);
	}
	
	/**
	 * Construct a new {@link Character} that control by player.
	 * @param world
	 * @param player
	 * @param data
	 */
	public Character(World world, SpriterPlayer player, CharacterData data) {
		this(true, world, player, data);
	}
	
	/**
	 * Construct a new {@link Character}.
	 * @param isControlled is this character control by player.
	 * @param world
	 * @param player
	 * @param data
	 */
	protected Character(boolean isControlled, World world, SpriterPlayer player, CharacterData data) {
		this.isControlled = isControlled;
		this.data = data;
		this.player = player;
		this.player.setScale(2.7f / player.getBoundingRectangle(null).size.height);
		
		if(isControlled) body = new B2DCharacter(world, data);
	}
	
	@Override
	public void update(float deltatime) {
		if(isControlled) body.update();
		
		switch(data.state) {
			case IDLE:
				player.setNewAnimation("idle");
				break;
			case WALK:
				player.setNewAnimation("walk");
				break;
			case JUMP:
				player.setNewAnimation("jump");
				break;
			case FALL:
				player.setNewAnimation("fall");
				break;
			default:
				break;
		}
		
		if(flipflag != data.flipX) {
			flipflag = data.flipX;
			player.flipX();
		}
		
		player.update(deltatime);
		player.setPosition(data.x, data.y);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		player.draw();
	}
	
	/**
	 * Update this character by using given {@link CharacterData}.
	 */
	public void updateCharacterByData(CharacterData data) {
		setPostion(data.x, data.y);
		setFlipX(data.flipX);
		setState(data.state);
	}
	
	/**
	 * Set the world position of this character.
	 */
	public void setPostion(float x, float y) {
		this.data.x = x;
		this.data.y = y;
	}
	
	/**
	 * Set horizontal flip of this character.
	 */
	public void setFlipX(boolean filpX) {
		this.data.flipX = filpX;
	}
	
	/**
	 * Set state of this character.
	 */
	public void setState(ActionState state) {
		this.data.state = state;
	}
	
	/**
	 * Get data of this character.
	 * @return {@link CharacterData}
	 */
	public CharacterData getData() {
		return data;
	}

	@Override
	public void dispose() {
		if(isControlled) body.dispose();
	}
	
}