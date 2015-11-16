package com.skyhouse.projectrpg.utils.math;

public class StatusCalcs {

	public static int expToLevel(int exp) {
		Integer level=1;
		Integer divide_exp= 50;
		while(exp>divide_exp){
			exp -= divide_exp;
			level++;
			divide_exp*=2;
		}
		return level;
	}
	
	public static int levelToBaseExp(int level) {
		int currentLv = 1;
		int exp = 0;
		int divide_exp= 50;
		while(currentLv < level){
			exp += divide_exp;
			divide_exp *= 2;
			currentLv++;
		}
		return exp;
	}
	
	public static void main(String[] args) {
		System.out.println(levelToBaseExp(30));
//		System.out.println(expToLevel(104000));
	}

}
