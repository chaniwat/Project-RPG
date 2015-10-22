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

/**
 * Skill slot for use in GameScene UI.
 * @author Meranote
 */
public class UISkillSlot extends Actor {

	private Sprite outside, skill;
	private RadialSprite overlay;
	private BitmapFont font;
	private GlyphLayout layout;
	private float cooldowntime, currenttime, cooldownprocess;
	private boolean isCooldown = false;
	private boolean isSkillSet = false;
	
	public UISkillSlot(TextureRegion outside, TextureRegion overlay, BitmapFont font) {
		this.font = font;
		this.layout = new GlyphLayout();
		this.skill = new Sprite();
		this.outside = new Sprite(outside);
		this.outside.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.overlay = new RadialSprite(overlay);
		this.overlay.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.overlay.setScale(1, -1);
		this.overlay.setOrigin(this.overlay.getTexture().getWidth() / 2, this.overlay.getTexture().getHeight() / 2);
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
	
	public void setSkill(TextureRegion region) {
		if(region == null) {
			isSkillSet = false;
			return;
		}
		skill.set(new Sprite(region));
		skill.setPosition(getX(), getY());
		skill.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		isSkillSet = true;
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
		outside.draw(batch);
		if(isSkillSet) skill.draw(batch);
		if(isCooldown) {
			overlay.draw(batch, getX(), getY(), 360f * cooldownprocess);
			//layout.setText(font, String.format("[WHITE]%.0f", cooldowntime - currenttime));
			//font.draw(batch, layout, outside.getOriginX() - (layout.width / 2f) + getX(), outside.getOriginY() + (layout.height / 2f) + getY());
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
		skill.setScale(scaleX + .02f, scaleY + .02f);
		overlay.setScale(scaleX + .02f, -scaleY - .02f);
		super.setScale(scaleX, scaleY);
	}
	
	@Override
	public void setScale(float scaleXY) {
		outside.setScale(scaleXY, scaleXY);
		skill.setScale(scaleXY + .02f, scaleXY + .02f);
		overlay.setScale(scaleXY + .02f, -scaleXY - .02f);
		super.setScale(scaleXY);
	}
	
	@Override
	public void setScaleX(float scaleX) {
		outside.setScale(scaleX, getScaleY());
		skill.setScale(scaleX + .02f, getScaleY());
		overlay.setScale(scaleX + .02f, -getScaleY());
		super.setScaleX(scaleX);
	}
	
	@Override
	public void setScaleY(float scaleY) {
		outside.setScale(getScaleX(), scaleY);
		skill.setScale(getScaleX(), scaleY + .02f);
		overlay.setScale(getScaleX(), -scaleY - .02f);
		super.setScaleY(scaleY);
	}
	
}
