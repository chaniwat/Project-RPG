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
import com.skyhouse.projectrpg.data.MapData;
import com.skyhouse.projectrpg.data.StructureData;
import com.skyhouse.projectrpg.data.CharacterData.CharacterInputState;
import com.skyhouse.projectrpg.net.packets.UpdateResponse;
import com.skyhouse.projectrpg.physics.B2DCharacter;
import com.skyhouse.projectrpg.physics.B2DStructure;

public class Instance extends Thread {
	
	private World world;
	private MapData mapData;
	private HashMap<String, B2DStructure> structures;
	private HashMap<Integer, B2DCharacter> characters;
	
	private ArrayList<Runnable> postRunnableList;
	
	private double accumulator;
    private double currentTime;
    private float step = 1.0f / 60.0f;
    
    private boolean isFinish = false;
	
    public Instance(MapData map) {
    	this("", map);
    }
    
	public Instance(String name, MapData map) {
		super(name + "-Instance");
		this.mapData = map;
		structures = new HashMap<String, B2DStructure>();
		characters = new HashMap<Integer, B2DCharacter>();
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
	        
	        for(B2DCharacter character : characters.values()) {
	        	character.update();
	        }
	        
	        UpdateResponse update = new UpdateResponse();
	        update.currentInstance = this.mapData.name;
			update.data = new HashMap<Integer, CharacterData>();
			for(Entry<Integer, B2DCharacter> character : characters.entrySet()) {
				update.data.put(character.getKey(), character.getValue().getData());
			}
			for(Integer id : characters.keySet()) {
				ProjectRPG.Server.net.sendToUDP(id, update);
			}
	        
	        while(!postRunnableList.isEmpty()) {
	        	Runnable runnable = postRunnableList.remove(0);
	        	if(runnable != null) runnable.run();
	        }
		}
		
		world.dispose();
		Gdx.app.log(ProjectRPG.TITLE, this.getName() + " finished.");
	}
	
	public void finish() {
		postRunnableList.add(new Runnable() {
			@Override
			public void run() {
				isFinish = true;
			}
		});
	}
	
	public void addCharacter(final CharacterData data) {
		postRunnableList.add(new Runnable() {
					@Override
					public void run() {
						characters.put(data.id, new B2DCharacter(world, data));
					}
		});
	}
	
	public void removeCharacter(final int id) {
		postRunnableList.add(new Runnable() {
			@Override
			public void run() {
				B2DCharacter o = characters.remove(id);
				o.dispose();
			}
		});
	}
	
	public void updateCharacter(final int id, final CharacterInputState data) {
		postRunnableList.add(new Runnable() {
			@Override
			public void run() {
				characters.get(id).getData().inputstate = data;
			}
		});
	}

}
