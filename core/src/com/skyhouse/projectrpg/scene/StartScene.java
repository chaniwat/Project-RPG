package com.skyhouse.projectrpg.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.graphics.viewports.UIViewport;

public class StartScene extends Scene {

	private ShapeRenderer renderer = ProjectRPG.Client.graphic.renderer;
	
	public StartScene() {
		addViewport(new UIViewport());
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
		useViewport(UIViewport.class);
		
		renderer.begin(ShapeType.Filled);
			renderer.setColor(Color.WHITE);
			renderer.rect(0, 0, getViewport(UIViewport.class).getWorldWidth(), getViewport(UIViewport.class).getWorldHeight());
		renderer.end();
	}

	@Override
	public void dispose() {

	}

}
