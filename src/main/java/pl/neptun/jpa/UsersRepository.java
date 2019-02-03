package pl.neptun.jpa;


import org.springframework.data.jpa.repository.JpaRepository;

import pl.neptun.model.User;


public interface UsersRepository extends JpaRepository<User, Long> {

	public User findTopByUsername(String username);
	
	public User findTopByUsernameAndPassword(String username, String password);
}
