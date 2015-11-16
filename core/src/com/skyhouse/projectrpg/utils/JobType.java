package com.skyhouse.projectrpg.utils;

public enum JobType {

	TRAVELER("นักเดินทาง"),
	WARRIOR("นักดาบ"),
	ARCHER("นักยิงธนู"),
	MAGICIAN("นักเวทย์");
	
	private String thainame;
	
	private JobType(String thainame) {
		this.thainame = thainame;
	}
	
	public String getThaiName() {
		return thainame;
	}
	
	public static JobType getValue(int index) {
		switch (index) {
			case 1:
				return WARRIOR;
			case 2:
				return ARCHER;
			case 3:
				return MAGICIAN;
			default:
				return TRAVELER;
		}
	}
	
}
