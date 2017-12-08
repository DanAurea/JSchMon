package sshReplayGUI.fxcontroller;

import java.util.ArrayDeque;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.transport.TransportException;
import sshReplayCore.SshCommand;
import sshReplayCore.SshContext;
import sshReplayCore.SshSession;
import sshReplayCore.SshUtil;

public class SessionController {
    private SshSession sshSession;
	private String	terminalSymbol = "$ ==> ";
	private String  oldContent = "";
	private int		bufferSize = 0;
	
	@FXML
	private BorderPane rootLayout;
	
	@FXML
	private TextArea displayArea;

	@FXML
    private TextField commandInput;
	
	@FXML
    private TextField searchBar;
	
	@FXML
	private ListView<String> historyView;
	
	/**
	 * Close the application.
	 */
	@FXML
	private void close() {
		SshUtil.getStage(rootLayout).close();
	}
	
	/**
	 * Clear all commands saved in stack
	 */
	@FXML
	private void clear() {
		SshContext.getInstance().clear();
		historyView.getItems().clear();
		displayArea.clear();
		displayArea.appendText(terminalSymbol);
		oldContent = "";
	}
	
	/**
	 * Display a message about developers
	 */
	@FXML
	private void about() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("SshReplay version 0.25b-Magma");
		alert.setContentText("Developer: DanAurea \n" + "Master 1 Informatique\n" + "Promotion 2017-2018\n");

		alert.showAndWait();
	}
	
	/**
	 * Display result about command selected
	 */
	@FXML
	private void selectCommand() {
		String command = historyView.getSelectionModel().getSelectedItem();
		search(command, true);
	}
	
	/**
	 * Display all results in textAera from results passed
	 * in parameter
	 * @param results
	 */
	private void displayResult(ArrayDeque<SshCommand> results) {
		if(oldContent.equals("")) {
			// We save textarea content to retrieve it later
			oldContent = displayArea.getText();
		}
		displayArea.clear();
		
		for(SshCommand sshCommand : results) {
			displayArea.appendText(terminalSymbol);
			displayArea.appendText(sshCommand.getCommand() + "\n");
			displayArea.appendText(sshCommand.getResult() + "\n");
			displayArea.appendText("Exit status: " + sshCommand.getExitStatus() + "\n");
		}
	}
	
	/**
	 * Display all results matching command requested by user
	 * @param command
	 */
	private void search(String command, boolean strict) {
		ArrayDeque<SshCommand> results = new ArrayDeque<SshCommand>();
		
		if(command != null) {
			if(command.equals("*")) {
				results = SshContext.getInstance().findCommands("", strict); // Retrieve all commands saved in stack
				
				this.displayResult(results);

			}else if(!command.equals("")) {
				results = SshContext.getInstance().findCommands(command, strict);
				
				this.displayResult(results);
				
			}else if(command.equals("") && !oldContent.equals("")){
				displayArea.setText(oldContent);
				oldContent = "";
			}
		}
	}
	
	/**
	 * Handle search event (triggered when enter is pressed in search bar)
	 * @param event
	 */
	@FXML
	private void handleSearch(KeyEvent event) {
		String command = "";
		
		if (event.getCode() == KeyCode.ENTER)  {
			command = searchBar.getText();
			
			search(command, false);
		}
	}
	
	/**
	 * Handle command send by user from command input.
	 * @param event
	 */
	@FXML
	private void handleCommand(KeyEvent event) {
		String command = "";
		
		if(!oldContent.equals("")) {
			displayArea.setText(oldContent);
			oldContent  = "";
		}
		
		displayArea.appendText(event.getText());
		
		if (event.getCode() == KeyCode.BACK_SPACE) {
			if(bufferSize > 0) {
				String newOutput = displayArea.getText();

				newOutput = newOutput.substring(0, newOutput.length() - 1);
				
				displayArea.setText(newOutput);
				bufferSize--;
			}
		}else if (event.getCode() == KeyCode.ENTER)  {
			command = commandInput.getText();
			
			try {
				
				displayArea.appendText("\n");
				
				SshCommand sshCommand = sshSession.execute(command);
				
				displayArea.appendText(sshCommand.getResult() + "\n");
				displayArea.appendText("Exit status: " + sshCommand.getExitStatus() + "\n");
				displayArea.appendText(terminalSymbol);
				
				bufferSize = 0;
			} catch (ConnectionException | TransportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(!command.equals("")) {
				historyView.getItems().add(command);
				commandInput.setText("");
			}
		}else if(event.getCode() != KeyCode.TAB && 
				!event.getCode().isArrowKey() && 
				!event.getCode().isFunctionKey() &&
				!event.getCode().isMediaKey() &&
				!event.getCode().isNavigationKey() &&
				!event.getCode().isModifierKey()){
			bufferSize++;
		}
	}
	
	@FXML
    protected void initialize() throws ConnectionException, TransportException {
		sshSession = SshContext.getInstance().getSession();
		displayArea.appendText(terminalSymbol);
	}
}