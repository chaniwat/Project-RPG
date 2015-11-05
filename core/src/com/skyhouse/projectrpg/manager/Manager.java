package com.skyhouse.projectrpg.manager;

import com.badlogic.gdx.utils.Disposable;

/**
 * Abstract class manager.
 * @author Meranote
 */
public abstract class Manager implements Disposable{

	/** Update the manager. */
	public abstract void update(float deltaTime);
	/** Update the manager with fixed time step. */
	public void updateFixed(float fixedTime) { }
	/** Release the resource. */
	public abstract void dispose();
	
}
