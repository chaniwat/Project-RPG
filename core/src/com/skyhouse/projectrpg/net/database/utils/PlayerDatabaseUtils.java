package com.skyhouse.projectrpg.net.database.utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.TreeSet;

import com.badlogic.gdx.Gdx;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.data.CharacterEquipData;
import com.skyhouse.projectrpg.data.InventoryData;
import com.skyhouse.projectrpg.data.QuestData;
import com.skyhouse.projectrpg.data.QuestData.ConditionMap;
import com.skyhouse.projectrpg.data.QuickSlotData;
import com.skyhouse.projectrpg.data.StatusData;
import com.skyhouse.projectrpg.utils.ItemDataUtils;

/**
 * Utility database : player section.<br>
 * Collection of function that related to member detail data.
 * @author Meranote
 */
public class PlayerDatabaseUtils extends DatabaseUtils {
	
	/**
	 * Construct a new member utility for database.
	 * @param connection
	 */
	public PlayerDatabaseUtils(Connection connection) {
		super(connection);
	}
	
	/**
	 * Similar to {@link #updateInventory(int, int, int, int)} but will find the empty position first and insert to that position.<br>
	 * if item can stack and not max will insert to that position.
	 * @return 1 for success, -1 for max inventory
	 */
	public int addItemToInventory(int uid, int itemid, int quantity) {
		int state = 0;
		
		try {
			String sql = "";
			PreparedStatement statement = null;
			ResultSet result = null;
			
			boolean isStackable = false;
			
			/**
			 * check is this item can stack
			 */
			if(ItemDataUtils.isStackable(itemid)) {
				int maxstack = ItemDataUtils.getMaxStack(itemid);
				
				/**
				 * Get all item position.
				 */
				sql = "SELECT position, quantity FROM game_inventory WHERE uid = ? AND item_id = ?";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, uid);
				statement.setInt(2, itemid);
				result = statement.executeQuery();
				
				TreeSet<Integer> positionSorted = new TreeSet<Integer>();
				HashMap<Integer, Integer> positionQuantityMapper = new HashMap<Integer, Integer>();
				
				while(result.next()) {
					positionSorted.add(result.getInt("position"));
					positionQuantityMapper.put(result.getInt("position"), result.getInt("quantity"));
				}
				
				/**
				 * Check that got position, if not insert to new position
				 */
				if(!positionSorted.isEmpty()) {
					boolean updateFlag = false;
					for(int p : positionSorted) {
						int currentstack = positionQuantityMapper.get(p);
						if(currentstack + quantity <= maxstack) {
							sql = "UPDATE game_inventory SET quantity = ? WHERE uid = ? AND position = ?";
							statement = connection.prepareStatement(sql);
							statement.setInt(1, currentstack + quantity);
							statement.setInt(2, uid);
							statement.setInt(3, p);
							statement.executeUpdate();
							isStackable = true;
							updateFlag = true;
							state = 1;
							break;
						}
					}
					
					/** Item not added, because all in max stack, insert new one */
					if(!updateFlag) {
						isStackable = false;
					}
				}
			}
			
			if(!isStackable) {
				/**
				 * Find first sort empty position
				 */
				sql = "SELECT position FROM game_inventory WHERE uid = ?";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, uid);
				result = statement.executeQuery();
				
				TreeSet<Integer> positionSorted = new TreeSet<Integer>();
				while(result.next()) {
					positionSorted.add(result.getInt("position"));
				}
				
				int lastposition = 0;
				int gotposition = -1;
				for(int p : positionSorted) {
					if(p - 1 == lastposition) {
						lastposition = p;
					} else {
						gotposition = p;
						break;
					}
				}
				
				if(gotposition != -1) {
					sql = "INSERT INTO game_inventory VALUES(?,?,?,?)";
					statement = connection.prepareStatement(sql);
					statement.setInt(1, uid);
					statement.setInt(2, itemid);
					statement.setInt(3, quantity);
					statement.setInt(4, gotposition);
					statement.executeUpdate();
					state = 1;
				} else {
					state = -1;
				}
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return state;
	}

	/**
	 * Create a new character.
	 * @param uid
	 * @param name Character name to be created
	 * @return 1 for success, -1 for name already used.
	 */
	public int createCharacter(int uid, String name) {
		int state = 0;
		
		try {
			String sql = "SELECT name FROM game_data WHERE name = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			ResultSet result = statement.executeQuery();
			
			result.last();
			
			if(result.getRow() == 0) {
				sql = "INSERT INTO game_data VALUES (?, ?, '', '', 0, 0, NOW())";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, uid);
				statement.setString(2, name);
				Gdx.app.log("DEBUG", new Date(System.currentTimeMillis()).toString());
				statement.executeUpdate();
				
				sql = "INSERT INTO game_status VALUES (?, 0, 1, 0, 15, 15, 15, 0)";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, uid);
				statement.executeUpdate();
				
				sql = "INSERT INTO game_equip VALUES (?, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1)";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, uid);
				statement.executeUpdate();
				
				sql = "INSERT INTO game_quickslot VALUES (?, 1, -1, -1, -1, -1, -1)";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, uid);
				statement.executeUpdate();
				
				sql = "INSERT INTO game_quickslot VALUES (?, 2, -1, -1, -1, -1, -1)";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, uid);
				statement.executeUpdate();
				state = 1;
			} else {
				state = -1;
			}
			
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return state;
	}

	/**
	 * Delete a quest.
	 * @return 1 for success
	 */
	public int deleteQuest(int uid, int questid) {
		int state = 0;
		
		try {
			String sql = "DELETE FROM game_questlog WHERE uid = ? and quest_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, uid);
			statement.setInt(2, questid);
			statement.executeUpdate();
			
			state = 1;
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return state;
	}

	/**
	 * Check username and password. <br>
	 * @return <b>uid</b> or <b>-1</b> if not match to any record
	 */
	public int getLogin(final String username, final String password) {
		int uid = 0;		
		try {
			String sql = "SELECT * FROM member WHERE username = ? AND password = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, password);
			ResultSet result = statement.executeQuery();
			
			result.last();
			
			if(result.getRow() >= 1) {
				uid =  result.getInt("uid");
			} else {
				uid = -1;
			}
			
			result.close();			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return uid;
	}
	
	/**
	 * Get character data by uid.
	 * @return {@link CharacterData} or <b>null</b> if not exist in "game_data" table.
	 */
	public CharacterData getCharacterData(final int uid) {
		CharacterData data = null;
		
		try {
			String sql = "SELECT name, last_x, last_y FROM game_data WHERE uid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, uid);
			ResultSet result = statement.executeQuery();
			
			result.last();
			
			if(result.getRow() >= 1) {
				data = new CharacterData();
				data.name = result.getString("name");
				data.x = result.getInt("last_x");
				data.y = result.getInt("last_y");
				data.equip = getCharacterEquip(uid);
			}
			
			
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	/**
	 * Get character status of uid.
	 * @return {@link StatusData}
	 */
	public StatusData getCharacterStatus(int uid) {
		StatusData data = null;
		
		try {
			String sql = "SELECT * FROM game_status WHERE uid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, uid);
			ResultSet result = statement.executeQuery();
			
			data = new StatusData();
			data.job = result.getInt("job");
			data.level = result.getInt("level");
			data.exp = result.getInt("exp");
			data.str = result.getInt("str");
			data.agi = result.getInt("agi");
			data.intel = result.getInt("intel");
			
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	/**
	 * Get current equip item.
	 * @return {@link CharacterEquipData}
	 */
	public CharacterEquipData getCharacterEquip(int uid) {
		CharacterEquipData data = new CharacterEquipData();
		
		try {
			String sql = "SELECT * FROM game_equip WHERE uid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, uid);
			ResultSet result = statement.executeQuery();
			
			result.last();
			
			if(result.getRow() > 0) {
				data.weapon = result.getInt("weapon");
				data.offhandweapon = result.getInt("offhand_weapon");
				data.helmet = result.getInt("helmet");
				data.armor = result.getInt("armor");
				data.pant = result.getInt("pant");
				data.glove = result.getInt("glove");
				data.boot = result.getInt("boot");
				data.neck = result.getInt("neck");
				data.ear = result.getInt("ear");
				data.ring = result.getInt("ring");
			}
			
			statement.close();
			
			 sql = "SELECT * FROM game_quickslot WHERE uid = ?";
			 statement = connection.prepareStatement(sql);
			 statement.setInt(1, uid);
			 result = statement.executeQuery();
			
			while(result.next()) {
				data.potion = result.getInt("heal");
			}
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	/**
	 * Get inventory of character uid.
	 * @return {@link InventoryData}
	 */
	public InventoryData getInventory(int uid) {
		InventoryData data = new InventoryData();
		
		try {
			String sql = "SELECT * FROM game_inventory WHERE uid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, uid);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				data.itemPosition.put(result.getInt("position"), result.getInt("item_id"));
				data.itemQuantity.put(result.getInt("position"), result.getInt("quantity"));
			}
			
			data.money = getMoney(uid);
			
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	/**
	 * Get money of character uid.
	 */
	public int getMoney(int uid) {
		int money = -1;
		
		try {
			String sql = "SELECT money FROM game_status WHERE uid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, uid);
			ResultSet result = statement.executeQuery();
			
			money = result.getInt("money");
			
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return money;
	}

	/**
	 * Get all quest.
	 * @param uid
	 * @return {@link HashMap}; Key is <b>quest_id</b>, value is {@link QuestData}
	 */
	public HashMap<Integer, QuestData> getAllQuest(int uid) {
		HashMap<Integer, QuestData> collection = new HashMap<Integer, QuestData>();
		
		try {
			String sql = "SELECT * FROM game_questlog WHERE uid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, uid);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				int quest_id = result.getInt("quest_id");
				if(collection.get(quest_id) == null) {
					collection.put(quest_id, new QuestData());
				}
				
				collection.get(quest_id).id = quest_id;
				
				HashMap<Integer, ConditionMap> data = collection.get(quest_id).stepData;
				
				int step = result.getInt("step");
				if(data.get(step) == null) {
					data.put(result.getInt(step), new ConditionMap());
				}
				
				data.get(step).put(result.getInt("condition"), result.getInt("progress_value"));
			}
			
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return collection;
	}

	/**
	 * Get quest.
	 * @return {@link QuestData}
	 */
	public QuestData getQuest(int uid, int quest_id) {
		QuestData data = new QuestData();
		
		try {
			String sql = "SELECT * FROM game_questlog WHERE uid = ? AND quest_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, uid);
			statement.setInt(2, quest_id);
			ResultSet result = statement.executeQuery();
			
			data.id = quest_id;
			
			while(result.next()) {
				HashMap<Integer, ConditionMap> stepcollection = data.stepData;
				
				int step = result.getInt("step");
				if(stepcollection.get(step) == null) {
					stepcollection.put(result.getInt(step), new ConditionMap());
				}
				
				stepcollection.get(step).put(result.getInt("condition"), result.getInt("progress_value"));
			}
			
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	/**
	 * Get quick slot data.
	 * @return Array of {@link QuickSlotData}, size = 2
	 */
	public QuickSlotData[] getQuickSlotData(int uid) {
		QuickSlotData[] data = new QuickSlotData[2];
		
		try {
			String sql = "SELECT * FROM game_quickslot WHERE uid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, uid);
			ResultSet result = statement.executeQuery();
			
			int i;
			while(result.next()) {
				i = result.getInt("tray");
				data[i] = new QuickSlotData();
				data[i].heal = result.getInt("heal");
				data[i].skillA = result.getInt("skill_a");
				data[i].skillB = result.getInt("skill_b");
				data[i].atkA = result.getInt("atk_a");
				data[i].atkB = result.getInt("atk_b");
			}
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	/**
	 * Update str, agi, intel of character.
	 * @return 1 for success
	 */
	public int updateCharacterStatus(int uid, StatusData data) {
		int state = 0;
		
		try {
			String sql = "UPDATE game_status SET str = ?, agi = ?, intel = ? WHERE uid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, data.str);
			statement.setInt(2, data.agi);
			statement.setInt(3, data.intel);
			statement.setInt(4, uid);
			statement.executeUpdate();
			state = 1;
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return state;
	}
	
	/**
	 * Update character job.
	 * @return 1 for success
	 */
	public int updateCharacterJob(int uid, int jobid) {
		int state = 0;
		
		try {
			String sql = "UPDATE game_status SET job = ? WHERE uid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, jobid);
			statement.setInt(2, uid);
			statement.executeUpdate();
			state = 1;
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return state;
	}
	
	/**
	 * Update character exp.
	 * @return
	 */
	public int updateCharacterExp(int uid, int exp) {
		int state = 0;
		
		try {
			String sql = "UPDATE game_status SET exp = ? WHERE uid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, exp);
			statement.setInt(2, uid);
			statement.executeUpdate();
			state = 1;
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return state;
	}
	
	/**
	 * Update character level.
	 * @return
	 */
	public int updateCharacterLevel(int uid, int level) {
		int state = 0;
		
		try {
			String sql = "UPDATE game_status SET level = ? WHERE uid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, level);
			statement.setInt(2, uid);
			statement.executeUpdate();
			state = 1;
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return state;
	}
	
	/**
	 * Update inventory of position.
	 * @param uid
	 * @param position
	 * @param itemid -1 for delete given position
	 * @param quantity
	 * @return 1 for success
	 */
	@SuppressWarnings("resource")
	public int updateInventory(int uid, int position, int itemid, int quantity) {
		int state = 0;
		
		try {
			String sql = "SELECT * FROM game_inventory WHERE uid = ? and position = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, uid);
			statement.setInt(2, position);
			ResultSet result = statement.executeQuery();
			
			result.last();
			
			if(result.getRow() <= 0 && itemid != -1) {
				sql = "INSERT INTO game_inventory VALUES(?, ?, ?, ?)";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, uid);
				statement.setInt(2, itemid);
				statement.setInt(3, quantity);
				statement.setInt(4, position);
				statement.executeUpdate();
			} else if(itemid != -1){
				sql = "UPDATE game_inventory SET item_id = ?, quantity = ?, position = ? WHERE uid = ?";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, itemid);
				statement.setInt(2, quantity);
				statement.setInt(3, position);
				statement.setInt(4, uid);
				statement.executeUpdate();
			} else {
				sql = "DELETE FROM game_inventory WHERE uid = ? AND position = ?";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, uid);
				statement.setInt(2, position);
				statement.executeUpdate();
			}
			
			state = 1;
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return state;
	}
	
	/**
	 * Update character equip.
	 * @return 1 for success
	 */
	public int updateCharacterEquip(int uid, CharacterEquipData data) {
		int state = 0;
		
		try {
			String sql = "UPDATE game_equip SET weapon = ?, offhand_weapon = ?, helmet = ?, armor = ?, pant = ?, glove = ?, boot = ?, neck = ?, ear = ?, ring = ?  WHERE uid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, data.weapon);
			statement.setInt(2, data.offhandweapon);
			statement.setInt(3, data.helmet);
			statement.setInt(4, data.armor);
			statement.setInt(5, data.pant);
			statement.setInt(6, data.glove);
			statement.setInt(7, data.boot);
			statement.setInt(8, data.neck);
			statement.setInt(9, data.ear);
			statement.setInt(10, data.ring);
			statement.setInt(11, uid);
			statement.executeUpdate();
			state = 1;
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return state;
	}
	
	/**
	 * Update quest progress or add it if not have.
	 * @return
	 */
	@SuppressWarnings("resource")
	public int updateQuest(int uid, int questid, int step, int condition, int progressvalue) {
		int state = 0;
		
		try {
			String sql = "SELECT * FROM game_questlog WHERE uid = ? AND quest_id = ? AND step = ? AND condition = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, uid);
			statement.setInt(2, questid);
			statement.setInt(3, step);
			statement.setInt(4, condition);
			ResultSet result = statement.executeQuery();
			
			result.last();
			
			if(result.getRow() <= 0) {
				sql = "INSERT INTO game_questlog VALUES(?,?,?,?,?)";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, uid);
				statement.setInt(2, questid);
				statement.setInt(3, step);
				statement.setInt(4, condition);
				statement.setInt(5, progressvalue);
				statement.executeUpdate();
			} else {
				sql = "UPDATE game_questlog SET progressvalue = ? WHERE uid = ? AND quest_id = ? AND step = ? AND condition = ?";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, progressvalue);
				statement.setInt(2, uid);
				statement.setInt(3, questid);
				statement.setInt(4, step);
				statement.setInt(5, condition);
				statement.executeUpdate();
			}
			
			state = 1;
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return state;
	}
	
	/**
	 * Update quick slot.
	 * @return 1 for success
	 */
	public int updateQuickSlot(int uid, int tray, QuickSlotData data) {
		int state = 0;
		
		try {
			String sql = "UPDATE game_quickslot SET tray = ?, heal = ?, skill_a = ?, skill_b = ?, atk_a = ?, atk_b = ? WHERE uid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, tray);
			statement.setInt(2, data.heal);
			statement.setInt(3, data.skillA);
			statement.setInt(4, data.skillB);
			statement.setInt(5, data.atkA);
			statement.setInt(6, data.atkB);
			statement.setInt(7, uid);
			statement.executeUpdate();
			
			state = 1;
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return state;
	}
}
