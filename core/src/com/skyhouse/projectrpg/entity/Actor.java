package com.skyhouse.projectrpg.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Abstract actor class.
 * @author Meranote
 */
public abstract class Actor {
	
	/**
	 * Update with given deltatime.
	 */
	public abstract void update(float deltatime);
	/**
	 * Draw.
	 */
	public abstract void draw(SpriteBatch batch);
	/**
	 * Dispose to free memory.
	 */
	public abstract void dispose();

}
