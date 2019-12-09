package com.neet.DiamondHunter.MapViewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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

	@FXML
	private Label fileOpened;

	private MapDrawer tileMap;

	@FXML
	private Pane instructionpane;

	/**
	 * if axe is chosen coordinates[0]=x; coordinates[1]=y; if boat is chosen
	 * coordinates[2]=i; coordinates[3]=j;
	 */
	private boolean axe = false;
	private boolean boat = false;
	private int curMouseX;
	private int curMouseY;
	private int[] coordinates = new int[4];

	// undo
	private ArrayList<int[]> coordinateHistory;

	// file save
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

		// set default coordinate
		coordinates[0] = MapDrawer.DEFAULT_COORDINATE[0];
		coordinates[1] = MapDrawer.DEFAULT_COORDINATE[1];
		coordinates[2] = MapDrawer.DEFAULT_COORDINATE[2];
		coordinates[3] = MapDrawer.DEFAULT_COORDINATE[3];
		coordinateHistory.add(coordinates.clone());

		render();
		initSaveMapChooser();
	}

	/**
	 * save map as default
	 */
	@FXML
	private void onSaveAsDefaultClicked() {
		// confirmation dialog appears when admin wants to replace default map
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Are you sure to replace the default map?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {// if admin confirms to replace
			saveNewCoordinates("default.itm");// save
		}
		alert.close();// close confirmation dialog
	}

	/**
	 * Updates existing opened map
	 */
	@FXML
	private void onSaveClicked() {
		if (itemMap != "")// if file is already saved in somewhere
			saveNewCoordinates(itemMap);// update the file
		else// if file is not saved yet
			onSaveAsClicked();// proceed to save as
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
	 * saves the map to the desired path
	 * 
	 * @param dir : desired path
	 */
	private void saveNewCoordinates(String dir) {
		try {
			PrintWriter itemMapFile = new PrintWriter(dir, "UTF-8");
			itemMapFile.println(BOAT + "," + coordinates[0] + "," + coordinates[1]);
			itemMapFile.println(AXE + "," + coordinates[2] + "," + coordinates[3]);
			itemMapFile.close();
			itemMap = dir;
			information.setText("Map saved!");
			Path p = Paths.get(dir);
			fileOpened.setText(p.getFileName().toString());
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		saveCoordinateHistory();
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
	 * Open a file choser and Load Map file from folder
	 */
	@FXML
	private void onLoadMapClicked() {
		// open file choser and select file
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

		// read coordinate from file
		try {
			FileReader input = new FileReader(itemMap);
			BufferedReader reader = new BufferedReader(input);

			while ((currLine = reader.readLine()) != null) {
				data = currLine.split(",");
				item = Integer.parseInt(data[0]);

				y = Integer.parseInt(data[1]);
				x = Integer.parseInt(data[2]);

				if (item == 0) {
					coordinates[0] = y;
					coordinates[1] = x;
				} else {
					coordinates[2] = y;
					coordinates[3] = x;
				}
			}
			reader.close();
			Path p = Paths.get(itemMap);
			fileOpened.setText(p.getFileName().toString());
			coordinateHistory.add(coordinates.clone());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens about
	 * 
	 */
	@FXML
	private void onAboutClicked() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("Author");
		alert.setContentText("University of Nottingham : \n\nKok Yong En\nOoi Kai Sheng\nKhor Ern Chieh\nYoh Zhi Ying");
		alert.showAndWait();
		alert.setOnCloseRequest(event -> {
			alert.close();
		});
	}

	/**
	 * Handle Axe Button Click event
	 */
	@FXML
	private void onBtnAxeClicked() {
		axe = true;
		boat = false;
		render();
	}

	/**
	 * Handle Boat Button Click event
	 */
	@FXML
	private void onBtnBoatClicked() {
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
	private void highlightCursor(MouseEvent event) {
		curMouseX = ((int) event.getX() / tileMap.winTileSize) + tileMap.colStart;
		curMouseY = ((int) event.getY() / tileMap.winTileSize) + tileMap.rowStart;
		cursorPosition.setText("(" + curMouseX + ", " + curMouseY + ")");
		render();
	}

	/**
	 * Nav up down left right during zoom
	 *
	 * @param event : Key Event
	 */
	@FXML
	private void onKeyPressed(KeyEvent event) {
		switch (event.getCode()) {
		case W:
			onNavUpClicked();
			break;
		case S:
			onNavDownClicked();
			break;
		case A:
			onNavLeftClicked();
			break;
		case D:
			onNavRightClicked();
			break;
		}
	}

	/**
	 * Set the item's location
	 *
	 * @param event MouseEvent
	 */
	@FXML
	private void setLocation(MouseEvent event) {
		int xCo = ((int) event.getX() / tileMap.winTileSize) + tileMap.colStart;
		int yCo = ((int) event.getY() / tileMap.winTileSize) + tileMap.rowStart;
		if (tileMap.isClickable(xCo, yCo)) {
			if (boat) {
				coordinates[0] = xCo;
				coordinates[1] = yCo;
				information.setText("Boat pos updated!");
				boatPosition.setText("Boat: (" + xCo + ", " + yCo + ")");

				render();
			}

			if (axe) {
				coordinates[2] = xCo;
				coordinates[3] = yCo;
				information.setText("Axe pos updated!");
				axePosition.setText("Axe: (" + xCo + ", " + yCo + ")");

				render();
			}
			saveCoordinateHistory();
		} else {
			// if position invalid, display alert dialog
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
	private void resetToDefaultCoordinates() {
		boat = axe = false;
		coordinates[0] = MapDrawer.DEFAULT_COORDINATE[0];
		coordinates[1] = MapDrawer.DEFAULT_COORDINATE[1];
		coordinates[2] = MapDrawer.DEFAULT_COORDINATE[2];
		coordinates[3] = MapDrawer.DEFAULT_COORDINATE[3];
		saveCoordinateHistory();
		render();
	}

	/**
	 * Display an alert box with the instruction for the MapViewer
	 */
	@FXML
	private void onInstructionClicked() {
		instructionpane.toFront();
	}
	@FXML
	private void onOkPressed() {
		instructionpane.toBack();
	}

	/**
	 * Undo changes, returning the items back to their previous coordinates
	 */
	@FXML
	private void undoChanges() {
		boat = axe = false;
		if (coordinateHistory.size() > 1) {
			coordinateHistory.remove(coordinateHistory.size() - 1);
			coordinates = coordinateHistory.get(coordinateHistory.size() - 1).clone();
			saveCoordinateHistory();
			render();
		}
	}

	/**
	 * save coordinate history for undo usage
	 */
	private void saveCoordinateHistory() {
		if (!Arrays.equals(coordinates, coordinateHistory.get(coordinateHistory.size() - 1))) {
			coordinateHistory.add(coordinates.clone());
		}
	}

	/**
	 * getter for itemMap
	 */
	public static String getItemMap() {
		return itemMap;
	}

	/**
	 * Zoom In Map
	 */
	public void onZoomInClicked() {
		if (tileMap.zoomRatio != 4) {
			tileMap.zoomRatio *= 2;
		}
		tileMap.zoomUpdate();
		render();
	}
	
	/**
	 * Zoom Out Map
	 * */
	public void onZoomOutClicked() {
		if (tileMap.zoomRatio != 1)
			tileMap.zoomRatio /= 2;
		tileMap.zoomUpdate();
		render();
	}
	
	/**
	* Zommed map navigation
	*/
	public void onNavUpClicked() {
		tileMap.navUpdate(1);
		render();
	}

	public void onNavDownClicked() {
		tileMap.navUpdate(2);
		render();
	}

	public void onNavLeftClicked() {
		tileMap.navUpdate(3);
		render();
	}

	public void onNavRightClicked() {
		tileMap.navUpdate(4);
		render();
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
