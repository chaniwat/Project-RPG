package com.skyhouse.projectrpg.graphics.viewports;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skyhouse.projectrpg.entity.Character;

/**
 * Gameplay viewport.
 * @author Meranote
 */
public class GameplayViewport extends Viewport {
	
	private float viewWorldSize;
	private float viewWorldScale = 1f;
	private boolean isUpdated;

	/**
	 * Construct a new {@link GameplayViewport} by given view size.
	 */
	public GameplayViewport(float viewSize) {
		viewWorldSize = viewSize;
		setCamera(new OrthographicCamera());
	}
	
	@Override
	public void update(int screenWidth, int screenHeight, boolean centerCamera) {
		float viewportWidth = (viewWorldSize * ((float)screenWidth / (float)screenHeight)) * viewWorldScale;
		float viewportHeight = viewWorldSize * viewWorldScale;
		
		setWorldSize(viewportWidth, viewportHeight);
		setScreenBounds(0, 0, screenWidth, screenHeight);
		
		isUpdated = true;
	}
	
	/**
	 * Set the view to the center of character and calculate the corrent view with offset value.
	 */
	public void setViewCenterToCharacter(Character character, float offsetX, float offsetY) {
		if(!character.equals(null)) {
				getCamera().position.set(character.getData().x + offsetX, character.getData().y + offsetY, 0.0f);
				getCamera().update();
		}
	}
	
	/**
	 * Set the new view size.
	 */
	public void setViewSize(float viewSize) {
		viewWorldSize = viewSize;
		
		if(isUpdated) {
			update(getScreenWidth(), getScreenHeight());
		}
	}
	
	/**
	 * Set the view scale.
	 * <b>Note that not change the view size</b>.
	 * @param viewScale
	 */
	public void setViewScale(float viewScale) {
		viewWorldScale = viewScale;
		
		if(isUpdated) {
			update(getScreenWidth(), getScreenHeight());
		}
	}
	
}
