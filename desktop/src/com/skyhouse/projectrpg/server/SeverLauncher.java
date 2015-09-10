package com.skyhouse.projectrpg.server;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;

public class SeverLauncher {
	public static void main (String[] arg) {
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		new HeadlessApplication(new ProjectRPGServer(), config);
	}
}