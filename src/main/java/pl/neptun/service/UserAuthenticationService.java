package pl.neptun.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pl.neptun.jpa.UsersRepository;
import pl.neptun.model.User;

//only that class is currently used to neptun auth
@Service
public final class UserAuthenticationService {

	Logger logger = LoggerFactory.getLogger(UserAuthenticationService.class);
	
	@Autowired
	UsersRepository usersRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	Map<String, User> users = new HashMap<>();

	public boolean login(final String username, final String password) {
		try {
			User u = usersRepository.findTopByUsername(username);
			if (u != null) {
				logger.debug("User login found...");
				if (bCryptPasswordEncoder.matches(password, u.getPassword())) {
					users.put(username, u);
					logger.debug("User password OK.");
					return true;
				}
			}
		} catch (Exception ex) {
			logger.debug("Logging error: " + ex.getMessage());
		}
		logger.debug("User password BAD.");
		return false;
	}

	public void logout(final User user) {
		users.remove(user.getUsername());
	}

	public boolean isAuthenticated(String username) {
		return users.containsKey(username);
	}
}
