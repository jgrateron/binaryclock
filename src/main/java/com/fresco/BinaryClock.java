package com.fresco;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.swing.JComponent;

public class BinaryClock extends JComponent {
	private static final long serialVersionUID = 1L;
	private final static int FPS = 15;
	private final static int TARGET_TIME = 1_000_000_000 / FPS;

	private Graphics2D g2;
	private BufferedImage image;
	private int widthPanel;
	private int heightPanel;
	private boolean start = true;
	private ClassLoader classLoader = ClassLoader.getSystemClassLoader();

	public void start() {
		widthPanel = getWidth();
		heightPanel = getHeight();
		image = new BufferedImage(widthPanel, heightPanel, BufferedImage.TYPE_INT_ARGB);
		g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		try {
			var is = classLoader.getResource("SymbolMonospacedBT-Regular.otf").openStream();
			Font font = Font.createFont(Font.TRUETYPE_FONT, is);
			Font sizedFont = font.deriveFont(300f);
			g2.setFont(sizedFont);
		} catch (FontFormatException | IOException e) {
			System.err.println(e.getMessage());
			g2.setFont(new Font("Monospaced", Font.PLAIN, 18));
		}
		var thread = new Thread(() -> {
			while (start) {
				long startTime = System.nanoTime();
				drawBackGround();
				drawClock();
				render();
				long endTime = System.nanoTime();
				long time = endTime - startTime;
				if (time < TARGET_TIME) {
					long sleep = (TARGET_TIME - time) / 1_000_000;
					sleep(sleep);
				}
			}
		});
		thread.start();
	}

	private void drawClock() {
		LocalDateTime now = LocalDateTime.now();
		String hour = addZero(Integer.toBinaryString(now.getHour()), 6);
		String minute = addZero(Integer.toBinaryString(now.getMinute()), 6);
		String second = addZero(Integer.toBinaryString(now.getSecond()), 6);
		String uno = ".";
		g2.setColor(Color.LIGHT_GRAY);
		g2.drawString(uno.repeat(6), 20, 150);
		g2.drawString(uno.repeat(6), 20, 300);
		g2.drawString(uno.repeat(6), 20, 450);
		g2.setColor(Color.BLACK);
		g2.drawString(hour.replace("1", uno).replace("0", " "), 20, 150);
		g2.drawString(minute.replace("1", uno).replace("0", " "), 20, 300);
		g2.drawString(second.replace("1", uno).replace("0", " "), 20, 450);
	}

	private void drawBackGround() {
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, widthPanel, heightPanel);
	}

	private void render() {
		var g = getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
	}

	private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ex) {
			System.out.println(ex);
		}
	}

	public String addZero(String number, int max) {
		String result = number;
		while (result.length() < max) {
			result = "0" + result;
		}
		return result;
	};
}
