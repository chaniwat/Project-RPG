package com.skyhouse.projectrpg.objects;

import com.badlogic.gdx.Gdx;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.PlayerTweener;
import com.brashmonkey.spriter.Spriter;
import com.skyhouse.projectrpg.utils.spriter.SpriterPlayerListener;

public class SpriterActor {

	PlayerTweener player;
	String c_animation;
	
	public SpriterActor(String pathtoscml) {
		Spriter.load(Gdx.files.internal(pathtoscml).read(), pathtoscml);
		player = (PlayerTweener)Spriter.newPlayer(pathtoscml, 0, PlayerTweener.class);
	}
	
	public void changeAnimationTo(String animationname) {
		c_animation = animationname;
		getPlayer().getSecondPlayer().setTime(0);
	}
	
	public void updateTweener() {
		if(!getFirstPlayer().getAnimation().name.equals(c_animation)) {
			getSecondPlayer().setAnimation(c_animation);
			if(getPlayer().getWeight() < 1) {
				getPlayer().setWeight(getPlayer().getWeight() + 4 * Gdx.graphics.getDeltaTime());
			}
			else if(getPlayer().getWeight() > 1) {
				getFirstPlayer().setAnimation(getSecondPlayer().getAnimation().name);
				getPlayer().setWeight(0.0f);
			}
		} else {
			if(getPlayer().getWeight() > 0) {
				getPlayer().setWeight(getPlayer().getWeight() - 4 * Gdx.graphics.getDeltaTime());
			}
			else if(getPlayer().getWeight() < 0) {
				getPlayer().setWeight(0.0f);
			}
		}
	}
	
	public PlayerTweener getPlayer () {
		return player;
	}
	
	public Player getFirstPlayer() {
		return player.getFirstPlayer();
	}
	
	public Player getSecondPlayer() {
		return player.getSecondPlayer();
	}
	
	public void addPlayerListener(SpriterPlayerListener listener) {
		getSecondPlayer().addListener(listener);
	}
	
	public void remove() {
	}
	
}
