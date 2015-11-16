package com.skyhouse.projectrpg.factory;

import java.util.HashMap;

import com.skyhouse.projectrpg.entity.NPC;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;

public class NpcFactory extends Factory {

	public static HashMap<Integer, NPC> npcLists = new HashMap<Integer, NPC>();
	
	private NpcFactory() { }

	public static void prepareAsset() {
		// TODO Auto-generated method stub
		
	}

	public static void register() {
		int i = 0;
		npcLists.put(i++, new NPC(new SpriterPlayer("entity/npc/npc_blacksmith/blacksmith.scml"), 3.8f));
		npcLists.put(i++, new NPC(new SpriterPlayer("entity/npc/npc_swordman/swordman.scml"), 3.8f));
	}

}
