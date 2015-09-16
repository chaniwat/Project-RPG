package com.skyhouse.projectrpg.graphics;

import com.badlogic.gdx.Gdx;
import com.brashmonkey.spriter.PlayerTweener;
import com.brashmonkey.spriter.Spriter;

/**
 * SpriterActor for Spriter
 * @author Meranote
 *
 */
public class SpriterActor {

	PlayerTweener player;
	String newanimation;
	
	/**
	 * Create a new SpriterActor for character, monster, etc. 
	 * @param pathtoscml path to .scml file
	 */
	public SpriterActor(String pathtoscml) {
		Spriter.load(Gdx.files.internal(pathtoscml).read(), pathtoscml);
		player = (PlayerTweener)Spriter.newPlayer(pathtoscml, 0, PlayerTweener.class);
		player.setWeight(0.0f);
	}
	
	/**
	 * Set new animation to changed to
	 * @param newAnimation animation name
	 */
	public void setNewAnimation(String newanimation) {
		this.newanimation  = newanimation;
		getPlayer().getSecondPlayer().setTime(0);
	}
	
	/**
	 * Update the tweener animation with default speed (4)
	 * @param deltaTime
	 */
	public void updateTweener(float deltaTime) {
		updateTweener(deltaTime, 4);
	}
	
	/**
	 * Update the tweener animation with given speed
	 * @param deltaTime
	 * @param speed
	 */
	public void updateTweener(float deltaTime, float speed) {
		if(player.getFirstPlayer().getAnimation().name.equals(newanimation)) {
			if(player.getWeight() > 0f) player.setWeight(player.getWeight() - (speed * deltaTime));
			else if(player.getWeight() < 0f) player.setWeight(0f);
		} else {
			player.getSecondPlayer().setAnimation(newanimation);
			if(player.getWeight() < 1f) player.setWeight(player.getWeight() + (speed * deltaTime));
			else if(player.getWeight() > 1f) {
				player.setWeight(0f);
				player.getFirstPlayer().setAnimation(newanimation);
			}
		}
	}
	
	/**
	 * Get the {@link PlayerTweener} of this actor
	 * @return {@link PlayerTweener} Player
	 */
	public PlayerTweener getPlayer() {
		return player;
	}
	
	/**
	 * Clear this SpriterActor
	 */
	public void dispose() {
		Spriter.removePlayer(player);
	}
}