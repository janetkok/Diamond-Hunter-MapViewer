package com.neet.DiamondHunter.MapViewer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * MapDrawer is responsible for everything related to manipulating
 * and drawing on the canvas.
 */
public class MapDrawer {

    public static String MAP_URL = "/Maps/testmap.map";
    public static final int[] DEFAULT_COORDINATE = {4, 12, 37, 26};

    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private Image tile;
    private Image itemSprite;
    private Image avatarSprite;
    private Image diamondSprite;

    //window tile size
    public int winTileSize = 16;
    
    private int tileSize;
    private int[][] map;
    private int mapWidth;
    private int mapHeight;
    private boolean isAxeHighlighted;
    private boolean isBoatHighlighted;
    
    //diamond coordinates
    private int diamonds[][] = {{20, 20}, {12, 36}, {28, 4}, {4, 34}, {28, 19}, {35, 26}, {38, 36}, {27, 28}, {20, 30}, {14, 25}, {4, 21}, {9, 14}, {4, 3}, {20, 14}, {13, 20}};


    /**
     * Constructor for MapDrawer
     *
     * @param canvas canvas to draw on
     */
    public MapDrawer(Canvas canvas) {
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.tileSize = 16;
        this.tile = new Image("/Tilesets/testtileset.gif");
        this.itemSprite = new Image("/Sprites/items.gif");
        this.avatarSprite = new Image("/Sprites/playersprites.gif");
        this.diamondSprite = new Image("/Sprites/diamond.gif");
        this.isAxeHighlighted = false;
        this.isBoatHighlighted = false;

        try {
            InputStream in = getClass().getResourceAsStream(MAP_URL); //read map file
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            mapWidth = Integer.parseInt(br.readLine());
            mapHeight = Integer.parseInt(br.readLine());
            map = new int[mapHeight][mapWidth];

            String delimiters = " ";
            for (int row = 0; row < mapHeight; row++) {
                String line = br.readLine();
                String[] tokens = line.split(delimiters);
                for (int col = 0; col < mapWidth; col++) {
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Set canvas to dynamic
        canvas.setWidth(mapWidth * winTileSize);
        canvas.setHeight(mapHeight * winTileSize);
    }

    /**
     * Draw the full tileMap for the game.
     */
    public void drawMap() { //draw map
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                int sourcex = map[row][col];
                int sourcey = 0;
                if (sourcex >= (tile.getWidth() / tileSize)) {
                    sourcey++;
                    sourcex = (int) (sourcex - tile.getWidth() / tileSize);
                }
                graphicsContext.drawImage(tile, sourcex * tileSize, sourcey * tileSize, tileSize, tileSize,
                        col * winTileSize, row * winTileSize, winTileSize, winTileSize);
            }
        }
    }

    /**
     * Draw out the usable items boat and axe on the given coordinates.
     *
     * @param boatX X coordinate for the boat
     * @param boatY Y coordinate for the boat
     * @param axeX  X coordinate for the axe
     * @param axeY  Y coordinate for the axe
     */
    public void drawItems(int boatX, int boatY, int axeX, int axeY) {
        //Draw the axe
        graphicsContext.drawImage(itemSprite, 0, 16, tileSize, tileSize,
                (boatX * winTileSize), (boatY * winTileSize), winTileSize, winTileSize);

        //Draw the boat
        graphicsContext.drawImage(itemSprite, 16, 16, tileSize, tileSize,
                (axeX * winTileSize), (axeY * winTileSize), winTileSize, winTileSize);

        graphicsContext.setStroke(Color.BLACK);
        if (isBoatHighlighted) {
            graphicsContext.strokeRect(boatX * winTileSize, boatY * winTileSize, winTileSize, winTileSize);

        }
        if (isAxeHighlighted) {
            graphicsContext.strokeRect(axeX * winTileSize, axeY * winTileSize, winTileSize, winTileSize);
        }
    }

    /**
     * Draw out the sprite/avatar for the playable character of the game on the map
     */
    public void drawAvatar() {
        //Draw the avatar
        graphicsContext.drawImage(avatarSprite, 0, 0, tileSize, tileSize,
                (17 * winTileSize), (17 * winTileSize), winTileSize, winTileSize);
    }

    /**
     * Draw out all the diamonds on the map.
     */
    public void drawDiamonds() {
        //Draw the diamonds
    	for(int i = 0; i < 15; i++) {
	        graphicsContext.drawImage(diamondSprite, 0, 0, tileSize, tileSize,
	                (diamonds[i][1] * winTileSize), (diamonds[i][0] * winTileSize), winTileSize, winTileSize);
    	}
    }


    /**
     * Check if axe and boat can be placed
     *
     * @param x x coordinates
     * @param y y coordinates
     * @return boolean value indicating if the tile is clickable
     */
    public boolean isClickable(int x, int y) {
    	boolean clickable = true;
    	if(map[y][x] >= 20) //check for valid terrain
    		clickable = false;
        for(int i = 0; i < 15; i++) { //check for diamond tiles
        	if(x == diamonds[i][1] && y == diamonds[i][0])
        		clickable = false;
        }
        return clickable;
    }

    /**
     * Setter for isAxeHighlighted
     * Set Axe to be highlighted
     *
     * @param highlight true to highlight, false to de-highlight
     */
    public void setAxeHighlighted(boolean highlight) {
        this.isAxeHighlighted = highlight;
    }

    /**
     * Setter for isBoatHighlighted
     * Set boat to be highlighted
     *
     * @param highlight true to highlight, false to de-highlight
     */
    public void setBoatHighlighted(boolean highlight) {
        this.isBoatHighlighted = highlight;
    }

    /**
     * Draw a box around current cursor location on the map
     * to highlight your current cursor location on the map,
     * and also show each tile's boundaries for better usability.
     * @param xCo Current cursor X coordinates
     * @param yCo Current cursor Y coordinates
     */
    public void drawCursorHighlight(int xCo, int yCo) {
        graphicsContext.setStroke(Color.CORAL);
        graphicsContext.strokeRect(xCo * winTileSize, yCo * winTileSize, winTileSize, winTileSize);
    }
}