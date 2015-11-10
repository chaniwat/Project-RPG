package com.skyhouse.projectrpg.manager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.InputData;
import com.skyhouse.projectrpg.entity.Character;
import com.skyhouse.projectrpg.input.listener.GameplayControllerListener;
import com.skyhouse.projectrpg.input.listener.GameplayInputListener;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.net.packets.UpdateRequest;

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
		mapmanager.update(deltaTime);
		entitymanager.update(deltaTime);
		
		if(isGameReady() && uid != 0) {
			if(getPlayerCharacter() != null) {
				/** Update input data to simulate physics */
				getPlayerCharacter().updateCharacterByInputData(inputData);
				
//				/** Send update request to current instance */
//				UpdateRequest request = new UpdateRequest();
//				request.input = inputData;
//				request.currentInstance = currentInstance;
//				ProjectRPG.client.network.net.sendUDP(request);
			}
		}
	}
	
	@Override
	public void updateFixed(float fixedTime) {
		if(isGameReady()) {
			world.step(fixedTime, 8, 3);
		}
	}
	
	@Override
	public void dispose() {
		world.dispose();
	}
	
	/**
	 * Get entity manager.
	 * @return {@link EntityManager}
	 */
	public EntityManager getEntityManager() {
		return entitymanager;
	}

	/**
	 * Get map manager.
	 * @return {@link MapManager}
	 */
	public MapManager getMapManager() {
		return mapmanager;
	}

	/**
	 * Get the current connected instance. 
	 * @return
	 */
	public String getCurrentInstance() {
		return currentInstance;
	}

	/**
	 * Get the current map.
	 * @return {@link map}
	 */
	public Map getCurrentMap() {
		return mapmanager.getCurrentMap();
	}

	/**
	 * Get the player character .
	 * @return {@link Character}
	 */
	public Character getPlayerCharacter() {
		return entitymanager.getCharacter(uid);
	}

	/**
	 * Get Box2D world.
	 * @return {@link World}
	 */
	public World getGameWorld() {
		return world;
	}

	/**
	 * Set the current instance to the given one.
	 * @param instance
	 */
	public void setCurrentInstance(String instance) {
		currentInstance = instance;
	}

	/**
	 * Get current player uid.
	 * @return
	 */
	public int getUID() {
		return uid;
	}

	/**
	 * Set current player uid.
	 * @return
	 */
	public void setUID(int uid) {
		this.uid = uid;
	}
	
	/**
	 * Check the game asset was ready.
	 * @return
	 */
	public boolean isGameReady() {
		return entitymanager.isEntityReady() && mapmanager.isMapReady();
	}
	
}
