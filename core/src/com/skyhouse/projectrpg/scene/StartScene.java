package com.skyhouse.projectrpg.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.skyhouse.projectrpg.ProjectRPG;

/**
 * Start scene. <br>
 * <b>First scene when application start.</b>
 * @author Meranote
 */
public class StartScene extends Scene {
	
	private Texture loadingImage;
	private SpriteBatch batch;
	
	/**
	 * Construct a new start scene.
	 */
	public StartScene() {
		addViewport(new ScreenViewport());
		loadingImage = ProjectRPG.Client.assetmanager.get("texture/background/startscreenvillage.png", Texture.class);
		batch = ProjectRPG.Client.graphic.batch;
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
		
		batch.begin();
			batch.draw(loadingImage, 0, 0);
		batch.end();
	}

	@Override
	public void dispose() {

	}

}
