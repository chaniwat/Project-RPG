package com.skyhouse.projectrpg.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.skyhouse.projectrpg.data.StructureData;

/**
 * Box2D Structure object.
 * @author Meranote
 *
 */
public class B2DStructure extends B2DObject{

	private StructureData data;
	
	/**
	 * Construct a new structure object.
	 * @param world
	 * @param data
	 * @param type
	 */
	public B2DStructure(World world, StructureData data, BodyType type) {
		super(world, new Vector2(data.x + (data.width / 2f), data.y - (data.height / 2f)), new Vector2(data.width, data.height), type, 0.0f);
		this.data = data;
	}

	@Override
	public void update() {
				
	}
	
	/**
	 * @return {@link StructureData}
	 */
	public StructureData getData() {
		return data;
	}
	
}
