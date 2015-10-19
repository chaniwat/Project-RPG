package com.skyhouse.projectrpg.net.listeners;

import java.util.Scanner;

public class CommandListener implements Runnable {

	Scanner input;
	String command;
	
	@Override
	public void run() {
			input = new Scanner(System.in);
			while(true) {
				command = input.nextLine();
			}
		}
	
}
