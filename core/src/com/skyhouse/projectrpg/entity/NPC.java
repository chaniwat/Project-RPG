package com.skyhouse.projectrpg.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;

public class NPC extends Actor {

	private SpriterPlayer player;
	
	public NPC(SpriterPlayer player, float size) {
		this.player = player;
		this.player.setScale(size / player.getBoundingRectangle(null).size.height);
	}

	@Override
	public void update(float deltatime) {
		player.update(deltatime);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		player.draw();
	}

	@Override
	public void dispose() {
		
	}
	
	public SpriterPlayer getPlayer() {
		return player;
	}
	
}
