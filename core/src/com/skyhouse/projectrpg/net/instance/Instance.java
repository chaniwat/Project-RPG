package com.skyhouse.projectrpg.net.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.data.MapData;
import com.skyhouse.projectrpg.net.packets.CharacterDataPacket;
import com.skyhouse.projectrpg.physics.B2DCharacter;
import com.skyhouse.projectrpg.physics.B2DStructure;

public class Instance extends Thread {
	
	private World world;
	private HashMap<String, B2DStructure> structures;
	private HashMap<Integer, B2DCharacter> characters;
	
	private ArrayList<Runnable> postRunnableList;
	
	private double accumulator;
    private double currentTime;
    private float step = 1.0f / 60.0f;
    
    private boolean isFinish = false;
	
	public Instance(MapData map) {
		structures = new HashMap<String, B2DStructure>();
		characters = new HashMap<Integer, B2DCharacter>();
		world = new World(new Vector2(0, -10f), true);
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
	        	world.step(1/60f, 8, 3);
	        	accumulator -= step;
	        }
	        
	        for(B2DCharacter character : characters.values()) {
	        	character.update();
	        }
	        
	        CharacterDataPacket update = new CharacterDataPacket();
			update.characters = new HashMap<Integer, CharacterData>();
			for(Entry<Integer, B2DCharacter> character : characters.entrySet()) {
				update.characters.put(character.getKey(), character.getValue().getData());
			}
			for(Integer id : characters.keySet()) {
				ProjectRPG.Server.net.sendToUDP(id, update);
			}
	        
	        while(!postRunnableList.isEmpty()) {
	        	Runnable runnable = postRunnableList.remove(0);
	        	runnable.run();
	        }
		}
	}
	
	public void addCharacter(CharacterData data) {
		characters.put(data.id, new B2DCharacter(world, data));
	}
	
	public void removeCharacter(int id) {
		B2DCharacter o = characters.remove(id);
		o.dispose();
	}

}