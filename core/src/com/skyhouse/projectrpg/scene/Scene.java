package com.skyhouse.projectrpg.scene;

import com.badlogic.gdx.utils.Disposable;

public interface Scene extends Disposable {

	public void resize(int width, int height);
	public void update(float deltatime);
	public void draw(float deltatime);
	
}
