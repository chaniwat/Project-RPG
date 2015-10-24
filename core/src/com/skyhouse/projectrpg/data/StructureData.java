package com.skyhouse.projectrpg.data;


/**
 * Structure data.
 * @author Meranote
 */
public class StructureData extends Data {
	
	/**
	 * Define behavior of structure.
	 * @author Meranote
	 */
	public static enum StructureBehavior {
		NONE, SOLID, CLUMBABLE
	}
	
	public float x, y;
	public float width, height;
	public StructureBehavior behavior;
	
}
