package com.skyhouse.projectrpg;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.skyhouse.projectrpg.manager.GameManager;
import com.skyhouse.projectrpg.manager.InputManager;
import com.skyhouse.projectrpg.manager.SceneManager;
import com.skyhouse.projectrpg.net.database.Database;
import com.skyhouse.projectrpg.net.instance.Instance;

/**
 * ProjectRPG.
 * @author Meranote
 */
public class ProjectRPG {
	
	private ProjectRPG() {}
	
	public static final String TITLE = "Project RPG";
	public static final String VERSION = "0.2.5 WIP";
	public static final String VERSION_NAME = "Multiplayer";
	public static final String AUTHOR = "Skyhouse Team.";
	
	private static String applicationSide = null;
	
	// Accumulator for fixed time updated.
	private static float accumulator;
	private static int updateFixedCount;
	private static final float fixedTime = 1f / 60f;
	
	/**
	 * @author Meranote
	 */
	@SuppressWarnings("typename")
	public static class client {
		
		private client() { applicationSide = "Client"; }
		
		public static AssetManager assetmanager;
		public static SceneManager scenemanager;
		public static GameManager gamemanager;
		public static InputManager inputmanager;
		
		/**
		 * @author Meranote
		 */
		@SuppressWarnings("typename")
		public static class graphic {
			public static SpriteBatch batch;
			public static ShapeRenderer renderer;	
		}
		
		/**
		 * @author Meranote
		 */
		@SuppressWarnings("typename")
		public static class network {
			public static Client net;
		}
		
	}
	
	/**
	 * @author Meranote
	 */
	@SuppressWarnings("typename")
	public static class server {
		
		private server() { applicationSide = "Server"; }
		
		public static Server net;
		public static Database database;
		public static HashMap<String, Instance> instances;
		
	}
	
	/**
	 * Get app-side of application.
	 * @return Client or Server
	 */
	public static String getApplicationSide() {
		return applicationSide;
	}
	
	public static void updateFixedTime(float deltaTime) {
		accumulator += deltaTime;
		updateFixedCount = 0;
		while(accumulator >= fixedTime) {
			accumulator -= fixedTime;
			updateFixedCount++;
		}
	}
	
	public static int getUpdateFixedCount() {
		return updateFixedCount;
	}
	
	public static float getStep() {
		return fixedTime;
	}
	
}
