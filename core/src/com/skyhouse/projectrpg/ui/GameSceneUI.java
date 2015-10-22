package com.skyhouse.projectrpg.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import com.kotcrab.vis.ui.widget.VisTable;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.entities.Character;
import com.skyhouse.projectrpg.ui.gameplay.QuickHealSlot;
import com.skyhouse.projectrpg.ui.gameplay.UISkillSlot;

public class GameSceneUI extends Stage {
	
	public static enum SlotPosition {
		FIRST, SECOND
	}
	
	private UISkillSlot s1, s2;
	private QuickHealSlot qh;
	private AssetManager assetmanager;
	private BitmapFont font;
	
	public GameSceneUI(Viewport viewport, SpriteBatch batch, BitmapFont font) {
		super(viewport, batch);
		assetmanager = ProjectRPG.Client.assetmanager;
		this.font = font;
		loadRequireAssets();
		
		s1 = new UISkillSlot(
				new TextureRegion(assetmanager.get("textures/gamesceneui/SkillCircle_ext.png", Texture.class)), 
				new TextureRegion(assetmanager.get("textures/gamesceneui/SkillCircle_ovr.png", Texture.class)));
		s2 = new UISkillSlot(
				new TextureRegion(assetmanager.get("textures/gamesceneui/SkillCircle_ext.png", Texture.class)), 
				new TextureRegion(assetmanager.get("textures/gamesceneui/SkillCircle_ovr.png", Texture.class)));
		
		addActor(s1);
		addActor(s2);
		
		assetmanager.load("textures/skill/test/t1.png", Texture.class);
		assetmanager.load("textures/skill/test/t2.png", Texture.class);
		assetmanager.finishLoading();
		s1.setSkill(new TextureRegion(assetmanager.get("textures/skill/test/t1.png", Texture.class)));
		s2.setSkill(new TextureRegion(assetmanager.get("textures/skill/test/t2.png", Texture.class)));
		
		s1.setScale(0.5f);
		s2.setScale(0.5f);
		
		qh = new QuickHealSlot(
				new TextureRegion(assetmanager.get("textures/gamesceneui/QuickHeal_ext.png", Texture.class)), 
				new TextureRegion(assetmanager.get("textures/gamesceneui/QuickHeal_ovr.png", Texture.class)));
		
		addActor(qh);
		
		assetmanager.load("textures/item/potion/heal/hp.png", Texture.class);
		assetmanager.finishLoading();
		qh.setHeal(new TextureRegion(assetmanager.get("textures/item/potion/heal/hp.png", Texture.class)));
		
		qh.setScale(0.5f);
	}
	
	private void loadRequireAssets() {
		assetmanager.load("textures/gamesceneui/hpbar_ext.png", Texture.class);
		assetmanager.load("textures/gamesceneui/hpbar_int.png", Texture.class);
		assetmanager.load("textures/gamesceneui/NormalAtk_ext.png", Texture.class);
		assetmanager.load("textures/gamesceneui/QuickHeal_ext.png", Texture.class);
		assetmanager.load("textures/gamesceneui/QuickHeal_ovr.png", Texture.class);
		assetmanager.load("textures/gamesceneui/SkillCircle_ext.png", Texture.class);
		assetmanager.load("textures/gamesceneui/SkillCircle_ovr.png", Texture.class);
		assetmanager.finishLoading();
	}
	
	public void useSkill(SlotPosition position, float cdtime) {
		switch(position) {
			case FIRST:
				s1.startCooldown(cdtime);
				break;
			case SECOND:
				s2.startCooldown(cdtime);
				break;
		}
	}
	
	public void useQuickHeal(float cdtime) {
		qh.startCooldown(cdtime);
	}
	
	public void update() {
		qh.setPosition(getViewport().getWorldWidth() / 2f - 160f, 32f);
		s2.setPosition(qh.getX() + (114f * s2.getScaleX()), 0);
		s1.setPosition(s2.getX() + (150f * s1.getScaleX()), 0);
	}
	
}
