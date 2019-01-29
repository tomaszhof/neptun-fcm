package pl.neptun.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import pl.neptun.model.User;



@Service
final class UserAuthenticationService {
	
	@NonNull
	UsersRepository usersRepository;

	Map<String, User> users = new HashMap<>();
	
	public boolean login(final String username, final String password) {
		try {
			User u = usersRepository.findByUsernameAndPassword(username, password);
			if (u != null) {
				users.put(username, u);
			}
		} catch (Exception ignore) {
		}

		return false;
	}

	public void logout(final User user) {
		users.remove(user.getUsername());
	}
	
	public boolean isAuthenticated(String username) {
		return users.containsKey(username);
	}
}
