package com.skyhouse.projectrpg.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.ui.gameplay.UISkillSlot;

public class GameSceneUI extends Stage {
	
	private UISkillSlot s1, s2;
	
	public GameSceneUI(Viewport viewport, SpriteBatch batch) {
		super(viewport, batch);
		loadAssets();
		
		s1 = new UISkillSlot(new TextureRegion(ProjectRPG.Client.assetmanager.get("textures/gamesceneui/SkillCircle_ext.png", Texture.class)), new TextureRegion(ProjectRPG.Client.assetmanager.get("textures/gamesceneui/SkillCircle_ovr.png", Texture.class)));
		s2 = new UISkillSlot(new TextureRegion(ProjectRPG.Client.assetmanager.get("textures/gamesceneui/SkillCircle_ext.png", Texture.class)), new TextureRegion(ProjectRPG.Client.assetmanager.get("textures/gamesceneui/SkillCircle_ovr.png", Texture.class)));
		addActor(s1);
		addActor(s2);
		
		s1.setPosition(300f, 0f);
	}
	
	private void loadAssets() {
		AssetManager assetmanager = ProjectRPG.Client.assetmanager;
		assetmanager.load("textures/gamesceneui/hpbar_ext.png", Texture.class);
		assetmanager.load("textures/gamesceneui/hpbar_int.png", Texture.class);
		assetmanager.load("textures/gamesceneui/NormalAtk_ext.png", Texture.class);
		assetmanager.load("textures/gamesceneui/QuickHeal_ext.png", Texture.class);
		assetmanager.load("textures/gamesceneui/QuickHeal_ovr.png", Texture.class);
		assetmanager.load("textures/gamesceneui/SkillCircle_ext.png", Texture.class);
		assetmanager.load("textures/gamesceneui/SkillCircle_ovr.png", Texture.class);
		assetmanager.finishLoading();
	}

}
