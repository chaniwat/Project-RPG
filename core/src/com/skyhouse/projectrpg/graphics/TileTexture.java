package com.skyhouse.projectrpg.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class TileTexture {
	
	public enum TileTexturePosition {
		TopLeft(0,0,1),
		TopMiddle(1,1,1),
		TopRight(2,2,1),
		MiddleLeft(3,3,1),
		MiddleMiddle(4,4,1),
		MiddleRight(5,5,1),
		BottomLeft(6,6,1),
		BottomMiddle(7,7,1),
		BottomRight(8,8,1),
		Top(0,3,1),
		MiddleHorizontal(3,6,1),
		Bottom(6,9,1),
		Left(0,9,3),
		MiddleVertical(1,10,3),
		Right(2,11,3),
		ALL(0,9,1);
		
		private final int[] data;
		private TileTexturePosition(int start, int end, int step) {
			this.data = new int[]{start, end, step};
		}
		
		public int[] getData() {
			return data;
		}
		
		public int getIndex() {
			return data[0];
		}
	}
	
	TextureRegion[] region;
	
	/*
	 * Position
	 * 0 - top-left
	 * 1 - top-mid
	 * 2 - top-right
	 * 3 - mid-left
	 * 4 - mid-mid
	 * 5 - mid-right
	 * 6 - bottom-left
	 * 7 - bottom-mid
	 * 8 - bottom-right
	 * 
	 * Draw priority size
	 * 1x1 - 0
	 * 3x1 - 0 1 2
	 * 1x2 - 0 6
	 * 2x2 - 0 2 6 8
	 * 1x3 - 0 3 6
	 * 2x3 - 0 2 3 5 6 8
	 * 3x3 - ALL
	 */
	
	public TileTexture() {
		this.region = new TextureRegion[9];
	}
	
	public TileTexture(TextureRegion region) {
		this.region = new TextureRegion[9];
		setRegionPosition(region, TileTexturePosition.ALL);
	}
	
	public void setRegionPosition(TextureRegion region, TileTexturePosition position) {
		int[] positionData = position.getData();
		for(int i = positionData[0]; i < positionData[1]; i += positionData[2]) {
			this.region[i] = region;
		}
	}
	
	public void setRegionPosition(TextureRegion region, TileTexturePosition... position) {
		for(TileTexturePosition pos : position) {
			setRegionPosition(region, pos);
		}
	}
	
 	public TextureRegion getRegionPosition(TileTexturePosition position) {
 		return region[position.getIndex()];
 	}
	
	public Texture getTexturePosition(TileTexturePosition position) {
		return region[position.getIndex()].getTexture();
	}
	
	public void draw(SpriteBatch batch, Vector2 position, Vector2 size) {
		batch.draw(region[TileTexturePosition.TopLeft.getIndex()], position.x, position.y - 1, 1, 1);
		
		if(size.x > 2) {
			for(int col = 1; col < size.x - 1; col++) {
				batch.draw(region[TileTexturePosition.TopMiddle.getIndex()], position.x + col, position.y - 1, 1, 1);
			}
			batch.draw(region[TileTexturePosition.TopRight.getIndex()], position.x + (size.x - 1), position.y - 1, 1, 1);
		} else {
			batch.draw(region[TileTexturePosition.TopRight.getIndex()], position.x + (size.x - 1), position.y - 1, 1, 1);
		}
		
		if(size.y > 2) {
			for(int row = 1; row < size.y - 1; row++) {
				batch.draw(region[TileTexturePosition.MiddleLeft.getIndex()], position.x, position.y - 1 - row, 1, 1);
				if(size.x > 2) {
					for(int col = 1; col < size.x - 1; col++) {
						batch.draw(region[TileTexturePosition.MiddleMiddle.getIndex()], position.x + col, position.y - 1 - row, 1, 1);
					}
					batch.draw(region[TileTexturePosition.MiddleRight.getIndex()], position.x + (size.x - 1), position.y - 1 - row, 1, 1);
				} else {
					batch.draw(region[TileTexturePosition.MiddleRight.getIndex()], position.x + (size.x - 1), position.y - 1 - row, 1, 1);
				}
			}
			batch.draw(region[TileTexturePosition.BottomLeft.getIndex()], position.x, position.y - 1 - (size.y - 1), 1, 1);
			if(size.x > 2) {
				for(int col = 1; col < size.x - 1; col++) {
					batch.draw(region[TileTexturePosition.BottomMiddle.getIndex()], position.x + col, position.y - 1 - (size.y - 1), 1, 1);
				}
				batch.draw(region[TileTexturePosition.BottomRight.getIndex()], position.x + (size.x - 1), position.y - 1 - (size.y - 1), 1, 1);
			} else {
				batch.draw(region[TileTexturePosition.BottomRight.getIndex()], position.x + (size.x - 1), position.y - 1 - (size.y - 1), 1, 1);
			}
		} else {
			batch.draw(region[TileTexturePosition.BottomLeft.getIndex()], position.x, position.y - 1 - (size.y - 1), 1, 1);
			if(size.x > 2) {
				for(int col = 1; col < size.x - 1; col++) {
					batch.draw(region[TileTexturePosition.BottomMiddle.getIndex()], position.x + col, position.y - 1 - (size.y - 1), 1, 1);
				}
				batch.draw(region[TileTexturePosition.BottomRight.getIndex()], position.x + (size.x - 1), position.y - 1 - (size.y - 1), 1, 1);
			} else {
				batch.draw(region[TileTexturePosition.BottomRight.getIndex()], position.x + (size.x - 1), position.y - 1 - (size.y - 1), 1, 1);
			}
		}	
	}

}
