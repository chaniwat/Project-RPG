package com.skyhouse.projectrpg.graphics.viewports;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * UI viewport. or the screen viewport. <br>
 * This viewport have the view same size to the screen. 
 * @author Meranote
 */
public class ScreenViewport extends Viewport {
	
	/**
	 * Construct a new {@link ScreenViewport}.
	 */
	public ScreenViewport() {
		setCamera(new OrthographicCamera());
	}
	
	@Override
	public void update(int screenWidth, int screenHeight, boolean centerCamera) {		
		setWorldSize(screenWidth, screenHeight);
		setScreenBounds(0,0, screenWidth, screenHeight);
		
		apply(true);
	}

}
