package br.ufrn.imd.cine.services.user;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.ufrn.imd.cine.services.JwtTokenService;
import br.ufrn.imd.cineframework.models.records.RecoveryJWTRecord;
import br.ufrn.imd.cineframework.models.records.SigninRecord;
import br.ufrn.imd.cineframework.models.records.SignonRecord;
import br.ufrn.imd.cineframework.models.user.User;
import br.ufrn.imd.cineframework.models.user.UserDetailsImpl;
import br.ufrn.imd.cineframework.repositories.GenericRepository;
import br.ufrn.imd.cineframework.repositories.user.UserRepository;
import br.ufrn.imd.cineframework.services.GenericService;
import jakarta.validation.Valid;

@Service
public class UserService extends GenericService<User> {
	private static final long serialVersionUID = 1L;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;

	@Override
	public GenericRepository<User> getRepository() {
		return repository;
	}

	public User register(@Valid SignonRecord signonRecord) throws NoSuchAlgorithmException {
		User user = new User();
		user.setName(signonRecord.name());
		user.setEmail(signonRecord.email());
		user.setUsername(signonRecord.username());
		user.setPassword(passwordEncoder.encode(signonRecord.password()));
		user.setBio(signonRecord.bio());
		user.setRole(signonRecord.role());

		return insert(user);
	}

	public RecoveryJWTRecord login(SigninRecord loginUserDto) {

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				loginUserDto.username(), loginUserDto.password());

		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		return new RecoveryJWTRecord(jwtTokenService.generateToken(userDetails));
	}

	public User findUserFromToken(String authorizationHeader) {
		String token = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);
		}

		String username = jwtTokenService.getSubjectFromToken(token);

		return repository.findByUsername(username).orElse(null);
	}
}
