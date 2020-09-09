package test;

import renderer.PixelRenderer;
import renderer.RendererListener;

public class Test implements RendererListener{
	
	public Test() {
		PixelRenderer renderer = new PixelRenderer("Pixel Renderer", 800, 600, 5, this);
		renderer.init();
	}
	
	@Override
	public void onStart() {
		
	}

	@Override
	public void onRender() {
		
	}

	@Override
	public void onExit() {
		System.exit(0);
	}
	
	public static void main(String[] args) {
		new Test();
	}
}
