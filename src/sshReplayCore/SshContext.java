package sshReplayCore;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Hold informations about current session and commands executed
 * @author danaurea
 *
 */
public class SshContext {
	private Deque<SshCommand> commandStack;
	private SshSession	session = null;
	
	private SshContext() {
		this.commandStack	=	new ArrayDeque<SshCommand>();
	}
	
	private static class ContextHolder{
		private final static SshContext instance = new SshContext();
	}
	
	/**
	 * Find all commands previously typed which contains
	 * command passed in parameter.
	 * @param cmd Command to search
	 * @param strict Define if search should match substring or not
	 * @return All SshCommand matching command parameter
	 */
	public ArrayDeque<SshCommand> findCommands(String cmd, boolean strict){
		ArrayDeque<SshCommand> results = new ArrayDeque<SshCommand>();
		
		for(SshCommand sshCommand: commandStack) {
			
			if(!strict) {
				if(sshCommand.getCommand().toLowerCase().contains(cmd.toLowerCase())) {
					results.add(sshCommand);
				}
			}else {
				if(sshCommand.getCommand().toLowerCase().equals(cmd.toLowerCase())) {
					results.add(sshCommand);
				}
			}
		}
		
		return results;
	}
	
	/**
	 * Add a ssh command to command stack in global
	 * context
	 * @param command
	 */
	public void add(SshCommand command) {
		this.commandStack.addLast(command);
	}
	
	/**
	 * Wipe all commands previously stored.
	 */
	public void clear() {
		this.commandStack.clear();
	}
	
	/**
	 * Print content of command Stack
	 */
	public void printStack() {
		for(SshCommand c: this.commandStack) {
			System.out.println(c.getCommand() + "\n" + c.getResult() + "\n");
		}
	}
	
	/**
	 * Retrieve session holded by current context
	 * @return Ssh session allowing to communicate with remote host
	 */
	public SshSession getSession() {
		return this.session;
	}
	
	/**
	 * Define which session to use in current global context
	 * @param session
	 */
	public void setSession(SshSession session) {
		this.session = session;
	}
	
	/**
	 * Retrieve instance of context respectively to
	 * singleton pattern.
	 * @return
	 */
	public static SshContext getInstance() {
		return ContextHolder.instance;
	}
}