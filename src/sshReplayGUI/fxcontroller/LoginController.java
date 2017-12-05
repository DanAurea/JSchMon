package sshReplayGUI.fxcontroller;

import java.io.IOException;

import javafx.animation.Animation;
import javafx.animation.Interpolator;

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
import javafx.stage.Stage;
import javafx.util.Duration;
import sshReplayCore.SshSession;
import sshReplayCore.SshUtil;

public class LoginController {
	
	@FXML
	private StackPane rootLayout;
	
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
    	
    	Stage stage 		= SshUtil.getStage(rootLayout);
    	SshSession session 	= null;
    	String errorMessage = "";
    	String login		= loginField.getText();			
    	String password		= passwordField.getText();
    	
    	session 		= new SshSession(login, password);
    	errorMessage 	= session.getErrorMessage();
    	
    	if(!errorMessage.equals("")) {
    		SshUtil.alertError(errorMessage);
    	}
    	
    	if(session != null && session.getClient().isAuthenticated()) {
    		// Swipe on session scene when logged
        	try {
    			SshUtil.changeScene(stage, "session");
    			stage.setWidth(1280);
    			stage.setHeight(800);
    			stage.centerOnScreen();
    			stage.setTitle("Session");
    			
//              try {
//            	session.allocateDefaultPTY();
//
//                final Shell shell = session.startShell();
//
//                new StreamCopier(shell.getInputStream(), System.out, LoggerFactory.DEFAULT)
//                        .bufSize(shell.getLocalMaxPacketSize())
//                        .spawn("stdout");
//
//                new StreamCopier(shell.getErrorStream(), System.err, LoggerFactory.DEFAULT)
//                        .bufSize(shell.getLocalMaxPacketSize())
//                        .spawn("stderr");
//
//                // Now make System.in act as stdin. To exit, hit Ctrl+D (since that results in an EOF on System.in)
//                // This is kinda messy because java only allows console input after you hit return
//                // But this is just an example... a GUI app could implement a proper PTY
//                new StreamCopier(System.in, shell.getOutputStream(), LoggerFactory.DEFAULT)
//                        .bufSize(shell.getRemoteMaxPacketSize())
//                        .copy();
//            }finally{
//            	session.close();
//            }
    			
    		} catch (NullPointerException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}else
    		SshUtil.alertError("Authentication failed !");
    	
    }

}