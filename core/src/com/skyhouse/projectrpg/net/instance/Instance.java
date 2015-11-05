package com.skyhouse.projectrpg.net.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.data.InputData;
import com.skyhouse.projectrpg.data.MapData;
import com.skyhouse.projectrpg.data.StructureData;
import com.skyhouse.projectrpg.net.packets.UpdateResponse;
import com.skyhouse.projectrpg.physics.B2DCharacter;
import com.skyhouse.projectrpg.physics.B2DStructure;

/**
 * Instance.
 * @author Meranote
 */
public class Instance extends Thread {
	
	private World world;
	private MapData mapData;
	private HashMap<String, B2DStructure> structures;
	private HashMap<Integer, B2DCharacter> characters;
	private HashMap<Integer, InputData> inputDataCollection;
	
	private ArrayList<Runnable> postRunnableList;
	
	private double accumulator;
    private double currentTime;
    private float step = 1.0f / 60.0f;
    
    private boolean isFinish = false;
	
    /**
     * Construct a new instance.
     */
    public Instance(MapData map) {
    	this("", map);
    }
    
    /**
     * Construct a new instance.
     */
	public Instance(String name, MapData map) {
		super(name + "-Instance");
		this.mapData = map;
		structures = new HashMap<String, B2DStructure>();
		characters = new HashMap<Integer, B2DCharacter>();
		inputDataCollection = new HashMap<Integer, InputData>();
		postRunnableList = new ArrayList<Runnable>();
		
		world = new World(new Vector2(0, -10f), true);
		for(Entry<String, StructureData> entry : map.structures.entrySet()) {
			structures.put(entry.getKey(), new B2DStructure(world, entry.getValue(), BodyType.StaticBody));
		}
	}
	
	@Override
	public void run() {		
		while(!isFinish) {
			double newTime = TimeUtils.millis() / 1000.0;
	        double frameTime = Math.min(newTime - currentTime, 0.25);
	        float deltaTime = (float)frameTime;

	        currentTime = newTime;
	        accumulator += deltaTime;
	        
	        while(accumulator >= step) {
	        	world.step(step, 8, 3);
	        	accumulator -= step;
	        }
	        
	        for(Entry<Integer, B2DCharacter> entry : characters.entrySet()) {
	        	int id = entry.getKey();
	        	B2DCharacter character = entry.getValue();
	        	character.update(inputDataCollection.get(id));
	        }
	        
	        UpdateResponse update = new UpdateResponse();
	        update.currentInstance = this.mapData.name;
			update.data = new HashMap<Integer, CharacterData>();
			for(Entry<Integer, B2DCharacter> character : characters.entrySet()) {
				update.data.put(character.getKey(), character.getValue().getData());
			}
			for(Integer id : characters.keySet()) {
				ProjectRPG.server.net.sendToUDP(id, update);
			}
	        
	        while(!postRunnableList.isEmpty()) {
	        	Runnable runnable = postRunnableList.remove(0);
	        	if(runnable != null) runnable.run();
	        }
		}
		
		world.dispose();
		Gdx.app.log(ProjectRPG.TITLE, this.getName() + " finished.");
	}
	
	/**
	 * Set this instance to finish its job, if it ended or not.
	 */
	public void finish() {
		postRunnableList.add(new Runnable() {
			@Override
			public void run() {
				isFinish = true;
			}
		});
	}
	
	/**
	 * Add a new character to this instance.
	 */
	public void addCharacter(final int id, final CharacterData data) {
		postRunnableList.add(new Runnable() {
					@Override
					public void run() {
						characters.put(id, new B2DCharacter(world, data));
						inputDataCollection.put(id, new InputData());
					}
		});
	}
	
	/**
	 * Remove the given character id.
	 */
	public void removeCharacter(final int id) {
		postRunnableList.add(new Runnable() {
			@Override
			public void run() {
				inputDataCollection.remove(id);
				B2DCharacter o = characters.remove(id);
				o.dispose();
			}
		});
	}
	
	/**
	 * Update the character by {@link InputData}.
	 */
	public void updateCharacter(final int id, final InputData data) {
		postRunnableList.add(new Runnable() {
			@Override
			public void run() {
				inputDataCollection.put(id, data);
			}
		});
	}

}
