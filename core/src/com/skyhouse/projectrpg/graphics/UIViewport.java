package com.skyhouse.projectrpg.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

public class UIViewport extends Viewport {
	
	public UIViewport() {
		setCamera(new OrthographicCamera());
	}
	
	@Override
	public void update(int screenWidth, int screenHeight, boolean centerCamera) {		
		setWorldSize(screenWidth, screenHeight);
		setScreenBounds(0,0, screenWidth, screenHeight);
		
		apply(true);
	}

}
