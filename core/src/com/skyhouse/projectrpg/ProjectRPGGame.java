package com.skyhouse.projectrpg;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector2;
import com.skyhouse.projectrpg.graphics.GameplayViewport;
import com.skyhouse.projectrpg.graphics.TileTexture;
import com.skyhouse.projectrpg.graphics.TileTexture.TileTexturePosition;
import com.skyhouse.projectrpg.input.GameplayInputProcess;
import com.skyhouse.projectrpg.objects.BackgroundGlobal;
import com.skyhouse.projectrpg.objects.Character;
import com.skyhouse.projectrpg.objects.Structure;
import com.skyhouse.projectrpg.physics.PhysicGlobal;
import com.skyhouse.projectrpg.utils.spriter.SpriterGlobal;

public class ProjectRPGGame extends ApplicationAdapter {
	
	public static class ProjectRPG {
		public static final String TITLE = "Project RPG";
		public static final String VERSION = "0.0(WIP)"; 
	}
	
	GameplayInputProcess gameplayinput;
	SpriteBatch environmentBatch;
	SpriteBatch UIbatch;
	GameplayViewport gameViewport;
	OrthographicCamera UICam;
	Character playercharacter;
	BitmapFont font;
	
	Structure ground;
	List<Structure> box;
	
	@Override
	public void create () {
		
		PhysicGlobal.init(0f, -10f,  true);
		SpriterGlobal.init();
		BackgroundGlobal.init();
		
		environmentBatch = new SpriteBatch();
		UIbatch = new SpriteBatch();
		
		gameViewport = new GameplayViewport(12f, 12f);
		UICam = new OrthographicCamera();
		
		BackgroundGlobal.setBackground(new Texture(Gdx.files.internal("background.png")));
		BackgroundGlobal.setSizeByHeight(17);
		
		playercharacter = new Character("entities/GreyGuy/player.scml", new Vector2(-7, 3) ,2.7f);
		
		TextureAtlas texture = new TextureAtlas(Gdx.files.internal("structures/greenland/tiles_spritesheet.pack"));
		
		// For Box2D test
		
		ground = new Structure(new Vector2(-20f, 0), new Vector2(60f, 5f), new TileTexture(texture.findRegion("grassMid")));
		ground.getTileTexture().setRegionPosition(texture.findRegion("grassCenter"), TileTexturePosition.MiddleHorizontal, TileTexturePosition.Bottom);
		
		box = new ArrayList<Structure>();
		box.add(new Structure(new Vector2(1, 1), new Vector2(1, 1), new TileTexture(texture.findRegion("boxAlt"))));
		box.add(new Structure(new Vector2(2, 2), new Vector2(1, 2), new TileTexture(texture.findRegion("boxAlt"))));
		box.add(new Structure(new Vector2(3, 3), new Vector2(3, 3), new TileTexture(texture.findRegion("boxAlt"))));
		box.add(new Structure(new Vector2(6, 2), new Vector2(1, 2), new TileTexture(texture.findRegion("boxAlt"))));
		box.add(new Structure(new Vector2(7, 1), new Vector2(1, 1), new TileTexture(texture.findRegion("boxAlt"))));
		
		font = new BitmapFont();
		font.setColor(new Color(0, 0, 0, 1));
		
		gameplayinput = new GameplayInputProcess(playercharacter);
		
		Gdx.input.setInputProcessor(gameplayinput);
		
		Gdx.app.log(ProjectRPG.TITLE, "Version = " + ProjectRPG.VERSION);
		Gdx.app.log(ProjectRPG.TITLE, "created");
		
		GLProfiler.enable();
	}
	
	@Override
	public void resize (int width, int height) {
		gameViewport.update(width, height);
		UICam.viewportWidth = width;
		UICam.viewportHeight = height;
		UICam.position.set(UICam.viewportWidth / 2f, UICam.viewportHeight / 2f, 0.0f);
		UICam.update();
		
		Gdx.app.log(ProjectRPG.TITLE, "screen width: "+width);
		Gdx.app.log(ProjectRPG.TITLE, "screen height: "+height);
		Gdx.app.log(ProjectRPG.TITLE, "width viewport: "+gameViewport.getWorldWidth());
		Gdx.app.log(ProjectRPG.TITLE, "Height viewport: "+gameViewport.getWorldHeight());
		
		UIbatch.setProjectionMatrix(UICam.combined);
	}

	@Override
	public void render () {
		// Input
		
		// Clear buffer
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		// Update & Process
		PhysicGlobal.getWorld().step(1/60f, 8, 3);
		gameViewport.setCenterToCharacter(playercharacter, 0, 2.4f);
		playercharacter.update();
		
		SpriterGlobal.setProjectionMatrix(gameViewport.getCamera().combined);
		environmentBatch.setProjectionMatrix(gameViewport.getCamera().combined);
		
		BackgroundGlobal.setPosition(-(BackgroundGlobal.getWidth() / 2f) + (playercharacter.getX() * 0.35f), -2f + (playercharacter.getY() * 0.35f));
		
		// Render -  Gameplay
		gameViewport.apply();
		
			// Background - Layer 0
			BackgroundGlobal.draw(environmentBatch);
			
			// Structure (eg. Ladder, background object) - Layer 1
			environmentBatch.begin();
				
			environmentBatch.end();
			
			// Entities (eg. Character, Monster) - Layer 2
			SpriterGlobal.updateAndDraw();
			
			// Structure (eg. floor, platforms) - Layer 3
			environmentBatch.begin();
				ground.render(environmentBatch);
				for(Structure box : box) {
					box.render(environmentBatch);
				}
			environmentBatch.end();
			
			//PhysicGlobal.debugRender(mainCam); - Layer 3.1
			PhysicGlobal.debugRender(gameViewport.getCamera());
	
		// Render - UI - Layer 4
		UIbatch.begin();
			font.draw(UIbatch, "FPS : "+Gdx.graphics.getFramesPerSecond(), 20, UICam.viewportHeight - 20);
			font.draw(UIbatch, String.format("Frametime : %.0f ms", Gdx.graphics.getDeltaTime()*1000), 20, UICam.viewportHeight - 40);
		UIbatch.end();
		
		// Log
		//
		
	}
	
	@Override
	public void dispose () {
		SpriterGlobal.dispose();
		PhysicGlobal.dispose();
	}
}
