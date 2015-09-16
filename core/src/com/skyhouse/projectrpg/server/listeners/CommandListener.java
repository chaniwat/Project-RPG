package com.skyhouse.projectrpg.server.listeners;

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
