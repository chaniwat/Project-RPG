package com.skyhouse.projectrpg.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.entity.Character;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.input.GameplayControllerProcess;
import com.skyhouse.projectrpg.input.GameplayInputProcess;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.net.packets.UpdateRequest;
import com.skyhouse.projectrpg.scene.input.SceneInput;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;
import com.skyhouse.projectrpg.utils.assetloader.MapLoader.MapLoaderParameter;

public class GameManager {
	
	private AssetManager assetmanager;
	private ArrayList<String> loadingMapPath;
	private ArrayList<String> finishLoadMapPath;
	private World world;
	private HashMap<String, Map> maps;
	private HashMap<Integer, Character> characters;
	private String currentMap;
	private int currentCharacter;
	private Sprite background;
	private SceneInput input;
	private GameplayViewport viewport;
	private double accumulator;
    private float step = 1.0f / 60.0f;
	
	public GameManager(AssetManager assetmanager, SceneInput input) {
		this.assetmanager = assetmanager;
		this.input = input;
		maps = new HashMap<String, Map>();
		characters = new HashMap<Integer, Character>();
		loadingMapPath = new ArrayList<String>();
		finishLoadMapPath = new ArrayList<String>();
		background = new Sprite();
		world = new World(new Vector2(0, -10f), true);
	}
	
	public void update(float deltaTime) {
		for(String path : loadingMapPath) {
			if(assetmanager.isLoaded(path)) {
				Map m = assetmanager.get(path, Map.class);
				maps.put(m.getData().name, m);
				if(getCurrentMap() == null) setCurrentMap(m.getData().name);
				finishLoadMapPath.add(path);
			}
		}
		while(!finishLoadMapPath.isEmpty()) loadingMapPath.remove(finishLoadMapPath.remove(0));
		
		updateBackground();
		
		if(getControlCharacter() != null) {
			UpdateRequest request = new UpdateRequest();
			request.input = getControlCharacter().getData().inputstate;
			request.currentInstance = ProjectRPG.Client.network.currentInstance;
			ProjectRPG.Client.network.net.sendUDP(request);
		}

        accumulator += deltaTime;
		
        while(accumulator >= step) {
			world.step(step, 8, 3);
			for(Character c : characters.values()) {
				if(c.equals(getControlCharacter())) c.update(step, true);
				else c.update(step, false);
			}
			if(getControlCharacter() != null) {
				viewport.setViewCenterToCharacter(getControlCharacter(), 0, 1.5f);
				background.setPosition(-(background.getWidth() / 2f) + (getControlCharacter().getData().x * 0.35f), -2f + (getControlCharacter().getData().y * 0.35f));
			}
			accumulator -= step;
        }
	}
	
	private void updateBackground() {
		if((background.getTexture() == null || !background.getTexture().equals(getCurrentMap().getBackground())) && getCurrentMap() != null) {
			float baseHeight = 17f;
			Texture t = getCurrentMap().getBackground();
			background.setRegion(t);
			background.setSize(baseHeight * (t.getWidth()/t.getHeight()), baseHeight);
			background.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);			
		}
	}
	
	public void updateCharacter(HashMap<Integer, CharacterData> data) {
		for(Entry<Integer, CharacterData> entry : data.entrySet()) {
			if(characters.get(entry.getKey()) == null) {
				addCharacter(entry.getValue(), new SpriterPlayer("entity/GreyGuy/player.scml"));
			}
			characters.get(entry.getKey()).updateCharacter(entry.getValue());
		}
	}
	
	public World getB2DWorld() {
		return world;
	}

	public void addMap(String pathToMap) {
		MapLoaderParameter params = new MapLoaderParameter();
		params.world = world;
		assetmanager.load(pathToMap, Map.class, params);
		loadingMapPath.add(pathToMap);
	}
	
	public void addCharacter(CharacterData data, SpriterPlayer player) {
		characters.put(data.id, new Character(world, player, data));
	}
	
	public Collection<Map> getAllMap() {
		return maps.values();
	}
	
	public Collection<Character> getAllCharacter() {
		return characters.values();
	}
	
	public Map getMap(String name) {
		return maps.get(name);
	}
	
	public Map getCurrentMap() {
		return maps.get(currentMap);
	}

	public void setCurrentMap(String name) {
		currentMap = name;
	}
	
	public Character getControlCharacter() {
		return characters.get(currentCharacter);
	}
	
	public void setControlCharacter(int id) {
		currentCharacter = id;
		input.setInputProcessor(new GameplayInputProcess(characters.get(id)));
		input.setControllerProcessor(new GameplayControllerProcess(characters.get(id)));
		input.use();
	}
	
	public void dispose() {
		world.dispose();
	}

	public void removeCharacter(int connectionid) {
		characters.remove(connectionid).getBody().dispose();
	}
	
	public Sprite getBackground() {
		return background;
	}

	public void setGameplayViewport(GameplayViewport viewport) {
		this.viewport = viewport;
	}
}
