package es.deusto.sd.facebook.registry;

import java.util.HashMap;
import java.util.Map;

public class UserRegistry {
	private static Map<String, User> usersByEmail = new HashMap<>();
	
	public static void addUser(User user) {
		usersByEmail.put(user.email(), user);
	}
	
	public static boolean userExists(String email) {
		return usersByEmail.containsKey(email);
	}
	
	public static boolean authorizeUser(String email, String password) {
		User user = usersByEmail.get(email);
		return user != null && user.password().equals(password);
	}

}
