package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;

import renderer.PixelRenderer;
import renderer.RendererListener;
import renderer.renderable.PixelStructure;
import renderer.util.Loader;

public class Test implements RendererListener{
	
	private PixelRenderer renderer;
	
	private PixelStructure car;
	private PixelStructure fpsCount;
	private Font font = new Font("Courier", Font.PLAIN, 8);
	
	public Test() {
		renderer = new PixelRenderer("Pixel Renderer", 1280, 720, 3, this);
		renderer.init();
	}
	
	@Override
	public void onStart() {
		car = Loader.loadTexture("res/Textures/car.png", renderer);
		car.translate(renderer.getPixelWidth()/2-car.getWidth()/2, renderer.getPixelHeight()/2-car.getHeight()/2);
		Rectangle bounds = font.getStringBounds("0000", new FontRenderContext(null, true, true)).getBounds();
		fpsCount = Loader.loadEmpty((int)bounds.getWidth(), (int)bounds.getHeight(), renderer);
		fpsCount.translate(renderer.getPixelWidth()-fpsCount.getWidth()-5, 1);
		Graphics graphics = fpsCount.getGraphics();
		graphics.setColor(Color.white);
		graphics.setFont(font);
	}

	@Override
	public void onRender() {
		car.rotate(0.003f, car.getWidth()/2, car.getHeight()/2);
		renderer.render(car);
		fpsCount.resetGraphics();
		Graphics graphics = fpsCount.getGraphics();
		graphics.setColor(Color.white);
		graphics.setFont(font);
		graphics.drawString(String.valueOf(renderer.getFPS()), 0, 6);
		renderer.render(fpsCount);
	}

	@Override
	public void onExit() {
		System.exit(0);
	}
	
	public static void main(String[] args) {
		new Test();
	}
}
