	package com.skyhouse.projectrpg.net.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.kryo.Kryo;
import com.skyhouse.projectrpg.data.ActorData;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.data.CharacterEquipData;
import com.skyhouse.projectrpg.data.Data;
import com.skyhouse.projectrpg.data.InputData;
import com.skyhouse.projectrpg.data.InventoryData;
import com.skyhouse.projectrpg.data.QuestData;
import com.skyhouse.projectrpg.data.QuickSlotData;
import com.skyhouse.projectrpg.data.StatusData;
import com.skyhouse.projectrpg.net.packets.CharacterRequest;
import com.skyhouse.projectrpg.net.packets.CharacterResponse;
import com.skyhouse.projectrpg.net.packets.CreateCharacterRequest;
import com.skyhouse.projectrpg.net.packets.CreateCharacterResponse;
import com.skyhouse.projectrpg.net.packets.PlayRequest;
import com.skyhouse.projectrpg.net.packets.PlayResponse;
import com.skyhouse.projectrpg.net.packets.UpdateRequest;
import com.skyhouse.projectrpg.net.packets.UpdateResponse;
import com.skyhouse.projectrpg.net.packets.DisconnectRequest;
import com.skyhouse.projectrpg.net.packets.DisconnectResponse;
import com.skyhouse.projectrpg.net.packets.LoginRequest;
import com.skyhouse.projectrpg.net.packets.LoginResponse;

/**
 * Utility class of network.
 * @author Meranote
 */
public class NetworkUtils {

	/**
	 * Registering the class before start the client or server.
	 * @param kryo {@link Kryo} object
	 */
	public static void registerKryoClass(Kryo kryo) {
		kryo.register(int[].class);
		kryo.register(ArrayList.class);
		kryo.register(HashMap.class);
		kryo.register(Data.class);
		kryo.register(ActorData.ActionState.class);
		kryo.register(CharacterData.class);
		kryo.register(CharacterEquipData.class);
		kryo.register(StatusData.class);
		kryo.register(QuestData.class);
		kryo.register(QuickSlotData.class);
		kryo.register(InventoryData.class);
		kryo.register(InputData.class);
		kryo.register(LoginRequest.class);
		kryo.register(LoginResponse.class);
		kryo.register(CharacterRequest.class);
		kryo.register(CharacterResponse.class);
		kryo.register(CreateCharacterRequest.class);
		kryo.register(CreateCharacterResponse.class);
		kryo.register(PlayRequest.class);
		kryo.register(PlayResponse.class);
		kryo.register(DisconnectRequest.class);
		kryo.register(DisconnectResponse.class);
		kryo.register(UpdateResponse.class);
		kryo.register(UpdateRequest.class);
	}
	
}
