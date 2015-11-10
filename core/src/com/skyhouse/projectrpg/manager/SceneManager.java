package com.skyhouse.projectrpg.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.scene.Scene;

/**
 *  Manage {@link Scene}.
 * @author Meranote
 */
public class SceneManager {
	
	private HashMap<Class<? extends Scene>, Scene> scenes;
	private Class<? extends Scene> currentscene;
	private boolean isChanged = false;
	
	/**
	 * Construct a SceneManager.
	 */
	public SceneManager() {
		scenes = new HashMap<Class<? extends Scene>, Scene>();
	}
	
	/**
	 * Add scene to manager.
	 * @param name
	 * @param scene
	 */
	public <T extends Scene> void addScene(Class<T> sceneClass, Object... args) {
		if(scenes.containsKey(sceneClass)) {
			Gdx.app.log(ProjectRPG.TITLE, "Already add this scene");
			return;
		}
		try {
			T o = sceneClass.getDeclaredConstructor().newInstance(args);
			scenes.put(sceneClass, o);
		} catch (InstantiationException e) {
			throw new GdxRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new GdxRuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new GdxRuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new GdxRuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new GdxRuntimeException(e);
		} catch (SecurityException e) {
			throw new GdxRuntimeException(e);
		}
	}
	
	/**
	 * Get scene.
	 * @param name
	 * @param type
	 * @return Given extend scene class.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Scene> T getScene(Class<T> sceneClass) {
		T scene = (T)scenes.get(sceneClass);
		if(scene == null) throw new GdxRuntimeException("No given scene added.");
		return scene;
	}
	
	/**
	 * Get scene.
	 * @param name
	 * @param type
	 * @return Given extend scene class.
	 */
	public Scene getCurrentScene() {
		if(currentscene == null) throw new GdxRuntimeException("No current scene set.");
		return scenes.get(currentscene);
	}
	
	/**
	 * Delete scene.
	 * @param name
	 */
	public <T extends Scene> void deleteScene(Class<T> sceneClass) {
		if(scenes.get(sceneClass) == null) {
			Gdx.app.error(ProjectRPG.TITLE, "Cannot delete : no given scene added");
			return;
		}
		scenes.remove(sceneClass).dispose();
	}
	
	/**
	 * Set current scene.
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	public <T extends Scene> void setUseScene(Class<T> sceneClass) {
		currentscene = sceneClass;
		T o = (T)scenes.get(sceneClass);
		if(o == null) throw new GdxRuntimeException("No given scene added.");
		isChanged = true;
	}
	
	/**
	 * Call the resize() of curren scene. 
	 * @see Scene#resize(int, int) 
	 */
	public void resize(int screenwidth, int screenheight) {
		if(currentscene == null) throw new GdxRuntimeException("Set scene before call this.");
		scenes.get(currentscene).updateViewport(screenwidth, screenheight);
	}
	
	/**
	 * Update the current scene.
	 * @param deltatime
	 */
	public void update(float deltatime) {
		if(currentscene == null) throw new GdxRuntimeException("Set scene before call this.");
		if(isChanged) {
			scenes.get(currentscene).updateViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			scenes.get(currentscene).change();
			isChanged = false;
		}
		scenes.get(currentscene).update(deltatime);
	}
	
	/**
	 * Update the current scene with fixed time step.
	 */
	public void updateFixed(float fixedtime) {
		if(currentscene == null) throw new GdxRuntimeException("Set scene before call this.");
		scenes.get(currentscene).updateFixed(fixedtime);
	}
	
	/**
	 * Draw the current scene.
	 * @param deltatime
	 */
	public void draw(float deltatime) {
		if(currentscene == null) throw new GdxRuntimeException("Set scene before call this.");
		scenes.get(currentscene).draw(deltatime);
	}
	
	/**
	 * Update and draw the current.
	 * @param deltatime
	 */
	public void updateAndDraw(float deltatime) {
		if(currentscene == null) throw new GdxRuntimeException("Set scene before call this.");
		if(isChanged) {
			scenes.get(currentscene).change();
			scenes.get(currentscene).updateViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		scenes.get(currentscene).updateAndDraw(deltatime);
	}

	/**
	 * Release all allocated resource.
	 */
	public void dispose() {
		for(Scene scene : scenes.values()) {
			scene.dispose();
		}
		scenes.clear();
	}
	
}
