package com.skyhouse.projectrpg.scene;

import com.skyhouse.projectrpg.scene.input.SceneInput;

public class SceneAdapter implements Scene {

	SceneInput input;
	
	public SceneAdapter() {
		input = new SceneInput();
	}
	
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
	
	public void setSceneInputProcessor(SceneInput listener) {
		input = listener;
	}
	
	public void updateAndDraw(float deltatime) {
		update(deltatime);
		draw(deltatime);
	}


}
