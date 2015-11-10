package com.skyhouse.projectrpg;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.skyhouse.projectrpg.manager.EntityManager;
import com.skyhouse.projectrpg.manager.GameManager;
import com.skyhouse.projectrpg.manager.InputManager;
import com.skyhouse.projectrpg.manager.MapManager;
import com.skyhouse.projectrpg.manager.SceneManager;
import com.skyhouse.projectrpg.net.database.Database;
import com.skyhouse.projectrpg.net.instance.Instance;
import com.skyhouse.projectrpg.net.instance.TownInstance;
import com.skyhouse.projectrpg.server.system.PlayerManagementSystem;

/**
 * @author Meranote
 */
public class ProjectRPG {
	
	private ProjectRPG() {}
	
	public static final String TITLE = "Project RPG";
	public static final String VERSION = "0.2.8 WIP";
	public static final String VERSION_NAME = "Multiplayer";
	public static final String AUTHOR = "Skyhouse Team.";
	
	// Accumulator for fixed time updated.
	private static float accumulator;
	private static int updateFixedCount;
	private static final float FIXEDTIME = 1f / 60f;
	
	/**
	 * @author Meranote
	 */
	@SuppressWarnings("typename")
	public static class client {
		
		public static AssetManager assetmanager;
		public static SceneManager scenemanager;
		public static GameManager gamemanager;
		public static InputManager inputmanager;
		public static MapManager mapmanager;
		public static EntityManager entitymanager;
		
		/**
		 * @author Meranote
		 */
		@SuppressWarnings("typename")
		public static class graphic {
			public static SpriteBatch batch;
			public static ShapeRenderer renderer;
			
			/**
			 * Font asset.
			 * @author Meranote
			 */
			public static class font {
				public static GlyphLayout layout;
				public static BitmapFont regular;
				public static BitmapFont smallregular;
				public static BitmapFont bold;
				public static BitmapFont smallbold;
			}
			
			/**
			 * Enable GL function for blending alpha color, likely use with {@link ShapeRenderer}.
			 */
			public static void enableGLAlphaBlend() {
				Gdx.gl.glEnable(GL11.GL_BLEND);
				Gdx.gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}
			
			/**
			 * Disable GL function for blending alpha color.
			 */
			public static void disableGLAlphaBlend() {
				Gdx.gl.glDisable(GL11.GL_BLEND);
			}
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
		
		public static class system {
			public static PlayerManagementSystem playermanagement;
		}
		
		public static Server net;
		
		public static TownInstance townInstance;
		public static HashMap<String, Instance> instances;
		
		public static Database database;
		public static ArrayList<Integer> logginUID;
		
	}
	
	/**
	 * Update fixed time.<br>
	 * Should be called before any update happen.
	 * @param deltaTime normal delta time
	 */
	public static void updateFixedTime(float deltaTime) {
		accumulator += deltaTime;
		updateFixedCount = 0;
		while(accumulator >= FIXEDTIME) {
			accumulator -= FIXEDTIME;
			updateFixedCount++;
		}
	}
	
	/**
	 * Get how many time that need to be update for fixed update.
	 * @return count to loop for fixed update.
	 */
	public static int getUpdateFixedCount() {
		return updateFixedCount;
	}
	
	/**
	 * Get fixed time step.
	 */
	public static float getStep() {
		return FIXEDTIME;
	}
	
}
