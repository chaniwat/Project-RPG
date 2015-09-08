package com.skyhouse.projectrpg.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.skyhouse.projectrpg.ProjectRPGGame.ProjectRPG;

public class BackgroundGlobal {
	
	static boolean isBackgroundSet;
	static boolean isInitSprite;
	
	static Sprite sprite;
	
	private BackgroundGlobal() {}
	
	public static void init() {
		if(!isInitSprite) sprite = new Sprite();
		else Gdx.app.log(ProjectRPG.TITLE, "Already initial background");
		isInitSprite = true;
	}
	
	public static void setBackground(Texture texture) {
		if(!isInitSprite) return;
		sprite.setBounds(0, 0, -texture.getWidth(), -texture.getHeight());
		sprite.setRegion(texture);
		sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		isBackgroundSet = true;
	}
	
	public static void removeBackground() {
		if(isInitSprite && isBackgroundSet) {
			sprite.setTexture(null);
			isBackgroundSet = false;
		}
	}
	
	public static void setSize(Vector2 size) {
		if(!isInitSprite || !isBackgroundSet) return;
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
		if(!isInitSprite || !isBackgroundSet) return;
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
	
	public static void draw(SpriteBatch batch) {
		if(!isInitSprite || !isBackgroundSet) return;
		batch.begin();
			sprite.draw(batch);
		batch.end();
	}

}
