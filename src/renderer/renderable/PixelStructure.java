package renderer.renderable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class PixelStructure {
	private BufferedImage texture;
	private int width, height;
	private AffineTransform transform;
	
	public PixelStructure(BufferedImage texture){
		this.texture = texture;
		width = texture.getWidth();
		height = texture.getHeight();
		transform = new AffineTransform();
	}
	
	public void setPixel(int x, int y, Color color) {
		texture.setRGB(x, y, color.getRGB());
	}
	
	public Color getPixel(int x, int y) {
		return new Color(texture.getRGB(x, y));
	}
	
	public void resetGraphics() {
		texture.getGraphics().clearRect(0, 0, width, height);
	}
	
	public Graphics getGraphics() {
		return texture.getGraphics();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void translate(int x, int y) {
		transform.translate(x, y);
	}
	
	public void rotate(float angle, float x, float y){
		transform.rotate(angle, x, y);
	}
	
	public void scale(float x, float y) {
		transform.scale(x, y);
	}
	
	public AffineTransform getTransform() {
		return transform;
	}
	
	public BufferedImage getTexture() {
		return texture;
	}
}
