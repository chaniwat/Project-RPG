package com.skyhouse.projectrpg.scene;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skyhouse.projectrpg.ProjectRPG;

/**
 * Scene.
 * @author Meranote
 */
public abstract class Scene {
	
	protected SpriteBatch batch;
	protected ShapeRenderer renderer;
	private HashMap<Class<? extends Viewport>, Viewport> viewports = new HashMap<Class<? extends Viewport>, Viewport>();
		
	/**
	 * Construct a new {@link Scene}.
	 * @param batch
	 * @param assetmanager
	 */
	public Scene() {
		this.batch = ProjectRPG.Client.graphic.batch;
		this.renderer = ProjectRPG.Client.graphic.renderer;
	}
	
	/**
	 * Add a {@link Viewport} to use in render.
	 * @param name
	 * @param viewport
	 * @return 
	 */
	public final void addViewport(Viewport viewport) {
		if(viewports.containsKey(viewport.getClass())) {
			Gdx.app.log(ProjectRPG.TITLE, "Already add this viewport");
			return;
		}
		viewports.put(viewport.getClass(), viewport);
	}
	
	/**
	 * Get a viewport.
	 * @param name
	 * @return {@link Viewport}
	 */
	@SuppressWarnings("unchecked")
	public final <T extends Viewport> T getViewport(Class<T> viewportClass) {
		T o = (T)viewports.get(viewportClass);
		if(o == null) throw new GdxRuntimeException("no given viewport added.");
		return o;
	}
	
	/** Use the given viewport.  */
	@SuppressWarnings("unchecked")
	public final <T extends Viewport> void useViewport(Class<T> viewportClass) {
		T o = (T)viewports.get(viewportClass); 
		if(o == null) throw new GdxRuntimeException("no given viewport added.");
		o.apply();
		batch.setProjectionMatrix(o.getCamera().combined);
		renderer.setProjectionMatrix(o.getCamera().combined);
	}

	/** Update viewport. */
	public final void updateViewport(int screenwidth, int screenheight) {
		for(Viewport viewport : this.viewports.values()) {
			viewport.update(screenwidth, screenheight);
		}
	}
	
	/** Call everytime when scene is changed to. Call before {@link #update(float)} and {@link #draw(float)}. */
	public abstract void change();
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
