package com.skyhouse.projectrpg.graphics.viewports;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skyhouse.projectrpg.ProjectRPGGame.ProjectRPG;
import com.skyhouse.projectrpg.entities.Character;

public class GameplayViewport extends Viewport {
	
	private float viewWorldSize;
	private float viewWorldScale;
	private boolean isUpdated;

	public GameplayViewport(float viewSize) {
		viewWorldSize = viewSize;
		viewWorldScale = 1f;
		setCamera(new OrthographicCamera());
	}
	
	@Override
	public void update(int screenWidth, int screenHeight, boolean centerCamera) {
		float viewportWidth = (viewWorldSize * ((float)screenWidth / (float)screenHeight)) * viewWorldScale;
		float viewportHeight = viewWorldSize * viewWorldScale;
		
		Gdx.app.log(ProjectRPG.TITLE, "w : "+viewportWidth+" , h : "+viewportHeight);
		setWorldSize(viewportWidth, viewportHeight);
		setScreenBounds(0, 0, screenWidth, screenHeight);
		
		isUpdated = true;
	}
	
	public void setViewCenterToCharacter(Character character, float offsetX, float offsetY) {
		if(!character.equals(null)) {
				getCamera().position.set(character.getPositionX() + offsetX, character.getPositionY() + offsetY, 0.0f);
				getCamera().update();
		}
	}
	
	public void setViewSize(float viewSize) {
		viewWorldSize = viewSize;
		
		if(isUpdated) {
			update(getScreenWidth(), getScreenHeight());
		}
	}
	
	public void setViewScale(float viewScale) {
		viewWorldScale = viewScale;
		
		if(isUpdated) {
			update(getScreenWidth(), getScreenHeight());
		}
	}
	
}