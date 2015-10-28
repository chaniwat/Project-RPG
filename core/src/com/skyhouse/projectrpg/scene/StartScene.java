package com.skyhouse.projectrpg.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.graphics.viewports.ScreenViewport;

/**
 * Start scene. <br>
 * <b>First scene when application start.</b>
 * @author Meranote
 */
public class StartScene extends Scene {

	private ShapeRenderer renderer = ProjectRPG.Client.graphic.renderer;
	
	/**
	 * Construct a new start scene.
	 */
	public StartScene() {
		addViewport(new ScreenViewport());
	}
	
	@Override
	public void change() {
		
	}

	@Override
	public void update(float deltatime) {
		if(ProjectRPG.Client.assetmanager.isLoaded("mapdata/L01.map")) {
			ProjectRPG.Client.scenemanager.setUseScene(GameScene.class);
		}
	}

	@Override
	public void draw(float deltatime) {
		useViewport(ScreenViewport.class);
		
		renderer.begin(ShapeType.Filled);
			renderer.setColor(Color.WHITE);
			renderer.rect(0, 0, getViewport(ScreenViewport.class).getWorldWidth(), getViewport(ScreenViewport.class).getWorldHeight());
		renderer.end();
	}

	@Override
	public void dispose() {

	}

}
