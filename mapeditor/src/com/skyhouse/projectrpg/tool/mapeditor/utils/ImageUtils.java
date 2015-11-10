package com.skyhouse.projectrpg.tool.mapeditor.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageUtils {

	private ImageUtils() { }
	
	public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) throws IOException {  
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR_PRE);  
        Graphics2D g = resizedImage.createGraphics();  
        g.drawImage(originalImage, 0, 0, width, height, null);  
        g.dispose();  
        return resizedImage;  
    }
	
}
