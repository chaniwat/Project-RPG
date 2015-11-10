package com.skyhouse.projectrpg.manager;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.physics.box2d.World;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.entity.Character;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;

/**
 * Manage character and monster, Contain the collection of {@link Character} and Monster.
 * @author Meranote
 */
public class EntityManager extends Manager {

	private HashMap<Integer, Character> characters;
	
	/**
	 * Construct a new {@link EntityManager}.
	 */
	public EntityManager() {
		characters = new HashMap<Integer, Character>();
	}
	
	@Override
	public void update(float deltaTime) {
		for(Character c : characters.values()) {
			c.update(deltaTime);
		}
	}
	
	/**
	 * Add a new non-controllable character.
	 */
	public void addCharacter(int id, CharacterData data, SpriterPlayer player) {
		addCharacter(id, data, player, null);
	}
	
	/**
	 * Add a new controllable character by given world.
	 */
	public void addCharacter(int id, CharacterData data, SpriterPlayer player, World world) {
		Character c;
		if(world == null) c = new Character(player, data);
		else c = new Character(world, player, data);
		characters.put(id, c);
	}
	
	/**
	 * Get the collection of {@link Character}.<br >
	 * <b>Key is represent to client connection id</b>
	 * @return {@link HashMap}<{@link Integer}, {@link Character}>
	 */
	public HashMap<Integer, Character> getAllCharacter() {
		return characters;
	}
	
	/**
	 * Get the given id character.
	 * @return {@link Character}
	 */
	public Character getCharacter(int id) {
		return characters.get(id);
	}

	/**
	 * Remove the given id character.
	 */
	public void removeCharacter(int id) {
		characters.remove(id).dispose();
	}
	
	/**
	 * Update all character by data that receive from current instance.
	 * @param uid
	 * @param data
	 * @param syncCharacterPlayer need to update player character?
	 */
	public void updateAllCharacter(int uid, HashMap<Integer, CharacterData> data, boolean syncCharacterPlayer) {
		for(Entry<Integer, CharacterData> entry : data.entrySet()) {
			if(getAllCharacter().get(entry.getKey()) == null) {
				addCharacter(entry.getKey(), entry.getValue(), new SpriterPlayer("entity/GreyGuy/player.scml"));
			} else if(entry.getKey() == uid) continue;
			getAllCharacter().get(entry.getKey()).updateCharacterByData(entry.getValue());
		}
	}

	@Override
	public void dispose() {
		characters.clear();
	}
	
	/**
	 * Check all entity that loaded and ready.
	 */
	public boolean isEntityReady() {
		return !getAllCharacter().isEmpty();
	}

}
