package com.skyhouse.projectrpg.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.skyhouse.projectrpg.ProjectRPG;

/**
 * Start scene. <br>
 * <b>First scene when application start.</b>
 * @author Meranote
 */
public class StartScene extends Scene {
	
	private float screenwidth, screenheight;
	private float currentProgress;
	
	/**
	 * Construct a new start scene.
	 */
	public StartScene() {
		addViewport(new ScreenViewport());
		currentProgress = 0f;
	}
	
	@Override
	public void change() {
		
	}

	@Override
	public void update(float deltatime) {
		screenwidth = getViewport(ScreenViewport.class).getWorldWidth();
		screenheight = getViewport(ScreenViewport.class).getWorldHeight();
		
		currentProgress = ProjectRPG.client.assetmanager.getProgress();
		if(currentProgress >= 1f && ProjectRPG.client.network.net.isConnected()) {
			Gdx.app.log("DEBUG", "load complete!");
			ProjectRPG.client.scenemanager.setUseScene(HomeScene.class);
		}
	}

	@Override
	public void draw(float deltatime) {
		useViewport(ScreenViewport.class);
		
		renderer.begin(ShapeType.Filled);
			renderer.setColor(Color.WHITE);
			renderer.rect(screenwidth * 0.05f, screenheight * 0.08f, (screenwidth * 0.90f) * currentProgress, 50);
		renderer.end();
	}

	@Override
	public void dispose() {

	}

}
