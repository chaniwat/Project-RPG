package com.skyhouse.projectrpg.tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.SCMLReader;
import com.skyhouse.projectrpg.spriter.SpriterDrawer;
import com.skyhouse.projectrpg.spriter.SpriterLoader;

public class SpriterTest extends ApplicationAdapter {

	private SpriteBatch batch;
	private ShapeRenderer renderer;
	private SpriterDrawer drawer;
	private Player player;
	private OrthographicCamera camera;
	
	@Override
	public void create() {
		// ������ͧ libgdx
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		
		// ���ҧ��ǨѴ�����ҹ����͹
		FileHandle handle = Gdx.files.internal("entity/Poji/newpoji.scml");
		// ���ҧ�����ҹ��� scml
		SCMLReader reader = new SCMLReader(handle.read());
		// ��ҹ��������� scml
		Data data = reader.getData();
		// ���ҧ������ spriter
		player = new Player(data.getEntity(0));
		player.setPosition(0, 80);
		player.setAnimation("NewAnimation_000");
		// ��Ŵ�����ŷ������ͨҡ�����ҹ��� (�ǡ�Ҿ��ҧ�)
		SpriterLoader loader = new SpriterLoader(data);
		loader.load(handle.file());
		// ���ҧ����Ҵ  spriter ����Ң����žǡ�Ҿ�Ҩҡ loader
		drawer = new SpriterDrawer(loader, batch , renderer);
	}
	
	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.position.set(new Vector2(camera.viewportWidth / 128f, camera.viewportHeight / 8f), 0);
	};
	
	@Override
	public void render() {
		// Clear buffer
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		// �Ѿഷ�����蹡�͹
		player.update();
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
			// �Ҵ�Ҿ��������š�â�Ѻ����ش�ҡ player (�ǡ����˹� - ͧ�Ңͧ�ٻ �繵�)
			drawer.draw(player);
		batch.end();
	}
	
	@Override
	public void dispose() {
		
	}
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new SpriterTest(), config);
	}
	
}
