package com.skyhouse.projectrpg.utils.scene;

import java.util.HashMap;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.skyhouse.projectrpg.scene.Scene;

/**
 *  Manage {@link Scene}.
 * @author Meranote
 */
public class SceneManager {
	
	private HashMap<String, Scene> scenes;
	private Scene currentscene;
	
	/**
	 * Construct a SceneManager.
	 */
	public SceneManager() {
		scenes = new HashMap<String, Scene>();
	}
	
	/**
	 * Add scene to manager.
	 * @param name
	 * @param scene
	 */
	public void addScene(String name, Scene scene) {
		scenes.put(name, scene);
	}
	
	/**
	 * Get scene.
	 * @param name
	 * @param type
	 * @return Given extend scene class.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getScene(String name, Class<T> type) {
		T scene = (T)scenes.get(name);
		if(scene == null) throw new GdxRuntimeException("No given scene added.");
		return scene;
	}
	
	/**
	 * Get scene.
	 * @param name
	 * @param type
	 * @return Given extend scene class.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getCurrentScene(Class<T> type) {
		return (T)currentscene;
	}
	
	/**
	 * Delete scene.
	 * @param name
	 */
	public void deleteScene(String name) {
		scenes.remove(name);
	}
	
	/**
	 * Set current scene.
	 * @param name
	 */
	public void setUseScene(String name) {
		currentscene = scenes.get(name);
		if(currentscene == null) throw new GdxRuntimeException("No given scene added.");
		currentscene.start();
	}
	
	/**
	 * Call the resize() of curren scene. 
	 * @see Scene#resize(int, int) 
	 */
	public void resize(int screenwidth, int screenheight) {
		if(currentscene == null) throw new GdxRuntimeException("Set scene before call this.");
		currentscene.updateViewport(screenwidth, screenheight);
	}
	
	/**
	 * Update the current scene.
	 * @param deltatime
	 */
	public void update(float deltatime) {
		if(currentscene == null) throw new GdxRuntimeException("Set scene before call this.");
		currentscene.update(deltatime);
	}
	
	/**
	 * Draw the current scene.
	 * @param deltatime
	 */
	public void draw(float deltatime) {
		if(currentscene == null) throw new GdxRuntimeException("Set scene before call this.");
		currentscene.draw(deltatime);
	}
	
	/**
	 * Update and draw the current.
	 * @param deltatime
	 */
	public void updateAndDraw(float deltatime) {
		if(currentscene == null) throw new GdxRuntimeException("Set scene before call this.");
		currentscene.updateAndDraw(deltatime);
	}

}
