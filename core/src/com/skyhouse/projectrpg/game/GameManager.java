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
import com.skyhouse.projectrpg.data.InputData;
import com.skyhouse.projectrpg.entity.Character;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.input.listener.GameplayControllerProcess;
import com.skyhouse.projectrpg.input.listener.GameplayInputProcess;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.net.packets.UpdateRequest;
import com.skyhouse.projectrpg.scene.input.SceneInput;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;
import com.skyhouse.projectrpg.utils.asset.loader.MapLoader.MapLoaderParameter;
import com.skyhouse.projectrpg.utils.scene.SceneManager;

public class GameManager {
	
	private AssetManager assetmanager = ProjectRPG.Client.assetmanager;
	private SceneManager scenemanager = ProjectRPG.Client.scenemanager;
	private ArrayList<String> loadingMapPath = new ArrayList<String>();
	private ArrayList<String> finishLoadMapPath = new ArrayList<String>();
	private SceneInput inputHandle;
	private InputData inputData = new InputData();
	private World world = new World(new Vector2(0, -10f), true);
	private HashMap<String, Map> maps = new HashMap<String, Map>();
	private HashMap<Integer, Character> characters = new HashMap<Integer, Character>();
	private String currentMap;
	private int currentCharacter;
	private Sprite background = new Sprite();
	private double accumulator;
    private float step = 1.0f / 60.0f;
	
	public GameManager(SceneInput gameSceneInput) {
		this.inputHandle = gameSceneInput;
	}
	
	public void update(float deltaTime) {
		checkLoadedMap();
		updateBackground();
		
		if(getControlCharacter() != null) {
			UpdateRequest request = new UpdateRequest();
			request.input = inputData;
			request.currentInstance = ProjectRPG.Client.network.currentInstance;
			ProjectRPG.Client.network.net.sendUDP(request);
		}

        accumulator += deltaTime;
		
        while(accumulator >= step) {
			world.step(step, 8, 3);
			for(Character c : characters.values()) {
				if(c.equals(getControlCharacter())) c.update(step);
				else c.update(step);
			}
			accumulator -= step;
        }
	}
	
	private void checkLoadedMap() {
		for(String path : loadingMapPath) {
			if(assetmanager.isLoaded(path)) {
				Map m = assetmanager.get(path, Map.class);
				maps.put(m.getData().name, m);
				if(getCurrentMap() == null) setCurrentMap(m.getData().name);
				finishLoadMapPath.add(path);
			}
		}
		while(!finishLoadMapPath.isEmpty()) loadingMapPath.remove(finishLoadMapPath.remove(0));
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
				addCharacter(0, entry.getValue(), new SpriterPlayer("entity/GreyGuy/player.scml"));
			}
			characters.get(entry.getKey()).updateCharacterByData(entry.getValue());
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
	
	public Collection<Map> getAllMap() {
		return maps.values();
	}
	
	public Collection<Character> getAllCharacter() {
		return characters.values();
	}
	
	public Map getMap(String name) {
		return maps.get(name);
	}
	
	public void setCurrentMap(String name) {
		currentMap = name;
	}
	
	public Map getCurrentMap() {
		return maps.get(currentMap);
	}
	
	public void removeMap(String name) {
		maps.get(name).dispose();
	}
	
	public void addCharacter(int id, CharacterData data, SpriterPlayer player) {
		characters.put(id, new Character(player, data));
	}
	
	public void setControlCharacter(int id) {
		currentCharacter = id;
		inputHandle.setInputProcessor(new GameplayInputProcess(inputData));
		inputHandle.setControllerProcessor(new GameplayControllerProcess(inputData));
		inputHandle.use();
	}
	
	public Character getControlCharacter() {
		return characters.get(currentCharacter);
	}

	public void removeCharacter(int connectionid) {
		characters.remove(connectionid).dispose();
	}
	
	public Sprite getBackground() {
		return background;
	}
	
	public void dispose() {
		world.dispose();
	}
}
