package com.skyhouse.projectrpg.net.packets;

import java.util.HashMap;

import com.skyhouse.projectrpg.data.CharacterData;

/**
 * Update response data.
 * @author Meranote
 */
public class UpdateResponse {

	public String currentInstance;
	public HashMap<Integer, CharacterData> data;
	
}
