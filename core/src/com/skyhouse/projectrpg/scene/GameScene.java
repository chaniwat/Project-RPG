package com.skyhouse.projectrpg.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.data.MapData;
import com.skyhouse.projectrpg.entity.Character;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.graphics.viewports.UIViewport;
import com.skyhouse.projectrpg.input.GameplayControllerProcess;
import com.skyhouse.projectrpg.input.GameplayInputProcess;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;

public class GameScene extends Scene {
	
	private ShapeRenderer renderer;
	
	private World world;
	private Map map;
	private Sprite background;
	private Character maincharacter;
	
	Box2DDebugRenderer B2DDebug;
	
	public GameScene(SpriteBatch batch) {
		super(batch);
		B2DDebug = new Box2DDebugRenderer();
		addViewport("Gameplay", new GameplayViewport(15.5f));
		addViewport("UI", new UIViewport());
		world = new World(new Vector2(0, -10f), true);
		map = new Map(world, new MapData(Gdx.files.internal("mapdata/L01.map")));
		updateBackground(map.getBackgroundPath());
		renderer = new ShapeRenderer();
		CharacterData data = new CharacterData();
		data.x = 6f;
		data.y = 6f;
		maincharacter = new Character(world, new SpriterPlayer("entity/GreyGuy/player.scml", batch, renderer), data);
		input.setInputProcessor(new GameplayInputProcess(maincharacter));
		input.setControllerProcessor(new GameplayControllerProcess(maincharacter));
		input.use();
	}
	
	private void updateBackground(String pathToBG) {
		background = new Sprite(new Texture(Gdx.files.internal(pathToBG)));
		background.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		float baseHeight = 17f;
		background.setSize(baseHeight * (background.getWidth()/background.getHeight()), baseHeight);
	}

	@Override
	public void update(float deltatime) {
		world.step(1/60f, 8, 3);
		maincharacter.update(deltatime);
				
		((GameplayViewport)getViewport("Gameplay")).setViewCenterToCharacter(maincharacter, 0, 2f);
		background.setPosition(-(background.getWidth() / 2f) + (maincharacter.getData().x * 0.35f), -2f + (maincharacter.getData().y * 0.35f));		
	}
	
	@Override
	public void draw(float deltatime) {
		if(maincharacter == null) return;
		drawEntities();
		drawUI();
		
	}
	
	private void drawEntities() {
		useViewport("Gameplay");
		batch.begin();
			background.draw(batch);
			map.draw(batch);
			maincharacter.draw();
		batch.end();
		
		B2DDebug.render(world, getViewport("Gameplay").getCamera().combined);
	}
	
	private void drawUI() {
		useViewport("UI");
		
		batch.begin();
			font.draw(batch, "FPS : "+Gdx.graphics.getFramesPerSecond(), 20, getViewport("UI").getScreenHeight() - 20);
			//font.draw(batch, "Lantacy : "+ProjectRPG.Client.net.getReturnTripTime()+" ms", 20, getViewport("UI").getScreenHeight() - 40);
		batch.end();
	}
	
	@Override
	public void dispose() {
		world.dispose();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

}
