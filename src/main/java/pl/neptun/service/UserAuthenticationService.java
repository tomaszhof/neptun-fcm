package pl.neptun.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pl.neptun.importer.QuestionsImporter;
import pl.neptun.model.User;

@Service
public final class UserAuthenticationService {

	Logger logger = LoggerFactory.getLogger(UserAuthenticationService.class);
	
	@NonNull
	UsersRepository usersRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	Map<String, User> users = new HashMap<>();

	public boolean login(final String username, final String password) {
		try {
			User u = usersRepository.findByUsername(username);
			if (u != null) {
				logger.debug("User login found...");
				if (bCryptPasswordEncoder.matches(password, u.getPassword())) {
					users.put(username, u);
					logger.debug("User password OK.");
					return true;
				}
			}
		} catch (Exception ignore) {
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
