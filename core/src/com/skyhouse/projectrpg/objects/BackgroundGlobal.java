package com.skyhouse.projectrpg.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.skyhouse.projectrpg.ProjectRPG;

public class BackgroundGlobal {
	
	static boolean isBackgroundSet;
	static boolean isInit;
	
	private static Sprite sprite;
	private static SpriteBatch batch;
	
	private BackgroundGlobal() {}
	
	public static void init(SpriteBatch batch) {
		if(!isInit) {
			sprite = new Sprite();
			BackgroundGlobal.batch  = batch;
		}
		else Gdx.app.log(ProjectRPG.TITLE, "Already initial background");
		isInit = true;
	}
	
	public static void setBackground(Texture texture) {
		if(!isInit) return;
		sprite.setBounds(0, 0, -texture.getWidth(), -texture.getHeight());
		sprite.setRegion(texture);
		sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		isBackgroundSet = true;
	}
	
	public static void removeBackground() {
		if(isInit && isBackgroundSet) {
			sprite.setTexture(null);
			isBackgroundSet = false;
		}
	}
	
	public static void setSize(Vector2 size) {
		if(!isInit || !isBackgroundSet) return;
		sprite.setSize(size.x, size.y);
	}
	
	public static void setSize(float width, float height) {
		setSize(new Vector2(width, height));
	}
	
	public static void setSizeByWidth(float width) {
		setSize(width, width * (sprite.getHeight() / sprite.getWidth()));
	}
	
	public static void setSizeByHeight(float height) {
		setSize(height * (sprite.getWidth() / sprite.getHeight()), height);
	}

	public static void setPosition(Vector2 position) {
		if(!isInit || !isBackgroundSet) return;
		sprite.setPosition(position.x, position.y);
	}
	
	public static void setPosition(float width, float height) {
		setPosition(new Vector2(width, height));
	}
	
	public static float getWidth() {
		return sprite.getWidth();
	}
	
	public static float getHeight() {
		return sprite.getHeight();
	}
	
	public static void draw() {
		if(!isInit || !isBackgroundSet) return;
		batch.begin();
			sprite.draw(batch);
		batch.end();
	}

}
