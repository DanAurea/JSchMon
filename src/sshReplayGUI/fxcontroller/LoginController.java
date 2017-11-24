package sshReplayGUI.fxcontroller;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {
	
	@FXML
	private StackPane mainContainer;
	
    @FXML
    private Button loginButton;
    
    @FXML
    private TextField loginField;
    
    @FXML
    private TextField passwordField;
    
    @FXML
    void login(ActionEvent event) {
    	final Animation animation = new Transition() {
    		{
                setCycleDuration(Duration.millis(500));
                setInterpolator(Interpolator.LINEAR);
            }

            @Override
            protected void interpolate(double frac) {
                Color vColor = new Color(0.18, 0.45, 0.80, frac);
                loginButton.setBackground(new Background(new BackgroundFill(vColor, new CornerRadii(3.0), Insets.EMPTY)));
            }
    	};
    	animation.play();
    	
    	// Retrieve primaryStage for switching on another scene
    	Stage primaryStage = (Stage) mainContainer.getScene().getWindow();
    	
    	System.out.println("Login");
    }

}