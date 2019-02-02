package pl.neptun.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pl.neptun.model.TestResult;
import pl.neptun.model.User;
import pl.neptun.service.TestResultsRepository;
import pl.neptun.service.UserAuthenticationService;
import pl.neptun.service.UsersRepository;

@RestController
@RequestMapping("admin/api/users")
final class SecuredUsersController {
	@Autowired
	UserAuthenticationService authentication;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private TestResultsRepository testsResultsRepository;

	@GetMapping("/all")
	public List<User> retrieveAllUsers() {
		return usersRepository.findAll();
	}

	@GetMapping("/{id}")
	public User retrieveUser(@PathVariable long id) {
		Optional<User> user = usersRepository.findById(id);

		if (!user.isPresent())
			return null;

		return user.get();
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable long id) {
		usersRepository.deleteById(id);
	}

	@GetMapping("/current")
	User getCurrent(@AuthenticationPrincipal final User user) {
		return user;
	}

	@GetMapping("/logout")
	boolean logout(@AuthenticationPrincipal final User user) {
		authentication.logout(user);
		return true;
	}

	@RequestMapping(value="results/", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody TestResult saveSpittle(@RequestBody TestResult testResult) {
		return testsResultsRepository.save(testResult);
	}
}
