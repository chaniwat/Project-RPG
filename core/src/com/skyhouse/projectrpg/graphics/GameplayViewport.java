package com.skyhouse.projectrpg.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skyhouse.projectrpg.ProjectRPGGame.ProjectRPG;
import com.skyhouse.projectrpg.objects.Character;

public class GameplayViewport extends Viewport {
	
	private float viewWorldWidth, viewWorldHeight;

	public GameplayViewport(float viewWorldWidth, float viewWorldHeight) {
		this.viewWorldWidth = viewWorldWidth;
		this.viewWorldHeight = viewWorldHeight;
		setCamera(new OrthographicCamera());
	}
	
	@Override
	public void update(int screenWidth, int screenHeight, boolean centerCamera) {
		float viewportWidth = this.viewWorldWidth * ((float)screenWidth / (float)screenHeight);
		float viewportHeight = this.viewWorldHeight;
		
		Gdx.app.log(ProjectRPG.TITLE, "w : "+viewportWidth+" , h : "+viewportHeight);
		setWorldSize(viewportWidth, viewportHeight);
		setScreenBounds(0, 0, screenWidth, screenHeight);
	}
	
	public void setCenterToCharacter(Character character, float offsetX, float offsetY) {
		if(!character.equals(null)) {
				getCamera().position.set(character.getX() + offsetX, character.getY() + offsetY, 0.0f);
				getCamera().update();
		}
	}
	
}
