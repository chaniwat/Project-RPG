package com.skyhouse.projectrpg.data;

import java.util.HashMap;

/**
 * Quest data.
 * @author Meranote
 */
public class QuestData {

	/**
	 * Condition map of step in quest.<br>
	 * Key is <b>condition id</b>, value is <b>value</b>
	 * @author Meranote
	 */
	public static class ConditionMap extends HashMap<Integer, Integer> {
		private static final long serialVersionUID = 684721531511700171L;

		/**
		 * Construct a new {@link ConditionMap}.
		 */
		public ConditionMap() {
			super();
		}
		
	}
	
	public int id;
	/** Key is <b>step</b>, value is <b>collection of condition and value data</b>. */
	public HashMap<Integer, ConditionMap> stepData;
	
	
	
}
