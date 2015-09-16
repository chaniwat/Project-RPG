package com.skyhouse.projectrpg.physics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;

public final class PhysicGlobal {
	
	static World world;
	static Box2DDebugRenderer box2dDebugRenderer;
	
	private PhysicGlobal() {}
	
	public static void init(Vector2 v, boolean doSleep, boolean debug) {
		world = new World(v, doSleep);
		if(debug) box2dDebugRenderer = new Box2DDebugRenderer();
	}
	
	public static void init(float vX, float vY, boolean doSleep, boolean debug) {
		init(new Vector2(vX, vY), doSleep, debug);
	}
	
	public static void debugRender(Camera camera) {
		box2dDebugRenderer.render(world, camera.combined);
	}
	
	public static void debugRender(Viewport viewport) {
		debugRender(viewport.getCamera());
	}

	public static World getWorld() {
		return world;
	}
	
	public static void dispose() {
		box2dDebugRenderer.dispose();
		world.dispose();
	}
}
