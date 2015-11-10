package com.skyhouse.projectrpg.utils;

public class ItemDataUtils {
	
	public static boolean isStackable(int itemid) {
		// TODO check that itemid can stack, if maxstack is only 1 will can't stack.
		return false;
	}
	
	public static int getMaxStack(int itemid) {
		// TODO get max stack of item
		return 1;
	}
	
}
