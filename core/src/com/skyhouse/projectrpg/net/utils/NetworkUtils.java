package com.skyhouse.projectrpg.net.utils;

import java.util.HashMap;

import com.esotericsoftware.kryo.Kryo;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.data.Data;
import com.skyhouse.projectrpg.data.InputData;
import com.skyhouse.projectrpg.net.packets.UpdateRequest;
import com.skyhouse.projectrpg.net.packets.UpdateResponse;
import com.skyhouse.projectrpg.net.packets.DisconnectRequest;
import com.skyhouse.projectrpg.net.packets.DisconnectResponse;
import com.skyhouse.projectrpg.net.packets.InitialRequest;
import com.skyhouse.projectrpg.net.packets.InitialResponse;

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
		kryo.register(HashMap.class);
		kryo.register(CharacterData.class);
		kryo.register(Data.ActionState.class);
		kryo.register(InputData.class);
		kryo.register(InitialRequest.class);
		kryo.register(InitialResponse.class);
		kryo.register(DisconnectRequest.class);
		kryo.register(DisconnectResponse.class);
		kryo.register(UpdateResponse.class);
		kryo.register(UpdateRequest.class);
	}
	
}
