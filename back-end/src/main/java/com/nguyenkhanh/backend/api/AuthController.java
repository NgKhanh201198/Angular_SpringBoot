package com.nguyenkhanh.backend.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenkhanh.backend.entity.Behavior;
import com.nguyenkhanh.backend.entity.ERole;
import com.nguyenkhanh.backend.entity.Role;
import com.nguyenkhanh.backend.entity.User;
import com.nguyenkhanh.backend.jwt.JwtTokenUtils;
import com.nguyenkhanh.backend.payload.request.LoginRequest;
import com.nguyenkhanh.backend.payload.request.RegisterRequest;
import com.nguyenkhanh.backend.payload.response.JwtResponse;
import com.nguyenkhanh.backend.payload.response.MessageResponse;
import com.nguyenkhanh.backend.repository.BehaviorRepository;
import com.nguyenkhanh.backend.repository.RoleRepository;
import com.nguyenkhanh.backend.repository.UserRepository;
import com.nguyenkhanh.backend.services.UserDetailsImpl;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	BehaviorRepository behaviorRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenUtils jwtTokenUtils;

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
		// Xác thực username password
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		// Set thông tin authentication vào Security Context
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);

		String jwt = jwtTokenUtils.generateJwtToken(authentication);

		// Truy xuất thông tin người dùng đang đặng nhập.
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		if (userDetailsImpl == null) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
		}

		List<String> roles = userDetailsImpl.getAuthorities().stream().map(role -> role.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, userDetailsImpl.getId(), userDetailsImpl.getUsername(),
				userDetailsImpl.getEmail(), roles, userDetailsImpl.isEnabled()));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
		// Kiểmm tra user email tồn tại chưa
		if (userRepository.existsByUsername(registerRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userRepository.existsByEmail(registerRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Tạo account
		User user = new User(registerRequest.getUsername(), registerRequest.getEmail(),
				passwordEncoder.encode(registerRequest.getPassword()));

		// Tạo role
		Set<String> strRoles = registerRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);

				}
			});
		}

		// Tạo behavior
		Set<String> strBehaviors = registerRequest.getBehavior();
		Set<Behavior> behaviors = new HashSet<>();

		if (strBehaviors == null) {
			roles.add(null);
		} else {
			strBehaviors.forEach(item -> {
				Behavior behavior = behaviorRepository.findByName(item)
						.orElseThrow(() -> new RuntimeException("Error: Behavior is not found."));
				behaviors.add(behavior);
			});
		}
		user.setBehaviors(behaviors);
		user.setRoles(roles);
		user.setStatus(true);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("Registered successfully!"));
	}
}
