package com.skyhouse.projectrpg.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skyhouse.projectrpg.ProjectRPG;

/**
 * Item slot for use in inventory or equip in character status.
 * @author Meranote
 */
public class ItemSlot {

	private float x, y, scale = 1f;
	private Sprite bg, icon;

	/** 
	 * Construct a new {@link ItemSlot}.
	 */
	public ItemSlot() {
		bg = new Sprite(ProjectRPG.client.assetmanager.get("texture/ui/menuscene/itemslot/itemslot.png", Texture.class));
		icon = new Sprite(ProjectRPG.client.assetmanager.get("texture/ui/menuscene/itemslot/mockitemicon.png", Texture.class));
	}
	
	/**
	 * Set icon to show in this slot, <b>null</b> for empty slot.
	 */
	public void setIcon(Texture texture) {
		if(texture == null) icon.setTexture(ProjectRPG.client.assetmanager.get("texture/ui/menuscene/itemslot/mockitemicon.png", Texture.class));
		else icon.setTexture(texture);
	}

	/**
	 * Update radio size.
	 */
	public void updateSize(float resolutionHeightScale) {
		bg.setSize(bg.getTexture().getWidth() * resolutionHeightScale * scale, bg.getTexture().getHeight() * resolutionHeightScale * scale);
		icon.setSize(icon.getTexture().getWidth() * resolutionHeightScale * scale, icon.getTexture().getHeight() * resolutionHeightScale * scale);
		
		bg.setPosition(0 + x, 0 + y);
		icon.setPosition(5f * resolutionHeightScale * scale + x, 5f * resolutionHeightScale * scale + y);
	}
	
	/**
	 * Draw.
	 */
	public void draw(SpriteBatch batch) {
		bg.draw(batch);
		icon.draw(batch);
	}

	/**
	 * Get position x.
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * Get position y.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Get current width.
	 */
	public float getWidth() {
		return bg.getWidth();
	}
	
	/**
	 * Get current height.
	 */
	public float getHeight() {
		return bg.getHeight();
	}
	
	/**
	 * Get current scale.
	 */
	public float getScale() {
		return scale;
	}
	
	/**
	 * Set position.
	 */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Set scale.
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

}
