package sshReplayCore;

public class SshCommand {
	private String command;
	private String result;
	private Integer exitStatus;
	
	public SshCommand(String command) {
		this.command	=	command;
		this.result		= 	"";
		this.exitStatus	=	0;
	}
	
	public String getCommand() {
		return this.command;
	}
	
	public void setResult(String result) {
		this.result		=	result;
	}
	
	public String getResult() {
		return this.result;
	}
	
	public void setExitStatus(Integer exitStatus) {
		this.exitStatus	=	exitStatus;
	}
	
	public Integer getExitStatus() {
		return this.exitStatus;
	}
	
}