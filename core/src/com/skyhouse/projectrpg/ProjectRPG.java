package com.skyhouse.projectrpg;

import com.badlogic.gdx.assets.AssetManager;
import com.skyhouse.projectrpg.scene.SceneManager;

/**
 * ProjectRPG.
 * @author Meranote
 */
public class ProjectRPG {
	
	private ProjectRPG() {}
	
	public static final String TITLE = "Project RPG";
	public static final String VERSION = "0.2.0 (Multiplayer)";
	
	/**
	 * Client of ProjectRPG.
	 * @author Meranote
	 *
	 */
	public static class Client {
		
		private Client() { }
		
		/** 
		 * Network class.
		 * @see com.esotericsoftware.kryonet.Client 
		 */
		public static com.esotericsoftware.kryonet.Client net;
		public static AssetManager assetmanager;
		public static SceneManager scenemanager;
		
	}
	
	/**
	 * Server of ProjectRPG.
	 * @author Meranote
	 */
	public static class Server {
		
		private Server() { }
		
		/** 
		 * Network class.
		 * @see com.esotericsoftware.kryonet.Server 
		 */
		public static com.esotericsoftware.kryonet.Server net;
		
	}
	
}
