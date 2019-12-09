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
    
    private int tileSize;
    private int[][] map;
    private int mapWidth;
    private int mapHeight;
    private boolean isAxeHighlighted;
    private boolean isBoatHighlighted;
    
    //zoom feature
    public int zoomRatio;
    public int numRowDisp;
    public int numColDisp;
    public int winTileSize;
    public int rowStart;
    public int colStart;
    
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
        
        //zoom feature
        this.zoomRatio = 1;
        this.winTileSize = 16;
        this.rowStart = 0;
        this.colStart = 0;
        this.numRowDisp = 40;
        this.numColDisp = 40;
        

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

    public void zoomUpdate() {
    	
    	//zoom with current view as center
    	rowStart = rowStart + numRowDisp / 2;
    	numRowDisp = map.length / zoomRatio;
    	rowStart -= numRowDisp / 2;
    	colStart = colStart + numColDisp / 2;;
    	numColDisp = numRowDisp; //map is square
    	colStart -= numColDisp / 2;
    	
    	//in case cannot zoom out with current view as center
    	if(rowStart < 0) {
    		rowStart = 0;
    	}
    	if(rowStart + numRowDisp >= map.length) {
    		rowStart = map.length - numRowDisp;
    	}
    	if(colStart < 0) {
    		colStart = 0;
    	}
    	if(colStart + numColDisp >= 40) {
    		colStart = 40 - numColDisp;
    	}
    }
    
    public void navUpdate(int x) {
    	switch (x){
    	case 1: //up
    		rowStart = Math.max(0, rowStart - (numRowDisp / 2));
    		break;
    	case 2: //down
    		rowStart = Math.min(map.length - numRowDisp, rowStart + (numRowDisp / 2));
    		break;
    	case 3: //left
    		colStart = Math.max(0, colStart - (numColDisp / 2));
    		break;
    	case 4: //right
    		colStart = Math.min(map.length - numColDisp, colStart + (numColDisp / 2));
    		break;
    	}
    }
    
    /**
     * Draw the full tileMap for the game.
     */
    public void drawMap() { //draw map
    	winTileSize = 16 * zoomRatio;  	
    	
        for (int row = 0; row < numRowDisp; row++) {
        	//System.out.println("row " + row);
            for (int col = 0; col < numColDisp; col++) {
            	//System.out.println("col " + col);
                int sourcex = map[row + rowStart][col + colStart];
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
                ((boatX - colStart) * winTileSize), ((boatY - rowStart) * winTileSize), winTileSize, winTileSize);

        //Draw the boat
        graphicsContext.drawImage(itemSprite, 16, 16, tileSize, tileSize,
                ((axeX - colStart) * winTileSize), ((axeY - rowStart) * winTileSize), winTileSize, winTileSize);

        graphicsContext.setStroke(Color.BLACK);
        if (isBoatHighlighted) {
            graphicsContext.strokeRect((boatX - colStart) * winTileSize, (boatY - rowStart) * winTileSize, winTileSize, winTileSize);

        }
        if (isAxeHighlighted) {
            graphicsContext.strokeRect((axeX - colStart) * winTileSize, (axeY - rowStart) * winTileSize, winTileSize, winTileSize);
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
	                ((diamonds[i][1] - colStart) * winTileSize), ((diamonds[i][0] - rowStart) * winTileSize), winTileSize, winTileSize);
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
    	if(map[y + rowStart][x + colStart] >= 20) //check for valid terrain
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