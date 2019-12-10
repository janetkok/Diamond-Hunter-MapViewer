package com.neet.DiamondHunter.MapViewer;

import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MapViewer extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane page = FXMLLoader.load(getClass().getResource("MapViewer.fxml"));
		Scene scene = new Scene(page);
		scene.getStylesheets().add(getClass().getResource("MapViewer.css").toExternalForm());
		primaryStage.setTitle("MapViewer");
		primaryStage.getIcons().add(new Image("/FXML-Image/windowsIcon.png"));
		primaryStage.setTitle(" MapViewer");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.setResizable(false);
		primaryStage.show();
		primaryStage.getIcons().add(new Image("/FXML-Image/windowsIcon.png"));
		primaryStage.setOnCloseRequest(event -> {
			//confirmation dialog appears when admin closes MapViewer without saving
			if (MapViewerController.getItemMap() == "" || MapViewerController.getItemMap() == null) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Leave without saving?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {//if admin confirms to leave,system exit
					System.exit(0); 
				} else {//admin cancel leaving
					alert.close();
					event.consume();//stop close request and continue with MapViewer
				}
			} else {
				System.exit(0);
			}
		});
	}



	public static void main(String[] args) {

		launch(args);
	}
	

}
