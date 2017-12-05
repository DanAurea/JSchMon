package sshReplayCore;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.StreamCopier;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.connection.channel.direct.Session.Shell;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.userauth.UserAuthException;
import net.schmizz.sshj.common.LoggerFactory;

public class SshSession {
	
	private SSHClient client;
	private String	errorMessage = "";
	
	public SshSession(String info, String password){
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
        
        // Retrieve host and username typed by user
        // if there is no username specified then the
        // input typed by user is host and username is
        // his system user.
        if(info.indexOf("@") != -1) {
        	hostInfo 		= info.split("@");
        	username		= hostInfo[0];
            host			= hostInfo[1];
        }else {
        	host			= info;
        }       
        
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
            final Session session = client.startSession();
            
        } catch(UserAuthException e) {
        	errorMessage = "Wrong credentials !";
        } catch (TransportException e) {
			// TODO Auto-generated catch block
			errorMessage = "Request can't be transmitted !";
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			errorMessage = "Connection refused !";
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			errorMessage = "Connection failed to host " + host + " !";
		}finally{
			
            try {
				client.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				errorMessage	= "Session hasn't been terminated correctly !"; 
				e.printStackTrace();
			}
            
        }
	};
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	public SSHClient getClient() {
		return this.client;
	}
}