package com.skyhouse.projectrpg;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.skyhouse.projectrpg.manager.EntityManager;
import com.skyhouse.projectrpg.manager.GameManager;
import com.skyhouse.projectrpg.manager.InputManager;
import com.skyhouse.projectrpg.net.instance.Instance;
import com.skyhouse.projectrpg.utils.scene.SceneManager;

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
	
	/**
	 * @author Meranote
	 */
	public static class Client {
		
		private Client() { }
		
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
			public static com.esotericsoftware.kryonet.Client net;
		}
		
	}
	
	/**
	 * @author Meranote
	 */
	public static class Server {
		
		private Server() { }
		public static com.esotericsoftware.kryonet.Server net;
		public static HashMap<String, Instance> instances;
		
	}
	
}
