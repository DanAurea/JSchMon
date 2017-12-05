package sshReplayCore;

public class SshCommand {
	private String command;
	private String result;
	
	public SshCommand(String command, String result) {
		this.command	=	command;
		this.result		=	result;
	}
}