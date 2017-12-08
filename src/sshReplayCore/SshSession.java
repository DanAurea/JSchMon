package sshReplayCore;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.userauth.UserAuthException;

public class SshSession {
	
	private SSHClient client;
	private String	errorMessage = "";
	
	public SshSession(String info, String password) throws IOException{
        String hostInfo[] 	= null;
        String username		= System.getProperty("user.name");
        String host			= "";
        int port			= 22;
		
		client = new SSHClient();
        try {
			client.loadKnownHosts();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        if(info == "" || password == "") {
        	return;
        }
        
        // Retrieve port number and update info
        if(info.indexOf(":") != -1) {
        	hostInfo		= info.split(":");
        	try {
        		port			= Integer.parseInt(hostInfo[1]);
        	} catch(NumberFormatException e) {
        		errorMessage 	= String.valueOf(hostInfo[1]) + " isn't a known port !"; 
        	}
            info			= hostInfo[0];
        }
        
        /** Retrieve host and username typed by user
         * if there is no username specified then the
         * input typed by user is host and username is
         * his system user.
         */
        if(info.indexOf("@") != -1) {
        	hostInfo 		= info.split("@");
        	username		= hostInfo[0];
            host			= hostInfo[1];
        }else {
        	host			= info;
        }       
        
        /**
         * Establish a ssh session with credentials
         * provided by user
         */
        try {
        	if(!client.isConnected())
        		client.connect(host, port);
        	else
        		errorMessage = "Connection refused, port busy !";
		} catch (IOException e) {
			errorMessage = "Connection refused !";
		} 
        
        try{
            client.authPassword(username, password);
        } catch(UserAuthException e) {
        	client.disconnect();
        	errorMessage = "Wrong credentials !";
        } catch (TransportException e) {
			// TODO Auto-generated catch block
			errorMessage = "Request can't be transmitted !";
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			errorMessage = "Connection failed to host " + host + " !";
		}
	};
	
	/**
	 * Execute a ssh command and return its result
	 * using sshj logic. A new session need to be
	 * created each time a command has been executed.
	 * @param cmd
	 * @return Result of command called and its exit status or an empty string.
	 */
	public SshCommand execute(String cmd) throws ConnectionException, TransportException {
		Session session = null;
		SshCommand	sshCommand = new SshCommand(cmd);
		String result	= "";
		
		try {
			session = this.client.startSession();
			final Command exec = session.exec(cmd);
			
			result = IOUtils.readFully(exec.getInputStream()).toString();
			sshCommand.setResult(result);
			
			exec.join(1, TimeUnit.SECONDS);
			sshCommand.setExitStatus(exec.getExitStatus());
			SshContext.getInstance().add(sshCommand);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return sshCommand;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	public SSHClient getClient() {
		return this.client;
	}
}