package com.skyhouse.projectrpg.ui.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class QuickHealSlot extends Actor {
	
	private TextureRegion outside, heal, overlay;
	private float cooldowntime, currenttime, cooldownprocess;
	private boolean isCooldown = false;
	private boolean isHealSet = false;
	
	public QuickHealSlot(TextureRegion outside, TextureRegion overlay) {
		this.outside = outside;
		this.outside.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.overlay = overlay;
		this.overlay.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		setScale(1f);
	}
	
	public void startCooldown(float time) {
		cooldowntime = time;
		currenttime = 0f;
		isCooldown = true;
	}
	
	public void resetCooldown() {
		isCooldown = false;
	}
	
	public void setHeal(TextureRegion heal) {
		if(heal == null) {
			isHealSet = false;
			return;
		}
		this.heal = heal;
		isHealSet = true;
	}
	
	@Override
	public void act(float delta) {
		if(isCooldown) {
			currenttime += delta;
			cooldownprocess = currenttime / cooldowntime;
			if(currenttime > cooldowntime) resetCooldown();
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(outside, getX(), getY(), getOriginX(), getOriginX(), outside.getTexture().getWidth(), outside.getTexture().getHeight(), getScaleX(), getScaleY(), getRotation());
		if(isHealSet) batch.draw(heal, getX(), getY(), getOriginX(), getOriginY(), heal.getTexture().getWidth(), heal.getTexture().getHeight(), getScaleX() + .02f, getScaleY() + .02f, getRotation());
		if(isCooldown) {
			overlay.setRegion(0, (int)(overlay.getTexture().getHeight() * cooldownprocess), overlay.getTexture().getWidth(), (int)(overlay.getTexture().getHeight() * (1f - cooldownprocess)));
			batch.draw(overlay, getX(), getY(), getOriginX(), getOriginX(), overlay.getTexture().getWidth(), overlay.getRegionHeight(), getScaleX() + .02f, getScaleY() + .02f, getRotation());			
		}
	}
	
}
