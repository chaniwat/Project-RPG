package com.skyhouse.projectrpg.spriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.PlayerTweener;
import com.brashmonkey.spriter.SCMLReader;
import com.brashmonkey.spriter.Spriter;

/**
 * SpriterActor for Spriter.
 * @author Meranote
 *
 */
public class SpriterPlayer extends PlayerTweener {

	private String newanimation;
	private SpriterDrawer drawer;
	
	/**
	 * Create a new SpriterActor for character, monster, etc. 
	 * @param pathtoscml path to .scml file
	 */
	public SpriterPlayer(String pathToScml, SpriteBatch batch, ShapeRenderer renderer) {
		super();
		FileHandle handle = Gdx.files.internal(pathToScml);
		SCMLReader reader = new SCMLReader(handle.read());
		Data data = reader.getData();
		setPlayers(new Player(data.getEntity(0)), new Player(data.getEntity(0)));
		SpriterLoader loader = new SpriterLoader(data);
		loader.load(handle.file());
		drawer = new SpriterDrawer(loader, batch , renderer);
		setWeight(0.0f);
	}
	
	/**
	 * Set new animation to changed to.
	 * @param newanimation animation name
	 */
	public void setNewAnimation(String newanimation) {
		this.newanimation = newanimation;
		getSecondPlayer().setTime(0);
	}
	
	/**
	 * Update the tweener animation with default speed (4).
	 * @param deltaTime deltatime
	 */
	public void update(float deltaTime) {
		update(deltaTime, 4);
	}
	
	/**
	 * Update the tweener animation with given speed.
	 * @param deltaTime deltatime
	 * @param speed tween speed
	 */
	public void update(float deltaTime, float speed) {
		if(getFirstPlayer().getAnimation().name.equals(newanimation)) {
			if(getWeight() > 0f) setWeight(getWeight() - (speed * deltaTime));
			else if(getWeight() < 0f) setWeight(0f);
		} else {
			getSecondPlayer().setAnimation(newanimation);
			if(getWeight() < 1f) setWeight(getWeight() + (speed * deltaTime));
			else if(getWeight() > 1f) {
				setWeight(0f);
				getFirstPlayer().setAnimation(newanimation);
			}
		}
		update();
	}
	
	public void draw() {
		drawer.draw(this);
	}
	
	/**
	 * Clear this SpriterActor.
	 */
	public void dispose() {
		Spriter.removePlayer(this);
	}
}
