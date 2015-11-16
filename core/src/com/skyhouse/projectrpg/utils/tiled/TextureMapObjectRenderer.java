package com.skyhouse.projectrpg.utils.tiled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TextureMapObjectRenderer extends OrthogonalTiledMapRenderer {

	public TextureMapObjectRenderer(TiledMap map) {
        super(map);
    }

    public TextureMapObjectRenderer(TiledMap map, Batch batch) {
        super(map, batch);
    }

    public TextureMapObjectRenderer(TiledMap map, float unitScale) {
        super(map, unitScale);
    }

    public TextureMapObjectRenderer(TiledMap map, float unitScale, Batch batch) {
        super(map, unitScale, batch);
    }

    @Override
    public void renderObject(MapObject object) {
        if (object instanceof TextureMapObject) {
            TextureMapObject textureObject = (TextureMapObject) object;
            batch.draw(
                    textureObject.getTextureRegion().getTexture(),
                    textureObject.getX() * (1f / 70f),
                    (textureObject.getY() + (Float)textureObject.getProperties().get("height")) * (1f / 70f),
                    (Float)textureObject.getProperties().get("width") * (1f / 70f),
                    (Float)textureObject.getProperties().get("height") * (1f / 70f)
            );
        }
    }

}
