package com.skyhouse.projectrpg;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector2;
import com.skyhouse.projectrpg.graphics.GameplayViewport;
import com.skyhouse.projectrpg.graphics.TileTexture;
import com.skyhouse.projectrpg.graphics.TileTexture.TileTexturePosition;
import com.skyhouse.projectrpg.graphics.UIViewport;
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
	SpriteBatch batch;
	GameplayViewport gameViewport;
	UIViewport uiViewport;
	ArrayList<Character> characters;
	Character playercharacter;
	BitmapFont font;
	
	Structure ground;
	List<Structure> box;
	
	String lastCharAnim = "";
	
	@Override
	public void create () {
		
		batch = new SpriteBatch();
		
		PhysicGlobal.init(0f, -10f,  true);
		SpriterGlobal.init(batch);
		BackgroundGlobal.init(batch);
		
		gameViewport = new GameplayViewport(12f);
		uiViewport = new UIViewport();
		
		BackgroundGlobal.setBackground(new Texture(Gdx.files.internal("background.png")));
		BackgroundGlobal.setSizeByHeight(17);
		
		characters = new ArrayList<Character>();
		playercharacter = new Character("entities/GreyGuy/player.scml", new Vector2(-7, 3) ,2.7f);
		characters.add(playercharacter);
		characters.add(new Character("entities/GreyGuy/player.scml", new Vector2(-10, 2) ,2.7f));
		
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
		uiViewport.update(width, height);
		
		Gdx.app.log(ProjectRPG.TITLE, "screen width: "+width);
		Gdx.app.log(ProjectRPG.TITLE, "screen height: "+height);
		Gdx.app.log(ProjectRPG.TITLE, "width viewport: "+gameViewport.getWorldWidth());
		Gdx.app.log(ProjectRPG.TITLE, "Height viewport: "+gameViewport.getWorldHeight());
	}

	@Override
	public void render () {
		// Input
		
		// Clear buffer
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		// Update & Process
		PhysicGlobal.getWorld().step(1/60f, 8, 3);
		gameViewport.setViewCenterToCharacter(playercharacter, 0, 2.4f);
		for(Character character : characters) {
			character.update(Gdx.graphics.getDeltaTime());
		}
		
		BackgroundGlobal.setPosition(-(BackgroundGlobal.getWidth() / 2f) + (playercharacter.getX() * 0.35f), -2f + (playercharacter.getY() * 0.35f));
		
		// Render -  Gameplay
		gameViewport.apply();
		batch.setProjectionMatrix(gameViewport.getCamera().combined);
		
		// Background - Layer 0
		BackgroundGlobal.draw();
		
		// Structure (eg. Ladder, background object) - Layer 1
		batch.begin();
			
		batch.end();
			
		// Entities (eg. Character, Monster) - Layer 2
		SpriterGlobal.updateAndDraw();
		
		// Structure (eg. floor, platforms) - Layer 3
		batch.begin();
			ground.render(batch);
			for(Structure box : box) {
				box.render(batch);
			}
		batch.end();
		
		//PhysicGlobal.debugRender(mainCam); - Layer 4
		PhysicGlobal.debugRender(gameViewport.getCamera());
	
		// Render - UI
		uiViewport.apply();
		batch.setProjectionMatrix(uiViewport.getCamera().combined);
		
		// Text - Layer 1
		batch.begin();
			font.draw(batch, "FPS : "+Gdx.graphics.getFramesPerSecond(), 20, uiViewport.getWorldHeight() - 20);
			font.draw(batch, String.format("Frametime : %.0f ms", Gdx.graphics.getDeltaTime()*1000), 20, uiViewport.getWorldHeight() - 40);
			font.draw(batch, String.format("Draw calls : %d | Bound calls : %d | Shader switch : %d", GLProfiler.drawCalls, GLProfiler.textureBindings, GLProfiler.shaderSwitches), 20, uiViewport.getWorldHeight() - 60);
			font.draw(batch, String.format("Player position: (%.2f, %.2f)", playercharacter.getX(), playercharacter.getY()), 20, uiViewport.getWorldHeight() - 80);
		batch.end();
		
		// Log
		// Animation changed
		/*
		if(!lastCharAnim.equals(playercharacter.getCurrentAnimation())) {
			lastCharAnim = playercharacter.getCurrentAnimation();
			Gdx.app.log(ProjectRPG.TITLE, String.format("Current Animation : %s", lastCharAnim));			
		} 
		*/
		
		GLProfiler.reset();
	}
	
	@Override
	public void dispose () {
		SpriterGlobal.dispose();
		PhysicGlobal.dispose();
	}
	
	public static void removeCharacter() {
		
	}
}
