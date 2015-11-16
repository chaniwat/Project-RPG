package com.skyhouse.projectrpg.scene.menu.tab;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skyhouse.projectrpg.scene.MenuScene;
import com.skyhouse.projectrpg.scene.MenuScene.TabSet;

/**
 * Tab in {@link MenuScene}.
 * @author Meranote
 */
public abstract class Tab {
	
	protected TabSet type;
	
	protected float width, height;
	protected float offsetX = 0, offsetY = 0;
	protected float x = 0, y = 0;
	
	public Tab(TabSet type) {
		this.type = type;
	}
	
	public void updateTabPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void updateTabSize(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	public void setTabOffset(float offsetX, float offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	public float getTabX() {
		return x;
	}
	
	public float getTabY() {
		return y;
	}
	
	public float getTabWidth() {
		return width;
	}
	
	public float getTabHeight() {
		return height;
	}
	
	public float getTabOffsetX() {
		return offsetX;
	}
	
	public float getTabOffsetY() {
		return offsetY;
	}
	
	public TabSet getTabSetType() {
		return type;
	}
	
	/** Update. */
	public abstract void update(float deltatime, float resolutionHeightScale);
	/** Draw. */
	public abstract void draw(SpriteBatch batch, float resolutionHeightScale);

}
