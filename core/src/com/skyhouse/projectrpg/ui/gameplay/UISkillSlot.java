package com.skyhouse.projectrpg.ui.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.graphics.RadialSprite;

public class UISkillSlot extends Actor {

	private Sprite outside, skill;
	private RadialSprite overlay;
	private BitmapFont font;
	private GlyphLayout layout;
	private float cooldowntime, currenttime;
	private boolean isCooldown;
	private float cooldownprocess;
	private boolean isSkillSet = false;
		
	public UISkillSlot(TextureRegion outside, TextureRegion overlay) {
		this.outside = new Sprite(outside);
		this.skill = new Sprite();
		this.outside.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.overlay = new RadialSprite(overlay);
		this.overlay.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.overlay.setScale(1, -1);
		this.overlay.setOrigin(this.overlay.getTexture().getWidth() / 2, this.overlay.getTexture().getHeight() / 2);
		font = ProjectRPG.Client.assetmanager.get("fonts/Roboto-Regular.ttf", BitmapFont.class);
		font.getData().markupEnabled = true;
		font.setColor(Color.BLACK);
		layout = new GlyphLayout();
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
	
	public void setSkill(TextureRegion region) {
		if(region == null) {
			isSkillSet = false;
			return;
		}
		skill.setRegion(region);
		isSkillSet = true;
	}
	
	@Override
	public void act(float delta) {
		if(loopcooldown) {
			if(currenttime > cooldowntime) currenttime = 0;
			currenttime += delta;
			cooldownprocess = currenttime / cooldowntime;
		}
		if(isCooldown) {
			if(currenttime > cooldowntime) resetCooldown();
			currenttime += delta;
			cooldownprocess = currenttime / cooldowntime;
		}
		setScale(0.6f);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		outside.draw(batch);
		if(isSkillSet) skill.draw(batch);
		if(isCooldown || loopcooldown) {
			overlay.draw(batch, getX(), getY(), 360f * cooldownprocess);
			layout.setText(font, String.format("%.0f", cooldowntime - currenttime));
			font.draw(batch, layout, outside.getOriginX() - (layout.width / 2f) + getX(), outside.getOriginY() + (layout.height / 2f) + getY());
		}
	}
	
	@Override
	public void setPosition(float x, float y) {
		outside.setPosition(x, y);
		skill.setPosition(x, y);
		super.setPosition(x, y);
	}
	
	@Override
	public void setPosition(float x, float y, int alignment) {
		outside.setPosition(x, y);
		skill.setPosition(x, y);
		super.setPosition(x, y, alignment);
	}
	
	@Override
	public void setScale(float scaleX, float scaleY) {
		outside.setScale(scaleX, scaleY);
		skill.setScale(scaleX, scaleY);
		overlay.setScale(scaleX + .02f, -scaleY - .02f);
		super.setScale(scaleX, scaleY);
	}
	
	@Override
	public void setScale(float scaleXY) {
		outside.setScale(scaleXY, scaleXY);
		skill.setScale(scaleXY, scaleXY);
		overlay.setScale(scaleXY + .02f, -scaleXY - .02f);
		super.setScale(scaleXY);
	}
	
	@Override
	public void setScaleX(float scaleX) {
		outside.setScale(scaleX, getScaleY());
		skill.setScale(scaleX, getScaleY());
		overlay.setScale(scaleX + .02f, -getScaleY() - .02f);
		super.setScaleX(scaleX);
	}
	
	@Override
	public void setScaleY(float scaleY) {
		outside.setScale(getScaleX(), scaleY);
		skill.setScale(getScaleX(), scaleY);
		overlay.setScale(getScaleX() + .02f, -scaleY - .02f);
		super.setScaleY(scaleY);
	}
	
}
