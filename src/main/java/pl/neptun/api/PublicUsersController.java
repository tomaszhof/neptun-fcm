package pl.neptun.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.neptun.model.User;
import pl.neptun.service.UserAuthenticationService;
import pl.neptun.service.UsersRepository;

@RestController
@RequestMapping("/public/users")
final class PublicUsersController {

  Logger logger = LoggerFactory.getLogger(PublicUsersController.class);
  
  @Autowired
  UserAuthenticationService authentication;
  
  @Autowired
  private UsersRepository usersRepository;
  
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @PostMapping("/register")
  String register(
    @RequestParam("username") final String username,
    @RequestParam("password") final String password) {
	  User user = new User();
	  user.setUsername(username);
	  user.setPassword(password);
	  logger.debug("Saving user -> username=" + user.getUsername());
		if (user.getPassword() != null) {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setRole("USER");
		}
		User savedUser = usersRepository.saveAndFlush(user);
		logger.debug("Saving user: " + savedUser.getUsername() + " done.");

    return login(username, password);
  }

  @PostMapping("/login")
  String login(
    @RequestParam("username") final String username,
    @RequestParam("password") final String password) {
	  
    if (authentication.login(username, password))
    	return password;
    else return "NoTlOgEd";
  }
}
