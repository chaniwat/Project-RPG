package com.skyhouse.projectrpg.map;

import java.util.HashMap;

import com.skyhouse.projectrpg.physics.StructureBody;

public class MapBody {

	HashMap<String, StructureBody> structures;
	String name, mapname;
	
	public MapBody(String name, String mapname) {
		structures = new HashMap<String, StructureBody>();
		this.name = name;
		this.mapname = mapname;
	}
	
	public void addStructures(String name, StructureBody structure) {
		structures.put(name, structure);
	}
	
}
