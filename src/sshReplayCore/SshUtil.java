package sshReplayCore;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sshReplayGUI.Main;

public class SshUtil {
	
	/**
	 * Retrieve stage and change scene with the one passed
	 * in parameters.
	 * @param stage Stage on which we should swipe of scene
	 * @param scene Scene on which swipe
	 * @throws IOException, NullPointerException 
	 */
	public static void changeScene(Stage stage, String scene) throws IOException, NullPointerException {
    	Pane newScene;
    	
    	if(scene != null) {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(Main.class.getResource("view/" + scene + ".fxml")); 
    		newScene = loader.load();

            // Show the scene containing the root layout.
            Scene loadedScene = new Scene(newScene);
            stage.setScene(loadedScene);
            stage.show();
    	}else {
    		throw new NullPointerException();
    	}
    		
	}
	
	/**
	 * Get stage from rootLayout using javaFX method.
	 * @param rootLayout
	 * @return Primary stage
	 */
	public static Stage getStage(Pane rootLayout) {
		return (Stage) rootLayout.getScene().getWindow();
	}
	
	/**
	 * Spawn an alert box with an error message passed
	 * by parameter.
	 * @param errorMessage
	 */
	public static void alertError(String errorMessage) {
		Alert alert = new Alert(AlertType.ERROR);
    	alert.setTitle("Error Dialog");
    	alert.setHeaderText("Something has gone wrong !");
    	alert.setContentText(errorMessage);
    	
    	alert.showAndWait();
    	errorMessage = "";
	}
}