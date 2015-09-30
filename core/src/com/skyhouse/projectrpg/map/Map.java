package com.skyhouse.projectrpg.map;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skyhouse.projectrpg.entities.Structure;

public class Map {

	HashMap<String, Structure> structures;
	String name, mapname;
	
	public Map(String name, String mapname) {
		structures = new HashMap<String, Structure>();
		this.name = name;
		this.mapname = mapname;
	}
	
	public void addStructures(String name, Structure structure) {
		structures.put(name, structure);
	}
	
	public void draw(SpriteBatch batch) {
		for(Structure structure : structures.values()) {
			structure.render(batch);
		}
	}
	
}
