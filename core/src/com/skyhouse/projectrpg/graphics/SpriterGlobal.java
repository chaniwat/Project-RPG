package com.skyhouse.projectrpg.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Spriter;
import com.skyhouse.projectrpg.utils.spriter.SpriterDrawer;
import com.skyhouse.projectrpg.utils.spriter.SpriterLoader;

public class SpriterGlobal extends Spriter {
	
	private static SpriteBatch batch;
	private static ShapeRenderer renderer;
	
	private SpriterGlobal() {}
	
	public static void init(SpriteBatch batch) {
		SpriterGlobal.batch = batch;
		renderer = new ShapeRenderer();
		
		Spriter.setDrawerDependencies(batch, renderer);
		Spriter.init(SpriterLoader.class, SpriterDrawer.class);
	}
	
	public static void setProjectionMatrix(Matrix4 combined) {
		batch.setProjectionMatrix(combined);
		renderer.setProjectionMatrix(combined);
	}
	
	public static void updateAndDraw() {
		batch.begin();
			Spriter.updateAndDraw();
		batch.end();
	}
	
	public static void updateAndDrawExcept(Player except) {
		batch.begin();
			Spriter.updateAndDrawExcept(except);
		batch.end();
	}
	
	public static void draw(Player player) {
		batch.begin();
			Spriter.draw(player);
		batch.end();
	}
	
	public static void dispose() {
		Spriter.dispose();
		batch.dispose();
		renderer.dispose();
	}
}