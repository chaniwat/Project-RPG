package com.skyhouse.projectrpg.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.utils.color.HSL;
import com.skyhouse.projectrpg.utils.font.ThaiLanguage;

/**
 * Start scene. <br>
 * <b>First scene when application start.</b>
 * @author Meranote
 */
public class StartScene extends Scene {
	
	private static final float MINLOADINGTIME = 1f;
	private static final float MINOVERLAYTIME = 2f;
	private static final float HUEFROM = 0f;
	private static final float HUETO = 180f;
	
	private AssetManager assetmanager = ProjectRPG.client.assetmanager;
	private float accumulatorLoadingTime, accumulatorOverlayTime;
	
	private float screenwidth, screenheight;
	private float currentProgress;
	private Sprite background;
	private HSL progressColor;
	
	/**
	 * Construct a new start scene.
	 */
	public StartScene() {
		addViewport(new ScreenViewport());
		
		currentProgress = 0f;
		progressColor = new HSL(Color.RED);
		
		accumulatorLoadingTime = MINLOADINGTIME;
		accumulatorOverlayTime = MINOVERLAYTIME;
		
		background = new Sprite(assetmanager.get("texture/background/cinematicstartbackground.png", Texture.class));
		background.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	@Override
	public void change() {
		queueLoadAsset();
	}
	
	private void queueLoadAsset() {
		FreeTypeFontLoaderParameter fontparams1 = new FreeTypeFontLoaderParameter();
		fontparams1.fontFileName = "font/Roboto-Regular-16.ttf";
		fontparams1.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS + ThaiLanguage.THAI_CHARS;
		assetmanager.load("font/Roboto-Regular-16.ttf", BitmapFont.class, fontparams1);
		
		FreeTypeFontLoaderParameter fontparams2 = new FreeTypeFontLoaderParameter();
		fontparams2.fontFileName = "font/Roboto-Regular-14.ttf";
		fontparams2.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS + ThaiLanguage.THAI_CHARS;
		assetmanager.load("font/Roboto-Regular-14.ttf", BitmapFont.class, fontparams2);
		
		FreeTypeFontLoaderParameter fontparams3 = new FreeTypeFontLoaderParameter();
		fontparams3.fontFileName = "font/Roboto-Bold-16.ttf";
		fontparams3.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS + ThaiLanguage.THAI_CHARS;
		assetmanager.load("font/Roboto-Bold-16.ttf", BitmapFont.class, fontparams3);
		
		FreeTypeFontLoaderParameter fontparams4 = new FreeTypeFontLoaderParameter();
		fontparams4.fontFileName = "font/Roboto-Bold-14.ttf";
		fontparams4.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS + ThaiLanguage.THAI_CHARS;
		assetmanager.load("font/Roboto-Bold-14.ttf", BitmapFont.class, fontparams4);
		
		assetmanager.load("uiskin.atlas", TextureAtlas.class);
		
		// TODO Load artwork for loading screen
		assetmanager.load("texture/artwork/artwork_1.png", Texture.class);
	}

	@Override
	public void update(float deltatime) {
		screenwidth = getViewport(ScreenViewport.class).getWorldWidth();
		screenheight = getViewport(ScreenViewport.class).getWorldHeight();
		
		updateBackground();
		
		currentProgress = assetmanager.getProgress() - (accumulatorLoadingTime / MINLOADINGTIME);
		if(currentProgress >= 1f) {
			updateAsset();
			
			ProjectRPG.client.scenemanager.addScene(HomeScene.class);
			ProjectRPG.client.scenemanager.addScene(LoadingScene.class);
			ProjectRPG.client.scenemanager.addScene(GameScene.class);
			ProjectRPG.client.scenemanager.addScene(MenuScene.class);
			ProjectRPG.client.scenemanager.setUseScene(HomeScene.class);
		} else if(currentProgress < 0f) {
			currentProgress = 1f - accumulatorLoadingTime / MINLOADINGTIME;
		}
		
		progressColor.h = (HUEFROM / 360f ) + ((HUETO / 360f) * currentProgress);
		
		if(accumulatorLoadingTime > 0f) {
			accumulatorLoadingTime -= deltatime;			
		} else accumulatorLoadingTime = 0f;
		
		if(accumulatorOverlayTime > 0f) {
			accumulatorOverlayTime -= deltatime;
		} else accumulatorOverlayTime = 0f;
	}
	
	private void updateBackground() {
		background.setSize(screenheight * ((float)background.getTexture().getWidth() / (float)background.getTexture().getHeight()), screenheight);
		if(background.getWidth() < screenwidth) {
			background.setSize(screenwidth, screenwidth * ((float)background.getTexture().getHeight() / (float)background.getTexture().getWidth()));
		}
		background.setPosition(getViewport(ScreenViewport.class).getCamera().position.x - (background.getWidth() / 2f),  0);
	}

	private void updateAsset() {
		ProjectRPG.client.graphic.font.layout = new GlyphLayout();
		
		ProjectRPG.client.graphic.font.regular = assetmanager.get("font/Roboto-Regular-16.ttf", BitmapFont.class);
		ProjectRPG.client.graphic.font.regular.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ProjectRPG.client.graphic.font.regular.getData().markupEnabled = true;
		ProjectRPG.client.graphic.font.smallregular = assetmanager.get("font/Roboto-Regular-14.ttf", BitmapFont.class);
		ProjectRPG.client.graphic.font.smallregular.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ProjectRPG.client.graphic.font.smallregular.getData().markupEnabled = true;
		ProjectRPG.client.graphic.font.bold = assetmanager.get("font/Roboto-Bold-16.ttf", BitmapFont.class);
		ProjectRPG.client.graphic.font.bold.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ProjectRPG.client.graphic.font.bold.getData().markupEnabled = true;
		ProjectRPG.client.graphic.font.smallbold = assetmanager.get("font/Roboto-Bold-14.ttf", BitmapFont.class);
		ProjectRPG.client.graphic.font.smallbold.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ProjectRPG.client.graphic.font.smallbold.getData().markupEnabled = true;
		
		Skin skin = new Skin();
		skin.addRegions(assetmanager.get("uiskin.atlas", TextureAtlas.class));
		skin.add("default-font", ProjectRPG.client.graphic.font.regular);
		skin.add("small-font", ProjectRPG.client.graphic.font.smallregular);
		skin.load(Gdx.files.internal("uiskin.json"));
		VisUI.load(skin);
	}
	
	@Override
	public void draw(float deltatime) {
		useViewport(ScreenViewport.class);
		
		batch.begin();
			background.draw(batch);
		batch.end();
		
		ProjectRPG.client.graphic.enableGLAlphaBlend();
		renderer.begin(ShapeType.Filled);
			// Loading progress
			renderer.setColor(progressColor.toRGB());
			renderer.rect(0, 0, screenwidth * currentProgress, screenheight * 0.011f);
			// Overlay
			if((accumulatorOverlayTime / MINOVERLAYTIME) > 0f) {
				renderer.setColor(0, 0, 0, accumulatorOverlayTime / MINOVERLAYTIME);
				renderer.rect(0, 0, screenwidth, screenheight);
			}
		renderer.end();
		ProjectRPG.client.graphic.disableGLAlphaBlend();
	}

	@Override
	public void dispose() {
		assetmanager.unload("texture/background/cinematicstartbackground.png");
	}

}
