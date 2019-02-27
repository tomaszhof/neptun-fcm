package pl.neptun.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import pl.neptun.jpa.UsersRepository;
import pl.neptun.model.TestResult;
import pl.neptun.model.User;
import pl.neptun.service.UserAuthenticationService;

@RestController
@RequestMapping("admin/api/users")
final class SecuredUsersController {
	@Autowired
	UserAuthenticationService authentication;

	@Autowired
	private UsersRepository usersRepository;


	@GetMapping("/all")
	public List<User> retrieveAllUsers() {
		return usersRepository.findAll();
	}

	@GetMapping("id//all")
	public Map<String, Long> getUsersId() {
		Map<String, Long> usersId = new HashMap<>();
		List<User> users = usersRepository.findAll();

		for (User user :
				users) {
			usersId.put(user.getUsername(), user.getId());
		}

		return usersId;
	}

	@GetMapping("/{id}")
	public User retrieveUser(@PathVariable long id) {
		Optional<User> user = usersRepository.findById(id);

		if (!user.isPresent())
			return null;

		return user.get();
	}

	@GetMapping("/{id}/latest")
	public TestResult retrieveUserLatest(@PathVariable long id) {
		Optional<User> user = usersRepository.findById(id);

		if (!user.isPresent())
			return null;

		List<TestResult> testResults = user.get().getTestsResults();
		return testResults.get(testResults.size() - 1);
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

	@RequestMapping(value="/{id}/results/", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody TestResult saveTestResult(@PathVariable long id, @RequestBody TestResult testResult) {
		Optional<User> u = usersRepository.findById(id);
		if (u.isPresent()) {
			User user = u.get();
//			testResult = testsResultsRepository.save(testResult);


			// // // // // // // // // // // // // // // //
			// dodawanie poprzednich odpowiedzi do nowych
			List<TestResult> userResults = user.getTestsResults();

			TestResult latestResult = new TestResult();
			if (userResults != null && !userResults.isEmpty())
				latestResult = userResults.get(userResults.size()-1);

			if(testResult.getBeforeAnswers() == null || testResult.getBeforeAnswers().equals("null") || testResult.getBeforeAnswers().equals(""))
				testResult.setBeforeAnswers(latestResult.getBeforeAnswers());

			if(testResult.getAfterAnswers() == null || testResult.getAfterAnswers().equals("null") || testResult.getAfterAnswers().equals(""))
				testResult.setAfterAnswers(latestResult.getAfterAnswers());

			if(testResult.getShortestPath() == null || testResult.getShortestPath().equals("0") || testResult.getShortestPath().equals(""))
				testResult.setShortestPath(latestResult.getShortestPath());

			if(testResult.getRealPath() == null || testResult.getRealPath().equals("0") || testResult.getRealPath().equals(""))
				testResult.setRealPath(latestResult.getRealPath());

			if(testResult.getDeviation() == null || testResult.getDeviation().equals("0") || testResult.getDeviation().equals(""))
				testResult.setDeviation(latestResult.getDeviation());

			if(testResult.getMaxDeviation() == null || testResult.getMaxDeviation().equals("0") || testResult.getMaxDeviation().equals(""))
				testResult.setMaxDeviation(latestResult.getMaxDeviation());

			if(testResult.getIntegralU() == null || testResult.getIntegralU().equals("0") || testResult.getIntegralU().equals(""))
				testResult.setIntegralU(latestResult.getIntegralU());

			// // // // // // // // // // // // // // // //

			user.getTestsResults().add(testResult);

			usersRepository.save(user);


//			usersRepository.saveAndFlush(user);
			return testResult;
		}
		return null;
	}


}
