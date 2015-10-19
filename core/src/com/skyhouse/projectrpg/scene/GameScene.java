package com.skyhouse.projectrpg.scene;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.skyhouse.projectrpg.ProjectRPGClient;
import com.skyhouse.projectrpg.entities.Character;
import com.skyhouse.projectrpg.entities.data.CharacterData;
import com.skyhouse.projectrpg.entities.data.CharacterData.CharacterActionState;
import com.skyhouse.projectrpg.graphics.SpriterGlobal;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.graphics.viewports.UIViewport;
import com.skyhouse.projectrpg.input.GameplayControllerProcess;
import com.skyhouse.projectrpg.input.GameplayInputProcess;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.map.MapLoader;
import com.skyhouse.projectrpg.physics.CharacterBody;
import com.skyhouse.projectrpg.physics.PhysicGlobal;

public class GameScene extends SceneAdapter {
	
	AssetManager assetmanager;
	
	SpriteBatch batch;
	ShapeRenderer renderer;
	BitmapFont font;
	
	GameplayViewport gameViewport;
	UIViewport uiViewport;
	GlyphLayout textlayout;
	
	World world;
	HashMap<String, Map> maps;
	Map mainmap;
	Sprite background;

	HashMap<Integer, Character> characters;
	Character maincharacter;
	
	public GameScene(SpriteBatch batch, AssetManager assetmanager) {
		this.batch = batch;
		this.renderer = new ShapeRenderer();
		this.assetmanager = assetmanager;
		
		gameViewport = new GameplayViewport(15f);
		uiViewport = new UIViewport();
		textlayout = new GlyphLayout();
		
		maps = new HashMap<String, Map>();
		characters = new HashMap<Integer, Character>();
		
		font = assetmanager.get("fonts/Roboto-Regular.ttf", BitmapFont.class);
		font.getData().markupEnabled = true;
		font.setColor(Color.BLACK);
		
		world = new World(new Vector2(0, -10f), true);
	}
	
	public void loadMap(String map, boolean waitforload) {
		String pathtomap = "mapdata/" + map + ".map";
		assetmanager.load(pathtomap, Map.class, new MapLoader.MapLoaderParameter(assetmanager));
		if(waitforload) assetmanager.finishLoading();
		
		maps.put(assetmanager.get(pathtomap, Map.class).getName(), assetmanager.get(pathtomap, Map.class));
		
		setBackgroundFromMap(maps.get("L01"));
	}
	
	private void setBackgroundFromMap(Map map) {
		Map.BackgroundData bgdata = map.getBackgroundData();
		background = new Sprite(assetmanager.get(bgdata.path, Texture.class));
		background.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background.setSize(bgdata.width, bgdata.height);
	}
	
	@Override
	public void resize(int width, int height) {
		gameViewport.update(width, height);
		uiViewport.update(width, height);
	}

	@Override
	public void update(float deltatime) {
		world.step(1/60f, 8, 3);
		
		if(maincharacter == null) return;
		CharacterData maindata = new CharacterData(maincharacter);
		ProjectRPGClient.client.sendUDP(maindata);
		
		for(Character character : characters.values()) {
			character.update(Gdx.graphics.getDeltaTime());
		}
		
		gameViewport.setViewCenterToCharacter(maincharacter, 0, 2.45f);
		gameViewport.setViewSize(16f);
		background.setPosition(-(background.getWidth() / 2f) + (maincharacter.getPositionX() * 0.35f), -2f + (maincharacter.getPositionY() * 0.35f));
	}

	@Override
	public void draw(float deltatime) {
		if(maincharacter == null) return;
		drawEntities();
		drawUI();
	}
	
	private void drawEntities() {
		gameViewport.apply();
		batch.setProjectionMatrix(gameViewport.getCamera().combined);
		
		batch.begin();
			background.draw(batch);
			for(Map map : maps.values()) {
				map.draw(batch);				
			}
			SpriterGlobal.updateAndDraw(maincharacter.getSpriterPlayer());
		batch.end();
	}
	
	private void drawUI() {
		uiViewport.apply();
		batch.setProjectionMatrix(uiViewport.getCamera().combined);
		renderer.setProjectionMatrix(uiViewport.getCamera().combined);
		
		Vector2 hpposition = new Vector2((uiViewport.getScreenWidth() / 2f), 128f);
		Vector2 hpsize = new Vector2(352f, 15f);
		float hpbordersize = 3f;
		float hpremain = 244520f;
		float hpmax = 365210f;
		float hpprocess = hpremain / hpmax;
		if(hpprocess > 1f) hpprocess = 1f;
		
		Vector2 expposition = new Vector2(hpposition.x, hpposition.y - 10f);
		Vector2 expsize = new Vector2(hpsize.x + hpbordersize * 2f, 5f);
		float expcurrent = 0.14f;
		float expextra = 0.34f;
		if(expcurrent + expextra > 1f) expextra = 1f - expcurrent;
		
		float skradius = 29f;
		float skborder = 4f;
		Vector2 qhposition = new Vector2(hpposition.x - hpbordersize - (hpsize.x / 2f) + skradius + skborder, expposition.y - skradius - 10f - skborder);
		Vector2 s1position = new Vector2(qhposition.x + (skradius * 2f) + 20f, qhposition.y);
		Vector2 s2position = new Vector2(s1position.x + (skradius * 2f) + 10f + skborder, qhposition.y);
		
		float ninsidesize = 3f;
		Vector2 nposition = new Vector2(s2position.x + skradius + 20f, qhposition.y - skradius - skborder);
		Vector2 n1position = new Vector2(nposition.x + skborder, nposition.y + skborder);
		Vector2 n2position = new Vector2(n1position.x + (skradius * 2f) + 1.2f, n1position.y);
		
		float modsbordersize = 3f;
		Vector2 modssize = new Vector2(skradius * 2f, 18f);
		Vector2 mods1position = new Vector2(s1position.x - skradius - modsbordersize, s1position.y - skradius * 2f - skborder - modsbordersize);
		Vector2 mods2position = new Vector2(s2position.x - skradius - modsbordersize, s2position.y - skradius * 2f - skborder - modsbordersize);
		
		renderer.begin(ShapeType.Filled);
			renderer.setColor(Color.BLACK);
			renderer.rect(hpposition.x - (hpsize.x / 2f) - hpbordersize, hpposition.y, hpsize.x + (hpbordersize * 2f), hpsize.y + (hpbordersize * 2f));
			renderer.ellipse(qhposition.x - skradius - skborder, qhposition.y - skradius - skborder,  (skradius * 2f) + (skborder * 2f),  (skradius * 2f) + (skborder * 2f), 8);
			renderer.circle(s1position.x, s1position.y, skradius + skborder, 200);
			renderer.circle(s2position.x, s2position.y, skradius + skborder, 200);
			renderer.rect(nposition.x, nposition.y, ((skradius * 2f) + (skborder * 2f)) * 2f - ninsidesize - skborder, (skradius * 2f) + (skborder * 2f));
			renderer.rect(mods1position.x, mods1position.y, modssize.x + modsbordersize * 2f, modssize.y + modsbordersize * 2f);
			renderer.rect(mods2position.x, mods2position.y, modssize.x + modsbordersize * 2f, modssize.y + modsbordersize * 2f);
			renderer.setColor(Color.WHITE);
			renderer.rect(hpposition.x - (hpsize.x / 2f), hpposition.y + hpbordersize, hpsize.x, hpsize.y);
			renderer.rect(expposition.x - (expsize.x / 2f), expposition.y, expsize.x, expsize.y);
			renderer.ellipse(qhposition.x - skradius, qhposition.y - skradius,  (skradius * 2f),  (skradius * 2f), 8);
			renderer.circle(s1position.x, s1position.y, skradius, 200);
			renderer.circle(s2position.x, s2position.y, skradius, 200);
			renderer.rect(n1position.x + ninsidesize / 2f, n1position.y + ninsidesize / 2f, (skradius * 2f) - ninsidesize, (skradius * 2f) -ninsidesize);
			renderer.rect(n2position.x + ninsidesize / 2f, n2position.y + ninsidesize / 2f, (skradius * 2f) - ninsidesize, (skradius * 2f) -ninsidesize);
			renderer.rect(mods1position.x + modsbordersize, mods1position.y + modsbordersize, modssize.x, modssize.y);
			renderer.rect(mods2position.x + modsbordersize, mods2position.y + modsbordersize, modssize.x, modssize.y);
			renderer.setColor(255f / 255f, 95f / 255f, 95f / 255f, 1f);
			renderer.rect(hpposition.x - (hpsize.x / 2f), hpposition.y + hpbordersize, hpsize.x * hpprocess, hpsize.y);
			renderer.setColor(0 / 255f, 255 / 255f, 0 / 255f, 1f);
			renderer.rect(expposition.x - (expsize.x / 2f), expposition.y, expsize.x * expcurrent, expsize.y);
			renderer.setColor(74f / 255f, 134f / 255f, 232f / 255f, 1f);
			renderer.rect(expposition.x - (expsize.x / 2f) + expsize.x * expcurrent, expposition.y, expsize.x * expextra, expsize.y);
		renderer.end();
		
		batch.begin();
			font.draw(batch, "FPS : "+Gdx.graphics.getFramesPerSecond(), 20, uiViewport.getScreenHeight() - 20);
			font.draw(batch, "Lantacy : "+ProjectRPGClient.client.getReturnTripTime()+" ms", 20, uiViewport.getScreenHeight() - 40);
			textlayout.setText(font, String.format("%.0f", hpremain));
			font.draw(batch, textlayout, (hpposition.x - (hpsize.x / 2f)) + (hpsize.x - textlayout.width) / 2, hpposition.y + (hpsize.y + 5f + textlayout.height) / 2f);
			textlayout.setText(font, "86 | Gunner");
			font.draw(batch, textlayout, (hpposition.x - (hpsize.x / 2f)) + (hpsize.x - textlayout.width) / 2, hpposition.y + (hpsize.y + 5f + textlayout.height) / 2f  + 24f);
			textlayout.setText(font, "QH");
			font.draw(batch, textlayout, qhposition.x - (textlayout.width / 2f), qhposition.y + (textlayout.height / 2f));
			textlayout.setText(font, "S1");
			font.draw(batch, textlayout, s1position.x - (textlayout.width / 2f), s1position.y + (textlayout.height / 2f));
			textlayout.setText(font, "S2");
			font.draw(batch, textlayout, s2position.x - (textlayout.width / 2f), s2position.y + (textlayout.height / 2f));
			textlayout.setText(font, "N1");
			font.draw(batch, textlayout, n1position.x + 20f, n1position.y + 35f);
			textlayout.setText(font, "N2");
			font.draw(batch, textlayout, n2position.x + 20f, n2position.y + 35f);
			textlayout.setText(font, "MOD1");
			font.draw(batch, textlayout, mods1position.x + modsbordersize + ((modssize.x - textlayout.width) / 2f), mods1position.y + (modssize.y + 5f + textlayout.height) / 2f);
			textlayout.setText(font, "ERR");
			font.draw(batch, textlayout, mods2position.x + modsbordersize + ((modssize.x - textlayout.width) / 2f), mods2position.y + (modssize.y + 5f + textlayout.height) / 2f);
		batch.end();
	}
	
	public void addCharacter(int id, CharacterData data) {
		if(characters.getOrDefault(id, null) == null) characters.put(id, new Character(data));
	}
	
	public void removeCharacter(int id) {
		if(characters.getOrDefault(id, null) == null) return;
		characters.get(id).dispose();
		characters.remove(id);
	}
	
	public void updateCharacter(int id, CharacterData data) {
		characters.get(id).setPosition(data.getPositionX(), data.getPositionY());
		characters.get(id).setFilpX(data.isFlipX());
		characters.get(id).actionstate = data.actionstate;
	}
	
	public void setMainCharacter(int id, CharacterData data) {
		addCharacter(id, data);
		maincharacter = characters.get(id);
		input.setInputProcessor(new GameplayInputProcess(maincharacter));
		input.setControllerProcessor(new GameplayControllerProcess(maincharacter));
		input.use();
	}
	
	public Character getMainCharacter() {
		return maincharacter;
	}
	
	@Override
	public void dispose() {
		world.dispose();
	}

}
