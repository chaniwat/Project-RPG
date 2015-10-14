package com.skyhouse.projectrpg.test;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.skyhouse.projectrpg.client.ProjectRPGClient;
import com.skyhouse.projectrpg.server.ProjectRPGServer;

public class TestLauncher {
	public static void main (String[] arg) {
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		new HeadlessApplication(new ProjectRPGServer(), config);
		
		LwjglApplicationConfiguration config1 = new LwjglApplicationConfiguration();
		config1.useGL30 = true;
		config1.width = 1280;
		config1.height = 720;
		new LwjglApplication(new ProjectRPGClient(), config1);
	}
}
