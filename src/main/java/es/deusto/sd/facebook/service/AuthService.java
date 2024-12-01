package es.deusto.sd.facebook.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

import es.deusto.sd.facebook.registry.UserRegistry;

public class AuthService extends Thread {
	private DataInputStream in;
	private DataOutputStream out;
	private Socket tcpSocket;

	private static String DELIMITER = "#";
	
	public AuthService(Socket socket) {
		try {
			this.tcpSocket = socket;
		    this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.err.println("# FacebookAuthServer - TCPConnection IO error:" + e.getMessage());
		}
	}

	public void run() {
		try {
			String queryData = this.in.readUTF();	
			boolean result = this.handleQuery(queryData);
					
			this.out.writeBoolean(result);
		} catch (EOFException e) {
			System.err.println("   # FacebookAuthServer - TCPConnection EOF error" + e.getMessage());
		} catch (IOException e) {
			System.err.println("   # FacebookAuthServer - TCPConnection IO error:" + e.getMessage());
		} finally {
			try {
				tcpSocket.close();
			} catch (IOException e) {
				System.err.println("   # FacebookAuthServer - TCPConnection IO error:" + e.getMessage());
			}
		}
	}
	
	public boolean handleQuery(String msg) {		
		if (msg == null || msg.trim().isEmpty()) {
			return false;
		}
		
		StringTokenizer tokenizer = new StringTokenizer(msg, DELIMITER);
		String command = tokenizer.nextToken();
		String email = tokenizer.nextToken();
		
		switch (command) {
		case "exists":
			return UserRegistry.userExists(email);
		case "login":
			String password = tokenizer.nextToken();
			return UserRegistry.authorizeUser(email, password);
		default:
			return false;
		}
	
	}
}