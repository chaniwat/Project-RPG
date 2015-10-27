package com.skyhouse.projectrpg.manager;

import com.badlogic.gdx.utils.Disposable;

public abstract class Manager implements Disposable{
	
	public abstract void update(float deltaTime);
	public abstract void dispose();
	
}
