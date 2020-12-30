package com.nguyenkhanh.backend.api;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenkhanh.backend.dto.BehaviorDTO;
import com.nguyenkhanh.backend.entity.Behavior;
import com.nguyenkhanh.backend.entity.ERole;
import com.nguyenkhanh.backend.entity.Role;
import com.nguyenkhanh.backend.entity.User;
import com.nguyenkhanh.backend.payload.request.RegisterRequest;
import com.nguyenkhanh.backend.payload.response.MessageResponse;
import com.nguyenkhanh.backend.repository.BehaviorRepository;
import com.nguyenkhanh.backend.repository.RoleRepository;
import com.nguyenkhanh.backend.repository.UserRepository;
import com.nguyenkhanh.backend.services.Impl.BehaviorService;
import com.nguyenkhanh.backend.services.Impl.UserService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class APIController {
	@Autowired
	private UserService userService;

	@Autowired
	private BehaviorService behaviorService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BehaviorRepository behaviorRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	RoleRepository roleRepository;

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}

	@GetMapping("/user1")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/mod")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	public String adminAccess() {
		return "Admin Board.";
	}

//---------------------------------------------------------------------------------------------------------

	@GetMapping("/user")
	public List<User> getUser() {
		List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		return users;
	}

	@GetMapping("/user/{id}")
	public Optional<User> getUserById(@PathVariable("id") long id) {
		Optional<User> user = userRepository.findById(id);
		return user;
	}

	@PutMapping("/user/{id}")
	public ResponseEntity<?> updateUser(@RequestBody RegisterRequest registerRequest, @PathVariable("id") long id) {

		// Tạo account
		User user = new User(registerRequest.getUsername(), registerRequest.getEmail()
//				passwordEncoder.encode(registerRequest.getPassword())
		);
		User oldUser = userRepository.getOne(id);

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
		user.setId(id);
		user.setPassword(oldUser.getPassword());
		user.setBehaviors(behaviors);
		user.setRoles(roles);
		user.setStatus(true);

		user.setCreateDate(oldUser.getCreateDate());
		user.setCreatedBy(oldUser.getCreatedBy());

		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("Updated successfully!"));
	}

	@DeleteMapping("/user")
	public ResponseEntity<?> deleteUsers(@RequestBody long[] ids) {
		userService.delete(ids);
		return ResponseEntity.ok(new MessageResponse("Deleted successfully!"));
	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
		userService.delete(id);
		return ResponseEntity.ok(new MessageResponse("Deleted successfully!"));
	}
//---------------------------------------------------------------------------------------------------------

	@GetMapping("/behavior")
	public List<Behavior> getBehavior() {
		List<Behavior> behaviors = behaviorRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		return behaviors;
	}

	@PostMapping("/behavior")
	public ResponseEntity<?> create(@RequestBody BehaviorDTO dto) {
		if (behaviorRepository.existsByName(dto.getName())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Behavior already exists!"));
		}
		behaviorService.save(dto);
//		return new ResponseEntity<>(behaviorService.save(dto), HttpStatus.CREATED);
		return ResponseEntity.ok(new MessageResponse("Behavior registered successfully!"));
	}

	@PutMapping("/behavior/{id}")
	public ResponseEntity<?> updateBehavior(@RequestBody BehaviorDTO dto, @PathVariable("id") long id) {
		dto.setId(id);
		behaviorService.save(dto);
		return new ResponseEntity<>(behaviorService.save(dto), HttpStatus.OK);
	}

	@DeleteMapping("/behavior")
	public ResponseEntity<?> deleteBehavior(@RequestBody long[] ids) {
		behaviorService.delete(ids);
		return ResponseEntity.ok(new MessageResponse("Deleted successfully!"));
	}

}
