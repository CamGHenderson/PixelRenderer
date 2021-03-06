package renderer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.VolatileImage;

import javax.swing.JFrame;

import renderer.renderable.PixelStructure;

public class PixelRenderer {
	private int fpsCap = 300;
	private int fps = 0;
	private final String title;
	private int width = 800, height = 600;
	private boolean fullscreen = false;
	private final float scale;
	private boolean updateRenderer = false;
	private boolean running = false;
	private Thread thread;
	private RendererListener listener;
	private Canvas canvas;
	private Graphics graphics;
	
	public PixelRenderer(String title, int width, int height, float scale, RendererListener listener){
		this.title = title;
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.listener = listener;
	}
	
	public PixelRenderer(String title, float scale, RendererListener listener){
		this.title = title;
		this.scale = scale;
		this.listener = listener;
		fullscreen = true;
	}
	
	public void init() {
		System.setProperty("sun.java2d.opengl", "true");
		running = true;
		thread = new Thread(() -> run());
		thread.start();
	}
	
	public void destroy() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void run(){
		JFrame window = new JFrame();
		window.setTitle(title);
		if(fullscreen) {
			window.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			window.setUndecorated(true);
			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
			width = dimension.width;
			height = dimension.height;
		}
		else{
			window.setSize(width, height);
			window.setLocationRelativeTo(null);
		}
		window.setResizable(false);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				destroy();
			}
		});
		canvas = new Canvas();
		window.add(canvas);
		canvas.setFocusable(true);
		canvas.requestFocus();
		listener.onStart();
		window.setVisible(true);
		
		GraphicsConfiguration gc = canvas.getGraphicsConfiguration();
		VolatileImage frame = gc.createCompatibleVolatileImage((int)Math.round(width/scale), (int)Math.round(height/scale));
		
		long frameTime = System.nanoTime();
		long lastFrameTime = frameTime;
		long delay = (long)(((double)1000 / (double)fpsCap) * 1e+6);
		
		long fpsTime = System.nanoTime();
		long lastFpsTime = fps;
		int fpsCount = 0;
		
		updateRenderer = false;
		while(running) {
			if(updateRenderer) {
				window.dispose();
				window = new JFrame();
				window.setTitle(title);
				if(fullscreen) {
					window.setExtendedState(JFrame.MAXIMIZED_BOTH); 
					window.setUndecorated(true);
					Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
					width = dimension.width;
					height = dimension.height;
				}
				else{
					window.setSize(width, height);
					window.setLocationRelativeTo(null);
				}
				window.setResizable(false);
				window.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						destroy();
					}
				});
				canvas = new Canvas();
				window.add(canvas);
				window.setVisible(true);
				
				gc = canvas.getGraphicsConfiguration();
				frame = gc.createCompatibleVolatileImage((int)Math.round(width/scale), (int)Math.round(height/scale));
				
				delay = (long)(((double)1000 / (double)fpsCap) * 1e+6);
				
				updateRenderer = false;
			}
			
			frameTime = System.nanoTime();
			if(frameTime - lastFrameTime >= delay){
				if(frame.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE)
					frame = gc.createCompatibleVolatileImage((int)Math.round(width/scale), (int)Math.round(height/scale));
				
				graphics = frame.getGraphics();
				graphics.setColor(Color.black);
				graphics.fillRect(0, 0, frame.getWidth(), frame.getHeight());
				
				listener.onRender();
				graphics.dispose();
				
				graphics = canvas.getGraphics();
				graphics.drawImage(frame, 0, 0, width, height, null);	
				graphics.dispose();
				
				fpsCount++;
				lastFrameTime = frameTime;
			}
			
			fpsTime = System.nanoTime();
			if(fpsTime - lastFpsTime >= 1000 * 1e+6) {
				fps = fpsCount;
				fpsCount = 0;
				lastFpsTime = fpsTime;
			}
		}
		listener.onExit();
	}
	
	public void render(PixelStructure pixelStructure) {
		((Graphics2D)graphics).drawImage(pixelStructure.getTexture(), pixelStructure.getTransform(), null);
	}

	public int getFpsCap() {
		return fpsCap;
	}

	public void setFpsCap(int fpsCap) {
		this.fpsCap = fpsCap;
		updateRenderer = true;
	}
	
	public int getFPS() {
		return fps;
	}

	public String getTitle() {
		return title;
	}
	
	public void setWidth(int width) {
		this.width = width;
		updateRenderer = true;
	}
	
	public void setHeight(int height) {
		this.height = height;
		updateRenderer = true;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getPixelWidth() {
		return (int)Math.round(width/scale);
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getPixelHeight() {
		return (int)Math.round(height/scale);
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		updateRenderer = true;
	}
	
	public void setFullscreen(boolean fullscreen){
		this.fullscreen = fullscreen;
		updateRenderer = true;
	}
	
	public boolean getFullscreen() {
		return fullscreen;
	}

	public float getPixelScale() {
		return scale;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
}
