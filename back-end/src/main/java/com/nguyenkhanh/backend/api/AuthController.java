package com.nguyenkhanh.backend.api;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenkhanh.backend.entity.BehaviorEntity;
import com.nguyenkhanh.backend.entity.ERole;
import com.nguyenkhanh.backend.entity.RoleEntity;
import com.nguyenkhanh.backend.entity.UserEntity;
import com.nguyenkhanh.backend.exception.ResponseMessage;
import com.nguyenkhanh.backend.jwt.JwtTokenUtils;
import com.nguyenkhanh.backend.payload.request.LoginRequest;
import com.nguyenkhanh.backend.payload.request.RegisterRequest;
import com.nguyenkhanh.backend.payload.response.JwtResponse;
import com.nguyenkhanh.backend.services.UserDetailsImpl;
import com.nguyenkhanh.backend.services.Impl.BehaviorServiceImpl;
import com.nguyenkhanh.backend.services.Impl.RoleServiceImpl;
import com.nguyenkhanh.backend.services.Impl.UserServiceImpl;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	UserServiceImpl userService;

	@Autowired
	RoleServiceImpl roleService;

	@Autowired
	BehaviorServiceImpl behaviorService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenUtils jwtTokenUtils;

	@GetMapping(path = "confirm")
	public String confirm(@RequestParam("token") String token) throws TimeoutException {
		return userService.confirmToken(token);
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

		try {
			// Kiểmm tra username tồn tại chưa
			if (userService.isUserExitsByUsername(registerRequest.getUsername())) {
				return ResponseEntity.badRequest().body(new ResponseMessage(new Date(), HttpStatus.BAD_REQUEST.value(),
						"Bad Request", "Error: Username is exist already! Please try other name!"));
			}
			// Kiểmm tra email tồn tại chưa
			if (userService.isUserExitsByEmail(registerRequest.getEmail())) {
				return ResponseEntity.badRequest().body(new ResponseMessage(new Date(), HttpStatus.BAD_REQUEST.value(),
						"Bad Request", "Error: Email is exist already! Please try other email!"));
			}
			// Kiểmm tra password
			if (registerRequest.getPassword() == null) {
				return ResponseEntity.badRequest().body(new ResponseMessage(new Date(), HttpStatus.BAD_REQUEST.value(),
						"Bad Request", "Password mustn't be null value"));
			}

			// Tạo role
			Set<String> strRoles = registerRequest.getRoles();
			Set<RoleEntity> roles = new HashSet<>();

			if (strRoles == null) {
				RoleEntity userRole = roleService.roleFindByName(ERole.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);
			} else {
				strRoles.forEach(role -> {
					switch (role) {
					case "admin":
						RoleEntity adminRole = roleService.roleFindByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);
						break;
					case "mod":
						RoleEntity modRole = roleService.roleFindByName(ERole.ROLE_MODERATOR)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(modRole);
						break;
					default:
						RoleEntity userRole = roleService.roleFindByName(ERole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);

					}
				});
			}

			// Tạo behavior
			Set<String> strBehaviors = registerRequest.getBehaviors();
			Set<BehaviorEntity> behaviors = new HashSet<>();
			if (strBehaviors == null) {
				behaviors.add(null);
			} else {
				for (String element : strBehaviors) {
					if (!(behaviorService.isBehaviorExistsByName(element))) {
						return ResponseEntity.badRequest().body(new ResponseMessage(new Date(),
								HttpStatus.BAD_REQUEST.value(), "Bad Request", "Error: Behavior is not found!"));
					} else {
						BehaviorEntity behavior = behaviorService.findByName(element)
								.orElseThrow(() -> new RuntimeException("Error: Behavior is not found!"));
						behaviors.add(behavior);
					}
				}
			}
			// Tạo account
			UserEntity user = new UserEntity(registerRequest.getUsername(), registerRequest.getEmail(),
					passwordEncoder.encode(registerRequest.getPassword()));

			user.setBehaviors(behaviors);
			user.setRoles(roles);
			user.setStatus(false);

			userService.save(user);
			return ResponseEntity
					.ok(new ResponseMessage(new Date(), HttpStatus.OK.value(), "Registered successfully!"));
		} catch (DataAccessException ex) {
			System.out.println(ex.getLocalizedMessage());
			return ResponseEntity.badRequest().body(new ResponseMessage(new Date(), HttpStatus.BAD_REQUEST.value(),
					"Bad Request", "Account is already in use. Please try other account!"));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest)  {
		try {
			// Xác thực username password
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			// Set thông tin authentication vào Security Context
			SecurityContext securityContext = SecurityContextHolder.getContext();
			securityContext.setAuthentication(authentication);

			// Tạo token
			String jwt = jwtTokenUtils.generateJwtToken(authentication);

			// Truy xuất thông tin người dùng đang đặng nhập.
			UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();

			if (userDetailsImpl == null) {
				return ResponseEntity.badRequest().body(new ResponseMessage(new Date(), HttpStatus.BAD_REQUEST.value(),
						"Bad Request", "Error: User not found!"));
			}

			List<String> roles = userDetailsImpl.getAuthorities().stream().map(role -> role.getAuthority())
					.collect(Collectors.toList());

			return ResponseEntity.ok(new JwtResponse(jwt, userDetailsImpl.getId(), userDetailsImpl.getUsername(),
					userDetailsImpl.getEmail(), roles, userDetailsImpl.isEnabled()));

		} 
//		catch (AuthenticationException ex) {
//			ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.UNAUTHORIZED.value(), "Unauthorized",
//					ex.getMessage());
//			return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
//		}
		catch (DisabledException ex) {
			ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.UNAUTHORIZED.value(), "Unauthorized",
					"Account is disabled!");
			return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
		} catch (BadCredentialsException ex) {
			ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.UNAUTHORIZED.value(), "Unauthorized",
					"Username or password is incorrect!");
			return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
		}
	}
}
