// Loads and splits all sprites on start up.
// The sprites can easily be accessed as they
// are public and static.

package com.neet.DiamondHunter.Manager;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Content {
	
	private static String itemMap = "default.itm";
	public static BufferedImage[][] MENUBG = load("/HUD/menuscreen.gif", 128, 144);
	public static BufferedImage[][] BAR = load("/HUD/bar.gif", 128, 16);
	
	public static BufferedImage[][] PLAYER = load("/Sprites/playersprites.gif", 16, 16);
	public static BufferedImage[][] DIAMOND = load("/Sprites/diamond.gif", 16, 16);
	public static BufferedImage[][] SPARKLE = load("/Sprites/sparkle.gif", 16, 16);
	public static BufferedImage[][] ITEMS = load("/Sprites/items.gif", 16, 16);
	
	public static BufferedImage[][] font = load("/HUD/font.gif", 8, 8);
	
	public static BufferedImage[][] load(String s, int w, int h) {
		BufferedImage[][] ret;
		try {
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));
			int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;
			ret = new BufferedImage[height][width];
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
	
	public static void drawString(Graphics2D g, String s, int x, int y) {
		s = s.toUpperCase();
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == 47) c = 36; // slash
			if(c == 58) c = 37; // colon
			if(c == 32) c = 38; // space
			if(c >= 65 && c <= 90) c -= 65; // letters
			if(c >= 48 && c <= 57) c -= 22; // numbers
			int row = c / font[0].length;
			int col = c % font[0].length;
			g.drawImage(font[row][col], x + 8 * i, y, null);
		}
	}
	
	/**
	 * Open file chooser and select map file
	 * */
	public static void loadItems() {
		JFileChooser itemLoader = new JFileChooser();
		itemLoader.setFileFilter(new FileNameExtensionFilter("Item Map File","itm"));
		int result = itemLoader.showOpenDialog(null);
		
		 if (result == JFileChooser.APPROVE_OPTION){
			 System.out.println(itemLoader.getSelectedFile().getAbsolutePath());
			itemMap = itemLoader.getSelectedFile().getAbsolutePath();
		 }
		}

	public static String getItemMap() {
		return itemMap;
	}
	
}
