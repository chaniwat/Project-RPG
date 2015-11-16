package com.skyhouse.projectrpg.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.skyhouse.projectrpg.data.ActorData.ActionState;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.data.InputData;
import com.skyhouse.projectrpg.physics.B2DCharacter;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;
import com.skyhouse.projectrpg.utils.JobType;

/**
 * Character class. Contain a sprtier and physic body.
 * @author Meranote
 */
public class Character extends Actor {
	
	private CharacterData data;
	private B2DCharacter body;
	public B2DCharacter getBody() {
		return body;
	}

	private SpriterPlayer player;
	private InputData inputData;
	private boolean isControlled = false;
	private boolean flipflag = false;
	
	private JobType job;
	
	/**
	 * Construct a new {@link Character} that not control by player.
	 * @param spriter
	 * @param data
	 */
	public Character(CharacterData data) {
		this(false, null, data);
	}
	
	/**
	 * Construct a new {@link Character} that control by player.
	 * @param world
	 * @param player
	 * @param data
	 */
	public Character(World world, CharacterData data) {
		this(true, world, data);
	}
	
	/**
	 * Construct a new {@link Character}.
	 * @param isControlled is this character control by player.
	 * @param world
	 * @param player
	 * @param data
	 */
	protected Character(boolean isControlled, World world, CharacterData data) {
		inputData = new InputData();
		this.isControlled = isControlled;
		this.data = data;
		this.player = new SpriterPlayer("entity/character/character.scml");
		this.player.setScale(2.5f / player.getBoundingRectangle(null).size.height);
		
		if(isControlled) body = new B2DCharacter(world, data);
	}
	
	@Override
	public void update(float deltatime) {
		if(isControlled) body.update(inputData);
		
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
			case DASH:
				player.setNewAnimation("dash");
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
		setPosition(data.x, data.y);
		setFlipX(data.flipX);
		setState(data.state);
	}
	
	/**
	 * Update this character by using given {@link InputData}.
	 * <b>Use with control character.</b>
	 */
	public void updateCharacterByInputData(InputData data) {
		this.inputData = data;
	}
	
	/**
	 * Set the world position of this character.
	 */
	public void setPosition(float x, float y) {
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
	 * Set scale of this character.
	 * @param scale
	 */
	public void setScale(float scale) {
		player.setScale(scale);
	}
	
	/**
	 * Get data of this character.
	 * @return {@link CharacterData}
	 */
	public CharacterData getData() {
		return data;
	}
	
	/**
	 * Get Spriter player.
	 * @return
	 */
	public SpriterPlayer getSpriterPlayer() {
		return player;
	}

	public JobType getJob() {
		return job;
	}

	public void setJob(JobType job) {
		this.job = job;
	}

	@Override
	public void dispose() {
		if(isControlled) body.dispose();
	}
	
}