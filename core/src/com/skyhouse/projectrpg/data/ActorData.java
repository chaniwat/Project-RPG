package com.skyhouse.projectrpg.data;

public class ActorData extends Data {

	/**
	 * Action state of character and monster.
	 * @author Meranote
	 */
	public static enum ActionState {
		IDLE, WALK, DASH, JUMP, FALL, ATTACK1, ATTACK2, SKILL1, SKILL2, DEATH, CLUMB
	}
	
}
