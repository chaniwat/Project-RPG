package com.skyhouse.projectrpg.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.factory.Factory;
import com.skyhouse.projectrpg.utils.color.HSL;
import com.skyhouse.projectrpg.utils.font.ThaiLanguage;

/**
 * Start scene. <br>
 * <b>First scene when application start.</b>
 * @author Meranote
 */
public class StartScene extends Scene {
	
	private static final float MINLOADINGTIME = 1f;
	private static final float MINOVERLAYTIME = 0.5f;
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
		
		background = new Sprite(assetmanager.get("texture/artwork/startbackground.png", Texture.class));
		background.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	@Override
	public void change() {
		queueLoadAsset();
	}
	
	private void queueLoadAsset() {
		// File resolver
		InternalFileHandleResolver resolver = new InternalFileHandleResolver();
		
		// Freetype font (UI, Normal render)
		assetmanager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetmanager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		queueFreetypeFont();
		
		// Distance field font (Sprite)
		assetmanager.setLoader(BitmapFont.class, ".fnt", new BitmapFontLoader(resolver));
		// All Bitmap Font settings (load with BitmapFontLoader, .fnt)
		BitmapFontParameter bitmapFontParams = new BitmapFontParameter();
		bitmapFontParams.genMipMaps = true;
		bitmapFontParams.minFilter = TextureFilter.MipMapLinearNearest;
		bitmapFontParams.magFilter = TextureFilter.Linear;
		// Load Font
		assetmanager.load("font/roboto-regular.fnt", BitmapFont.class, bitmapFontParams);
		assetmanager.load("font/roboto-bold.fnt", BitmapFont.class, bitmapFontParams);
		
		// Load ui skin
		assetmanager.load("texture/ui/skin/uiskin.atlas", TextureAtlas.class);
		
		// Texture settings
		TextureParameter textureParams = new TextureParameter();
		textureParams.genMipMaps = true;
		textureParams.minFilter = TextureFilter.MipMapLinearLinear;
		textureParams.magFilter = TextureFilter.Linear;
		// Load Item Slot
		assetmanager.load("texture/ui/menuscene/itemslot/itemslot.png", Texture.class, textureParams);
		assetmanager.load("texture/ui/menuscene/itemslot/mockitemicon.png", Texture.class, textureParams);
		// Load artwork
		assetmanager.load("texture/artwork/artwork_1.png", Texture.class, textureParams);
		// Load MenuScene Assets
		assetmanager.load("texture/ui/menuscene/cursor.png", Texture.class, textureParams);
		assetmanager.load("texture/ui/menuscene/bg.png", Texture.class, textureParams);
		assetmanager.load("texture/ui/menuscene/bar/timebar/moon.png", Texture.class, textureParams);
		assetmanager.load("texture/ui/menuscene/bar/timebar/sun.png", Texture.class, textureParams);
		assetmanager.load("texture/ui/menuscene/bar/tabbar/arrow.png", Texture.class, textureParams);
		assetmanager.load("texture/ui/menuscene/bar/tabbar/character.png", Texture.class, textureParams);
		assetmanager.load("texture/ui/menuscene/bar/tabbar/group.png", Texture.class, textureParams);
		assetmanager.load("texture/ui/menuscene/bar/tabbar/settings.png", Texture.class, textureParams);
		assetmanager.load("texture/ui/menuscene/bar/tabbar/skill.png", Texture.class, textureParams);
		// Character Tab
		assetmanager.load("texture/ui/menuscene/charactertab/arrow.png", Texture.class, textureParams);
		assetmanager.load("texture/ui/menuscene/charactertab/arrow_disable.png", Texture.class, textureParams);
		
		// Factory
		Factory.prepareAsset();
	}

	private void queueFreetypeFont() {
		FreeTypeFontLoaderParameter fontparams1 = new FreeTypeFontLoaderParameter();
		fontparams1.fontFileName = "font/Roboto-Regular-16.ttf";
		fontparams1.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS + ThaiLanguage.THAI_CHARS;
		fontparams1.fontParameters.genMipMaps = true;
		fontparams1.fontParameters.minFilter = TextureFilter.MipMapLinearNearest;
		fontparams1.fontParameters.magFilter = TextureFilter.Linear;
		assetmanager.load("font/Roboto-Regular-16.ttf", BitmapFont.class, fontparams1);
		
		FreeTypeFontLoaderParameter fontparams2 = new FreeTypeFontLoaderParameter();
		fontparams2.fontFileName = "font/Roboto-Regular-14.ttf";
		fontparams2.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS + ThaiLanguage.THAI_CHARS;
		fontparams2.fontParameters.size = 14;
		fontparams2.fontParameters.genMipMaps = true;
		fontparams2.fontParameters.minFilter = TextureFilter.MipMapLinearNearest;
		fontparams2.fontParameters.magFilter = TextureFilter.Linear;
		assetmanager.load("font/Roboto-Regular-14.ttf", BitmapFont.class, fontparams2);
		
		FreeTypeFontLoaderParameter fontparams3 = new FreeTypeFontLoaderParameter();
		fontparams3.fontFileName = "font/Roboto-Bold-16.ttf";
		fontparams3.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS + ThaiLanguage.THAI_CHARS;
		fontparams3.fontParameters.genMipMaps = true;
		fontparams3.fontParameters.minFilter = TextureFilter.MipMapLinearNearest;
		fontparams3.fontParameters.magFilter = TextureFilter.Linear;
		assetmanager.load("font/Roboto-Bold-16.ttf", BitmapFont.class, fontparams3);
		
		FreeTypeFontLoaderParameter fontparams4 = new FreeTypeFontLoaderParameter();
		fontparams4.fontFileName = "font/Roboto-Bold-14.ttf";
		fontparams4.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS + ThaiLanguage.THAI_CHARS;
		fontparams4.fontParameters.size = 14;
		fontparams4.fontParameters.genMipMaps = true;
		fontparams4.fontParameters.minFilter = TextureFilter.MipMapLinearNearest;
		fontparams4.fontParameters.magFilter = TextureFilter.Linear;
		assetmanager.load("font/Roboto-Bold-14.ttf", BitmapFont.class, fontparams4);
	}
	
	@Override
	public void update(float deltatime) {
		screenwidth = getViewport(ScreenViewport.class).getWorldWidth();
		screenheight = getViewport(ScreenViewport.class).getWorldHeight();
		
		updateBackground();
		
		currentProgress = assetmanager.getProgress() - (accumulatorLoadingTime / MINLOADINGTIME);
		if(currentProgress >= 1f) {
			updateAsset();
			// Factory
			Factory.register();
			// Add scene
			ProjectRPG.client.scenemanager.addScene(HomeScene.class);
			ProjectRPG.client.scenemanager.addScene(LoadingScene.class);
			ProjectRPG.client.scenemanager.addScene(GameScene.class);
			ProjectRPG.client.scenemanager.addScene(MenuScene.class);
			// Change scene
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
		// Prepare font
		ProjectRPG.client.graphic.font.layout = new GlyphLayout();
		ProjectRPG.client.graphic.font.uifont = assetmanager.get("font/Roboto-Regular-16.ttf", BitmapFont.class);
		ProjectRPG.client.graphic.font.uifont.getData().markupEnabled = true;
		ProjectRPG.client.graphic.font.uifontsmall = assetmanager.get("font/Roboto-Regular-14.ttf", BitmapFont.class);
		ProjectRPG.client.graphic.font.uifontsmall.getData().markupEnabled = true;
		ProjectRPG.client.graphic.font.uifontbold = assetmanager.get("font/Roboto-Bold-16.ttf", BitmapFont.class);
		ProjectRPG.client.graphic.font.uifontbold.getData().markupEnabled = true;
		ProjectRPG.client.graphic.font.uifontsmallbold = assetmanager.get("font/Roboto-Bold-14.ttf", BitmapFont.class);
		ProjectRPG.client.graphic.font.uifontsmallbold.getData().markupEnabled = true;
		ProjectRPG.client.graphic.font.spritefont = assetmanager.get("font/roboto-regular.fnt", BitmapFont.class);
		ProjectRPG.client.graphic.font.spritefont.getData().markupEnabled = true;
		ProjectRPG.client.graphic.font.spritefontbold = assetmanager.get("font/roboto-bold.fnt", BitmapFont.class);
		ProjectRPG.client.graphic.font.spritefontbold.getData().markupEnabled = true;
		
		// Prepare ui skin
		Skin skin = new Skin();
		skin.addRegions(assetmanager.get("texture/ui/skin/uiskin.atlas", TextureAtlas.class));
		skin.add("default-font", ProjectRPG.client.graphic.font.uifont);
		skin.add("small-font", ProjectRPG.client.graphic.font.uifontsmall);
		skin.load(Gdx.files.internal("texture/ui/skin/uiskin.json"));
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
		assetmanager.unload("texture/artwork/startbackground.png");
	}

}
