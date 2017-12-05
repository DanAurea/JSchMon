package sshReplayCore;

import java.util.ArrayDeque;
import java.util.Deque;

public class SshContext {
	private Deque<SshCommand> commandStack;
	
	private SshContext() {
		this.commandStack	=	new ArrayDeque<SshCommand>();
	}
	
	private static class ContextHolder{
		private final static SshContext instance = new SshContext();
	}
	
	public static SshContext getInstance() {
		return ContextHolder.instance;
	}
}