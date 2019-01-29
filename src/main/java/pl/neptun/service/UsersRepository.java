package pl.neptun.service;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.neptun.model.User;

public interface UsersRepository extends JpaRepository<User, Long> {

	public User findByUsername(String username);
	
	public User findByUsernameAndPassword(String username, String password);
}
