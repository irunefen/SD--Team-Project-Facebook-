package es.deusto.sd.facebook;

import java.io.IOException;
import java.net.ServerSocket;

import es.deusto.sd.facebook.registry.User;
import es.deusto.sd.facebook.registry.UserRegistry;
import es.deusto.sd.facebook.service.AuthService;

public class FacebookAuthServer {
	
	private static int numClients = 0;
	
	public static void main(String args[]) {
		if (args.length < 1) {
			System.err.println(" # Usage: FacebookAuthServer [PORT]");
			System.exit(1);
		}
		
		// Create some users
		UserRegistry.addUser(new User("john.doe@gmail.com", "johndoe1234"));
		UserRegistry.addUser(new User("jane.doe@gmail.com", "janedoe1234"));
		UserRegistry.addUser(new User("alice@gmail.com", "alice1234"));
		UserRegistry.addUser(new User("bob@gmail.com", "bob1234"));
		
		
		int serverPort = Integer.parseInt(args[0]);
		
		try (ServerSocket tcpServerSocket = new ServerSocket(serverPort);) {
			System.out.println(" - FacebookAuthServer: Waiting for connections '" + tcpServerSocket.getInetAddress().getHostAddress() + ":" + tcpServerSocket.getLocalPort() + "' ...");
			
			while (true) {
				new AuthService(tcpServerSocket.accept());
				System.out.println(" - FacebookAuthServer: New client connection accepted. Client number: " + ++numClients);
			}
		} catch (IOException e) {
			System.err.println("# TranslationServer: IO error:" + e.getMessage());
		}
	}
}