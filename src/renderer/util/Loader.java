package renderer.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import renderer.PixelRenderer;
import renderer.renderable.PixelStructure;

public class Loader {
	public static PixelStructure loadTexture(String path, PixelRenderer renderer) {
		try {
			BufferedImage raw = ImageIO.read(new File(path));
			BufferedImage complete = renderer.getCanvas().getGraphicsConfiguration().createCompatibleImage(raw.getWidth(), raw.getHeight(), raw.getTransparency());
			complete.getGraphics().drawImage(raw, 0, 0, raw.getWidth(), raw.getHeight(), null);
			return new PixelStructure(complete);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedImage loadImage(String path, PixelRenderer renderer) {
		try {
			BufferedImage raw = ImageIO.read(new File(path));
			BufferedImage complete = renderer.getCanvas().getGraphicsConfiguration().createCompatibleImage(raw.getWidth(), raw.getHeight(), raw.getTransparency());
			complete.getGraphics().drawImage(raw, 0, 0, raw.getWidth(), raw.getHeight(), null);
			return complete;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static PixelStructure loadEmpty(int width, int height, PixelRenderer renderer) {
		BufferedImage complete = renderer.getCanvas().getGraphicsConfiguration().createCompatibleImage(width, height, BufferedImage.BITMASK);
		return new PixelStructure(complete);
	}
	
	public static PixelStructure loadEmpty(int width, int height, boolean transparent, PixelRenderer renderer) {
		BufferedImage complete = null;
		if(transparent)
			complete = renderer.getCanvas().getGraphicsConfiguration().createCompatibleImage(width, height, BufferedImage.TRANSLUCENT);
		else 
			complete = renderer.getCanvas().getGraphicsConfiguration().createCompatibleImage(width, height, BufferedImage.BITMASK);
		return new PixelStructure(complete);
	}
}
