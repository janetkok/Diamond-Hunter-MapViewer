package com.neet.DiamondHunter.MapViewer;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Controller for the MapViewer Application.
 * Handles all user input and interaction with MapViewer.
 */
public class MapViewerController {

    @FXML
    private Canvas canvas;

    private MapDrawer tileMap;

    /**
     * if axe is chosen
     * coordinates[0]=x;
     * coordinates[1]=y;
     * if boat is chosen
     * coordinates[2]=i;
     * coordinates[3]=j;
     */

    private int[] coordinates = new int[4];


    /**
     * Initialize function, meant for fxml loader to initialize the controller
     */
    public void initialize() {
        this.tileMap = new MapDrawer(canvas);
        render();
    }

 

    /**
     * Responsible for the rendering of the canvas.
     * All rendering related operations are done here.
     */
    private void render() {
        tileMap.drawMap();
        tileMap.drawAvatar();
        tileMap.drawDiamonds();
        tileMap.drawItems(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
    }
}