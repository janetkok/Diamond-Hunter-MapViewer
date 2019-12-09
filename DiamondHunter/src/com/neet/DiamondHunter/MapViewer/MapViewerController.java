package com.neet.DiamondHunter.MapViewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller for the MapViewer Application. Handles all user input and
 * interaction with MapViewer.
 */
public class MapViewerController {

	@FXML
	private Canvas canvas;
	
	@FXML
	private Label cursorPosition;
	
	@FXML
	private Label information;

	@FXML
	private Label axePosition;

	@FXML
	private Label boatPosition;

	private MapDrawer tileMap;

	/**
	 * if axe is chosen coordinates[0]=x; coordinates[1]=y; if boat is chosen
	 * coordinates[2]=i; coordinates[3]=j;
	 */
	private boolean axe = false;
	private boolean boat = false;
	private int curMouseX;
	private int curMouseY;

	private int[] coordinates = new int[4];
	private ArrayList<int[]> coordinateHistory;

	private static Stage view;
	private FileChooser itemFileChooser = new FileChooser();
	private final int AXE = 1;
	private final int BOAT = 0;
	private static String itemMap = "";

	/**
	 * Initialize function, meant for fxml loader to initialize the controller
	 */
	public void initialize() {
		this.tileMap = new MapDrawer(canvas);
		this.coordinateHistory = new ArrayList<>();
		setCoordinates();
		render();
		initSaveMapChooser();
	}

	/**
	 * save as default
	 */
	@FXML
	public void onSaveAsDefaultClicked() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Are you sure to replace the default map?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			saveNewCoordinates("default.itm");
		} else {
			alert.close();
		}
	}

	/**
	 * save as default
	 */
	@FXML
	public void onSaveClicked() {
		if (itemMap != "")
			saveNewCoordinates(itemMap);
		else
			onSaveAsClicked();
	}

	/**
	 * Opens a File Chooser and saves the map to the selected file
	 * 
	 */
	@FXML
	private void onSaveAsClicked() {
		File itemFile = itemFileChooser.showSaveDialog(view);
		saveNewCoordinates(itemFile.getAbsolutePath());
	}


	/**
	 * Handle Axe Button Click event
	 */
	@FXML
	public void onBtnAxeClicked() {
		axe = true;
		boat = false;
		render();
	}

	/**
	 * Handle Boat Button Click event
	 */
	@FXML
	public void onBtnBoatClicked() {
		axe = false;
		boat = true;
		render();
	}

	/**
	 * Hightlights your current cursor location on mouse moved
	 *
	 * @param event MouseEvent
	 */
	@FXML
	public void highlightCursor(MouseEvent event) {
		curMouseX = (int) event.getX() / tileMap.winTileSize;
		curMouseY = (int) event.getY() / tileMap.winTileSize;
		cursorPosition.setText("(" + curMouseX + ", " + curMouseY + ")");
		render();
	}

	/**
	 * Set the item's location
	 *
	 * @param event MouseEvent
	 */
	@FXML
	public void setLocation(MouseEvent event) {
		int xCo = (int) event.getX() / tileMap.winTileSize;
		int yCo = (int) event.getY() / tileMap.winTileSize;
		if (tileMap.isClickable(xCo, yCo)) {
			if (boat) {
				coordinates[0] = xCo;
				coordinates[1] = yCo;
				information.setText("Boat pos updated!");
				boatPosition.setText(
    					"Boat: (" + xCo + ", " + yCo + ")");

				render();
			}

			if (axe) {
				coordinates[2] = xCo;
				coordinates[3] = yCo;
				information.setText("Axe pos updated!");
				axePosition.setText(
    					"Axe: (" + xCo + ", " + yCo + ")");

				render();
			}
		} else {
			if (boat || axe) {
				information.setText("Position invalid!");
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Warning");
				alert.setHeaderText(null);
				alert.setContentText("You can't place it here.");
				alert.showAndWait();
			}
		}

	}

	/**
	 * Reset the items to their default preset location.
	 */
	@FXML
	public void resetToDefaultCoordinates() {
		boat = axe = false;
		coordinates[0] = MapDrawer.DEFAULT_COORDINATE[0];
		coordinates[1] = MapDrawer.DEFAULT_COORDINATE[1];
		coordinates[2] = MapDrawer.DEFAULT_COORDINATE[2];
		coordinates[3] = MapDrawer.DEFAULT_COORDINATE[3];
//        saveNewCoordinates();
		render();
	}

	/**
	 * Display an alert box with the instruction for the MapViewer
	 */
	@FXML
	public void showInformation() {
		String instructions = "Buttons:\n\n 1) Axe\t\t: Set Axe Location\n 2) Boat\t\t: Set Boat Location\n "
				+ "3) Default\t: Reset Axe and Boat Locations\n 4) Undo\t\t: Undo Previous Changes\n "
				+ "5) Info\t\t: Show MapViewer Instruction";
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(null);
		alert.setHeaderText("MapViewer Instruction");
		alert.setContentText(instructions);
		alert.showAndWait();
	}

	/**
	 * Undo the previous change, returning the items back to their previous
	 * coordinates
	 */
	@FXML
	public void undoChanges() {
		boat = axe = false;
		if (coordinateHistory.size() > 1) {
			coordinateHistory.remove(coordinateHistory.size() - 1);
			coordinates = coordinateHistory.get(coordinateHistory.size() - 1).clone();
//            saveNewCoordinates();
			render();
		}
	}

	/**
	 * set default coordinates
	 */
	private void setCoordinates() {

		coordinates[0] = MapDrawer.DEFAULT_COORDINATE[0];
		coordinates[1] = MapDrawer.DEFAULT_COORDINATE[1];
		coordinates[2] = MapDrawer.DEFAULT_COORDINATE[2];
		coordinates[3] = MapDrawer.DEFAULT_COORDINATE[3];

		coordinateHistory.add(coordinates.clone());
	}

	private void saveNewCoordinates(String dir) {
		try {
			PrintWriter itemMapFile = new PrintWriter(dir, "UTF-8");
			itemMapFile.println(BOAT + "," + coordinates[0] + "," + coordinates[1]);
			itemMapFile.println(AXE + "," + coordinates[2] + "," + coordinates[3]);
			itemMapFile.close();
			itemMap = dir;
			information.setText("Map saved!");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (!Arrays.equals(coordinates, coordinateHistory.get(coordinateHistory.size() - 1))) {
			coordinateHistory.add(coordinates.clone());
		}
	}

	/**
	 * Setup of the Save Map File Chooser including Title, File Name and File Filter
	 */
	private void initSaveMapChooser() {
		itemFileChooser.setTitle("Save Item Map");
		itemFileChooser.setInitialFileName("My Item Map");
		itemFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Item Map", "*.itm"));
	}

	/**
	 * Load Map menu item clicked
	 */
	@FXML
	private void onLoadMapClicked() {
		JFileChooser itemLoader = new JFileChooser();
		itemLoader.setFileFilter(new FileNameExtensionFilter("Item Map File", "itm"));
		int result = itemLoader.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			System.out.println(itemLoader.getSelectedFile().getAbsolutePath());
			itemMap = itemLoader.getSelectedFile().getAbsolutePath();
		}
		String currLine;
		String[] data = new String[3];
		int item, x, y;

		try {
			FileReader input = new FileReader(itemMap);
			BufferedReader reader = new BufferedReader(input);

			while ((currLine = reader.readLine()) != null) {
				data = currLine.split(",");
				item = Integer.parseInt(data[0]);

				y = Integer.parseInt(data[1]);
				x = Integer.parseInt(data[2]);

				placeItem(item, x, y);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void placeItem(int item, int x, int y) {
		if (item == 0) {
			coordinates[0] = y;
			coordinates[1] = x;
		} else {
			coordinates[2] = y;
			coordinates[3] = x;
		}
	}
	
	public static String getItemMap() {
		return itemMap;
	}

	/**
	 * Responsible for the rendering of the canvas. All rendering related operations
	 * are done here.
	 */
	private void render() {
		tileMap.drawMap();
		tileMap.drawAvatar();
		tileMap.drawDiamonds();
		tileMap.setAxeHighlighted(axe);
		tileMap.setBoatHighlighted(boat);
		tileMap.drawItems(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
		tileMap.drawCursorHighlight(curMouseX, curMouseY);
	}
}