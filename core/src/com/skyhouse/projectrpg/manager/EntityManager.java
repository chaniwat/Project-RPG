package com.skyhouse.projectrpg.manager;

import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.World;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.entity.Character;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;

public class EntityManager extends Manager {

	private HashMap<Integer, Character> characters = new HashMap<Integer, Character>();
	
	@Override
	public void update(float deltaTime) {
		
	}
	
	public void addCharacter(int id, CharacterData data, SpriterPlayer player) {
		addCharacter(id, data, player, null);
	}
	
	public void addCharacter(int id, CharacterData data, SpriterPlayer player, World world) {
		Character c;
		if(world == null) {
			c = new Character(player, data);
		} else {
			c = new Character(world, player, data);
		}
		characters.put(id, c);
	}
	
	public HashMap<Integer, Character> getAllCharacter() {
		return characters;
	}
	
	public Character getCharacter(int id) {
		return characters.get(id);
	}

	public void removeCharacter(int connectionid) {
		characters.remove(connectionid).dispose();
	}

	@Override
	public void dispose() {
		characters.clear();
	}

}
