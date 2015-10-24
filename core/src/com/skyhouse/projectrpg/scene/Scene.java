package com.skyhouse.projectrpg.scene;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.scene.input.SceneInput;

/**
 * Scene.
 * @author Meranote
 */
public abstract class Scene {
	
	protected AssetManager assetmanager;
	protected SceneManager scenemanager;
	protected SpriteBatch batch;
	protected ShapeRenderer renderer;
	protected SceneInput input;
	private HashMap<String, Viewport> viewport;
		
	/**
	 * Construct a new {@link Scene}.
	 * @param batch
	 * @param assetmanager
	 */
	public Scene() {
		this.assetmanager = ProjectRPG.Client.assetmanager;
		this.scenemanager = ProjectRPG.Client.scenemanager;
		this.batch = ProjectRPG.Client.graphic.batch;
		this.renderer = ProjectRPG.Client.graphic.renderer;
		viewport = new HashMap<String, Viewport>();
		input = new SceneInput();
	}
	
	/**
	 * Add a {@link Viewport} to use in render.
	 * @param name
	 * @param viewport
	 */
	public final void addViewport(String name, Viewport viewport) {
		this.viewport.put(name, viewport);
	}
	
	/**
	 * Get a viewport.
	 * @param name
	 * @return {@link Viewport}
	 */
	public final Viewport getViewport(String name) {
		return viewport.get(name);
	}
	
	/** Use the given viewport.  */
	public final void useViewport(String name) {
		viewport.get(name).apply();
		batch.setProjectionMatrix(viewport.get(name).getCamera().combined);
	}

	/** Update viewport. */
	public final void updateViewport(int screenwidth, int screenheight) {
		for(Viewport viewport : this.viewport.values()) {
			viewport.update(screenwidth, screenheight);
		}
	}
	
	/** First call before use this scene. */
	public abstract void start();
	/** Update scene. */
	public abstract void update(float deltatime);
	/** Draw scene. */
	public abstract void draw(float deltatime);
	/** Destroy scene. */
	public abstract void dispose();
	/** Call both {@link #update(float)} and {@link #draw(float)} in order. */
	public final void updateAndDraw(float deltatime) {
		update(deltatime);
		draw(deltatime);
	}
	
}
