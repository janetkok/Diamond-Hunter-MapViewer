package com.neet.DiamondHunter.MapViewer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

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

    private int tileSize;
    private int[][] map;
    private int mapWidth;
    private int mapHeight;

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
        canvas.setWidth(mapWidth * 16);
        canvas.setHeight(mapHeight * 16);
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
                        col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }
    }


}