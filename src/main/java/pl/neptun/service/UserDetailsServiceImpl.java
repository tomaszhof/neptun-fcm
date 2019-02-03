package pl.neptun.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.neptun.jpa.UsersRepository;
import pl.neptun.model.User;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UsersRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User user = userRepository.findTopByUsername(username);
			List<GrantedAuthority> grantedAuthorities = AuthorityUtils
					.commaSeparatedStringToAuthorityList(user.getRole());
			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
					grantedAuthorities);
		} catch (Exception ex) {
			throw new UsernameNotFoundException(ex.getMessage());
		}
	}
}