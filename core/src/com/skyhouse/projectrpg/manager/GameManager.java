package com.skyhouse.projectrpg.manager;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.data.InputData;
import com.skyhouse.projectrpg.entity.Character;
import com.skyhouse.projectrpg.input.listener.GameplayControllerListener;
import com.skyhouse.projectrpg.input.listener.GameplayInputListener;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.net.packets.UpdateRequest;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;

/**
 * Manage and control the game state and logic.
 * @author Meranote
 */
public class GameManager extends Manager {
	
	private MapManager mapmanager;
	private EntityManager entitymanager;
	private InputData inputData;
	private World world;
	private String currentInstance;
	private int uid = 0;
	
    /**
     * Construct a new {@link GameManager} class.
     */
    public GameManager() {
    	mapmanager = new MapManager();
    	entitymanager = new EntityManager();
    	inputData = new InputData();
    	world = new World(new Vector2(0, -10f), true);
    	ProjectRPG.client.inputmanager.addInputProcessor(new GameplayInputListener(inputData));
    	ProjectRPG.client.inputmanager.addControllerProcessor(new GameplayControllerListener(inputData));
    }
    
	@Override
	public void update(float deltaTime) {
		if(uid != 0) {			
			mapmanager.update(deltaTime);
			
			if(getPlayerCharacter() != null) {
				getPlayerCharacter().updateCharacterByInputData(inputData);
				UpdateRequest request = new UpdateRequest();
				request.input = inputData;
				request.currentInstance = currentInstance;
				ProjectRPG.client.network.net.sendUDP(request);
			}
		}
	}
	
	@Override
	public void updateFixed(float fixedTime) {
		world.step(fixedTime, 8, 3);
		entitymanager.update(fixedTime);
	}
	
	/**
	 * Update all character except player character by data that receive from current instance.
	 * @param data
	 */
	public void updateAllCharacter(HashMap<Integer, CharacterData> data) {
		for(Entry<Integer, CharacterData> entry : data.entrySet()) {
			if(entitymanager.getAllCharacter().get(entry.getKey()) == null) {
				entitymanager.addCharacter(entry.getKey(), entry.getValue(), new SpriterPlayer("entity/GreyGuy/player.scml"));
			} else if(entry.getKey() == uid) continue;
			entitymanager.getAllCharacter().get(entry.getKey()).updateCharacterByData(entry.getValue());
		}
	}
	
	/**
	 * Get map manager.
	 * @return {@link MapManager}
	 */
	public MapManager getMapManager() {
		return mapmanager;
	}
	
	/**
	 * Get entity manager.
	 * @return {@link EntityManager}
	 */
	public EntityManager getEntityManager() {
		return entitymanager;
	}
	
	/**
	 * Get Box2D world.
	 * @return {@link World}
	 */
	public World getWorld() {
		return world;
	}
	
	/**
	 * Get the current connected instance. 
	 * @return
	 */
	public String getCurrentInstance() {
		return currentInstance;
	}
	
	/**
	 * Set the current instance to the given one.
	 * @param instance
	 */
	public void setCurrentInstance(String instance) {
		currentInstance = instance;
	}
	
	/**
	 * Get the current map.
	 * @return {@link map}
	 */
	public Map getCurrentMap() {
		return mapmanager.getMap(currentInstance);
	}
	
	/**
	 * Get the player character .
	 * @return {@link Character}
	 */
	public Character getPlayerCharacter() {
		return entitymanager.getCharacter(uid);
	}
	
	@Override
	public void dispose() {
		world.dispose();
	}
	
}
