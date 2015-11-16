package com.skyhouse.projectrpg.scene;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.net.packets.ClientReadyRequest;
import com.skyhouse.projectrpg.utils.color.HSL;

/**
 * Loading scene. <br>
 * <b>Appear when change scene or change map and while assetmanager do some magic with the assets.</b>
 * @author Meranote
 */
public class LoadingScene extends Scene {

	private static final float MINLOADINGTIME = 1;
	private static final float OVERLAYTIME = 0.4f;
	private static final float HUEFROM = 0f;
	private static final float HUETO = 180f;
	
	private float accumulatorLoadingTime, accumulatorOverlayTime;
	
	private float screenwidth, screenheight;
	private float currentProgress;
	private ArrayList<Sprite> backgroundList;
	private Sprite background;
	private HSL progressColor;
	
	private ArrayList<String> messageLists;
	
	private BitmapFont font = ProjectRPG.client.graphic.font.uifont;
	private GlyphLayout layout = ProjectRPG.client.graphic.font.layout;
	
	/**
	 * Construct a new loading scene.
	 */
	public LoadingScene() {
		addViewport(new GameplayViewport(16f));
		addViewport(new ScreenViewport());
		progressColor = new HSL(Color.RED);
		
		// TODO Prepare background artwork
		backgroundList = new ArrayList<Sprite>();
		backgroundList.add(new Sprite(ProjectRPG.client.assetmanager.get("texture/artwork/artwork_1.png", Texture.class)));
		
		background = backgroundList.get(0);
		
		// TODO Prepare message message
		messageLists = new ArrayList<String>(Arrays.asList(Gdx.files.internal("data/loadingmessage.txt").readString().split(System.getProperty("line.separator"))));
	}
	
	@Override
	public void change() {
		ProjectRPG.client.inputmanager.setInputProcessor(null);
		ProjectRPG.client.inputmanager.setControllerProcessor(null);
		
		accumulatorOverlayTime = 0f;
		accumulatorLoadingTime = MINLOADINGTIME;
	}

	@Override
	public void update(float deltatime) {
		screenwidth = getViewport(ScreenViewport.class).getWorldWidth();
		screenheight = getViewport(ScreenViewport.class).getWorldHeight();
		
		updateBackground();
		
		currentProgress = ProjectRPG.client.assetmanager.getProgress() - (accumulatorLoadingTime / MINLOADINGTIME);
		if(currentProgress >= 1f && ProjectRPG.client.gamemanager.isGameReady()) {
			if(accumulatorOverlayTime < OVERLAYTIME) {
				accumulatorOverlayTime += deltatime;
			} else {
				ProjectRPG.client.scenemanager.setUseScene(GameScene.class);
				ProjectRPG.client.network.net.sendTCP(new ClientReadyRequest());
			}
		} else if(currentProgress < 0f) {
			currentProgress = 1f - accumulatorLoadingTime / MINLOADINGTIME;
		}
		
		progressColor.h = (HUEFROM / 360f ) + ((HUETO / 360f) * currentProgress);
		
		if(accumulatorLoadingTime > 0f) {
			accumulatorLoadingTime -= deltatime;			
		} else accumulatorLoadingTime = 0f;
	}

	@Override
	public void draw(float deltatime) {
		useViewport(ScreenViewport.class);
		
		batch.begin();
			background.draw(batch);
		batch.end();
	
		// Calculate position and size
		Vector2 position = new Vector2(screenwidth * 0.05f, screenheight * 0.07f);
		Rectangle rect = new Rectangle(position.x, position.y, screenwidth - (position.x * 2f), layout.height + 50f);

		ProjectRPG.client.graphic.enableGLAlphaBlend();
		renderer.begin(ShapeType.Filled);
			// Message & box
			renderer.setColor(0, 0, 0, 0.75f);
			layout.setText(font, "[WHITE]" + messageLists.get(0) + " " + ProjectRPG.client.mapmanager.isMapReady());
			// Draw box
			renderer.rect(rect.x, rect.y, rect.width, rect.height);
		renderer.end();
		ProjectRPG.client.graphic.disableGLAlphaBlend();
		
		batch.begin();
			// Draw message
			font.draw(batch, layout, (screenwidth / 2f) - (layout.width / 2f), (rect.y + 25f) + layout.height);
		batch.end();
		
		ProjectRPG.client.graphic.enableGLAlphaBlend();
		renderer.begin(ShapeType.Filled);
			// Loading progress bar
			renderer.setColor(progressColor.toRGB());
			renderer.rect(0, 0, screenwidth * currentProgress, screenheight * 0.011f);
			// Overlay
			if((accumulatorOverlayTime / OVERLAYTIME) > 0f) {
				renderer.setColor(0, 0, 0, accumulatorOverlayTime / OVERLAYTIME);
				renderer.rect(0, 0, screenwidth, screenheight);
			}
		renderer.end();
		ProjectRPG.client.graphic.disableGLAlphaBlend();
	}
	
	private void updateBackground() {
		background.setSize(screenheight * ((float)background.getTexture().getWidth() / (float)background.getTexture().getHeight()), screenheight);
		if(background.getWidth() < screenwidth) {
			background.setSize(screenwidth, screenwidth * ((float)background.getTexture().getHeight() / (float)background.getTexture().getWidth()));
		}
		background.setPosition(getViewport(ScreenViewport.class).getCamera().position.x - (background.getWidth() / 2f),  0);
	}

	@Override
	public void dispose() {
		
	}

}
