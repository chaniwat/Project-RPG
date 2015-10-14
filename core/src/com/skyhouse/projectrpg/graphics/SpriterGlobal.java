package com.skyhouse.projectrpg.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Spriter;
import com.skyhouse.projectrpg.utils.spriter.SpriterDrawer;
import com.skyhouse.projectrpg.utils.spriter.SpriterLoader;

public class SpriterGlobal extends Spriter {
	
	private SpriterGlobal() {}
	
	public static void init(SpriteBatch batch,  ShapeRenderer renderer) {
		renderer = new ShapeRenderer();
		
		Spriter.setDrawerDependencies(batch, renderer);
		Spriter.init(SpriterLoader.class, SpriterDrawer.class);
	}
	
	public static void updateAndDraw(Player maincharacter) {
		Spriter.update();
		Spriter.drawAndLast(maincharacter);
	}
	
	public static void dispose() {
		Spriter.dispose();
	}
}
