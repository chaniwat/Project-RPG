package com.skyhouse.projectrpg.scene;

public class SceneAdapter implements Scene {

	@Override
	public void dispose() {
		
	}

	@Override
	public void resize(int width, int height) {
		
	}
	
	@Override
	public void update(float deltatime) {
		
	}

	@Override
	public void draw(float deltatime) {
		
	}
	
	public void updateAndDraw(float deltatime) {
		update(deltatime);
		draw(deltatime);
	}


}
