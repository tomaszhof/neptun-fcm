package pl.neptun.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.neptun.model.User;
import pl.neptun.service.UserAuthenticationService;

@RestController
@RequestMapping("/users")
final class SecuredUsersController {
  @Autowired
  UserAuthenticationService authentication;

  @GetMapping("/current")
  User getCurrent(@AuthenticationPrincipal final User user) {
    return user;
  }

  @GetMapping("/logout")
  boolean logout(@AuthenticationPrincipal final User user) {
    authentication.logout(user);
    return true;
  }
}
