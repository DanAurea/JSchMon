package sshReplayGUI;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;


public class Main extends Application {
	private Stage primaryStage;
    private StackPane loginLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setMinWidth(480);
        this.primaryStage.setMinHeight(640);
        this.primaryStage.setTitle("Login");
        this.primaryStage.setMaximized(true);

        initloginLayout();
     }

    public void initloginLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/login.fxml")); 
            loginLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(loginLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}