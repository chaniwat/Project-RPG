package com.skyhouse.projectrpg.ui.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.skyhouse.projectrpg.graphics.RadialSprite;

public class UISkillSlot extends Actor {

	private Sprite outside, skill;
	private RadialSprite overlay;
	private float cooldowntime, currenttime;
	private boolean isCooldown;
	private float cooldownprocess;
	
	public UISkillSlot(TextureRegion outside, TextureRegion overlay) {
		this.outside = new Sprite(outside);
		this.overlay = new RadialSprite(overlay);
		this.overlay.setScale(1, -1);
		this.overlay.setOrigin(0, this.overlay.getTexture().getHeight() / 2);
		loopCooldown(12f);
	}
	
	public void startCooldown(float time) {
		cooldowntime = time;
		currenttime = 0f;
		isCooldown = true;
	}
	
	private boolean loopcooldown;
	public void loopCooldown(float time) {
		cooldowntime = 12f;
		currenttime = 0f;
		loopcooldown = true;
	}
	
	public void resetCooldown() {
		isCooldown = false;
	}
	
	@Override
	public void act(float delta) {
		if(loopcooldown) {
			currenttime += delta;
			cooldownprocess = currenttime / cooldowntime;
			if(currenttime > cooldowntime) currenttime = 0;
		}
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
		batch.draw(outside, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		if(isCooldown || loopcooldown) overlay.draw(batch, getX(), getY(), 360f * cooldownprocess);
	}
	
}
