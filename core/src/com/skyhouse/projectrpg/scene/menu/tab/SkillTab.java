package com.skyhouse.projectrpg.scene.menu.tab;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.scene.MenuScene.TabSet;

public class SkillTab extends Tab {

	private AssetManager assetmanager = ProjectRPG.client.assetmanager;
	private BitmapFont font = ProjectRPG.client.graphic.font.spritefont;
	private BitmapFont fontbold = ProjectRPG.client.graphic.font.spritefontbold;
	private GlyphLayout layout = ProjectRPG.client.graphic.font.layout;
	
	public SkillTab() {
		super(TabSet.MENU);
	}
	
	@Override
	public void update(float deltatime, float resolutionHeightScale) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(SpriteBatch batch, float resolutionHeightScale) {
		batch.setShader(ProjectRPG.client.graphic.font.shader);
		
			fontbold.getData().setScale(1f * resolutionHeightScale);
			layout.setText(fontbold, "[ORANGE]SKILL");
			fontbold.draw(batch, layout, (width / 2f) - (layout.width / 2f) + x, (height / 2f) - (layout.height / 2f) + y);
		
		batch.setShader(null);
	}

}
