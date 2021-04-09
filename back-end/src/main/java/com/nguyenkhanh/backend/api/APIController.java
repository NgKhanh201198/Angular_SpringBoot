package com.nguyenkhanh.backend.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenkhanh.backend.dto.BehaviorDTO;
import com.nguyenkhanh.backend.dto.UserDTO;
import com.nguyenkhanh.backend.entity.BehaviorEntity;
import com.nguyenkhanh.backend.entity.ERole;
import com.nguyenkhanh.backend.entity.RoleEntity;
import com.nguyenkhanh.backend.entity.UserEntity;
import com.nguyenkhanh.backend.exception.ResponseMessage;
import com.nguyenkhanh.backend.payload.request.RegisterRequest;
import com.nguyenkhanh.backend.services.Impl.BehaviorServiceImpl;
import com.nguyenkhanh.backend.services.Impl.RoleServiceImpl;
import com.nguyenkhanh.backend.services.Impl.UserServiceImpl;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class APIController {
	@Autowired
	UserServiceImpl userService;

	@Autowired
	RoleServiceImpl roleService;

	@Autowired
	BehaviorServiceImpl behaviorService;

	@Autowired
	PasswordEncoder passwordEncoder;

//User---------------------------------------------------------------------------------------------------------

	@GetMapping("/search")
	public ResponseEntity<?> search(@RequestParam(name = "keyword", defaultValue = "") String keyword) {
		List<UserEntity> users = userService.searchUser(keyword);
		return ResponseEntity.ok(users);
	}

	@GetMapping("/user")
	public ResponseEntity<?> getUser() {
		List<UserEntity> users = userService.userFindAll();
		return new ResponseEntity<List<UserEntity>>(users, HttpStatus.OK);
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<?> getUserById(@Valid @PathVariable("id") long id) {
		if (userService.isUserExitsById(id) == false) {
			ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.NOT_FOUND.value(), "Not Found",
					"Not found ID = " + id);
			return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
		} else {
			UserEntity user = userService.userFindById(id);
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		}
	}

	@PutMapping("/user/{id}")
	public ResponseEntity<?> updateUser(@Valid @RequestBody RegisterRequest registerRequest,
			@PathVariable("id") long id) {
		try {
			if (userService.isUserExitsById(id) == false) {
				ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.NOT_FOUND.value(), "Not Found",
						"Not found ID = " + id);
				return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
			} else {
				// Tạo account
				UserEntity user = new UserEntity(registerRequest.getUsername(), registerRequest.getEmail());
				UserDTO oldUser = userService.userGetOne(id);

				// Kiểmm tra username tồn tại chưa
				if (userService.isUserExitsByUsername(registerRequest.getUsername())
						&& !(registerRequest.getUsername().equals(oldUser.getUsername()))) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new ResponseMessage(new Date(), HttpStatus.BAD_REQUEST.value(), "Bad Request",
									"Error: Username is exist already! Please try other name!"));
				}
				// Kiểmm tra email tồn tại chưa
				if (userService.isUserExitsByEmail(registerRequest.getEmail())
						&& !(registerRequest.getEmail().equals(oldUser.getEmail()))) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new ResponseMessage(new Date(), HttpStatus.BAD_REQUEST.value(), "Bad Request",
									"Error: Email is exist already! Please try other email!"));
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
					roles.add(null);
				} else {
					strBehaviors.forEach(item -> {
						BehaviorEntity behavior = behaviorService.findByName(item)
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

				userService.update(user);

				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseMessage(new Date(), HttpStatus.OK.value(), "Updated successfully!"));
			}
		} catch (Exception ex) {
			System.out.println(ex.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseMessage(new Date(), HttpStatus.BAD_REQUEST.value(), "Bad Request",
							"Account is already in use. Please try other account!"));
		}

	}

	@PutMapping("/user/restore/{id}")
	public ResponseEntity<?> restoreUser(@Valid @PathVariable("id") long id) {
		try {
			userService.deleteAndRestore(id);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseMessage(new Date(), HttpStatus.OK.value(), "Restore successfully!"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new ResponseMessage(new Date(), HttpStatus.NOT_FOUND.value(), "Not Found", "Not found ID = " + id));
		}
	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUser(@Valid @PathVariable("id") long id) {
		try {
			userService.deleteAndRestore(id);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseMessage(new Date(), HttpStatus.OK.value(), "Deleted successfully!"));
		} catch (Exception e) {
			ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.NOT_FOUND.value(), "Not Found",
					"Not found ID = " + id);
			return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/user")
	public ResponseEntity<?> deleteUsers(@Valid @RequestBody long[] ids) {
		try {
			userService.deletes(ids);
			return ResponseEntity.ok(new ResponseMessage(new Date(), HttpStatus.OK.value(), "Deleted successfully!"));
		} catch (Exception e) {
			ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.NOT_FOUND.value(), "Not Found",
					"Not found ID");
			return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
		}
	}

//Behavior---------------------------------------------------------------------------------------------------------
	@GetMapping("/behavior")
	public ResponseEntity<?> getBehavior() {
		try {
			List<BehaviorEntity> behaviors = behaviorService.behaviorFindAll();
			return new ResponseEntity<List<BehaviorEntity>>(behaviors, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseMessage(new Date(), HttpStatus.BAD_REQUEST.value(),
					"Bad Request", "Did not find the data you requested"));
		}
	}

	@PostMapping("/behavior")
	public ResponseEntity<?> create(@Valid @RequestBody BehaviorDTO dto) throws Exception {
		try {
			if (behaviorService.isBehaviorExistsByName(dto.getName())) {
				return ResponseEntity.badRequest().body(new ResponseMessage(new Date(), HttpStatus.BAD_REQUEST.value(),
						"Bad Request", "Behavior is exist already! Please try other behavior!!"));
			}
			behaviorService.save(dto);
			return ResponseEntity
					.ok(new ResponseMessage(new Date(), HttpStatus.OK.value(), "Behavior is created successfully!"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseMessage(new Date(), HttpStatus.BAD_REQUEST.value(),
					"Bad Request", "Behavior is exist already! Please try other behavior!"));
		}
	}

	@PutMapping("/behavior/{id}")
	public ResponseEntity<?> updateBehavior(@Valid @RequestBody BehaviorDTO dto, @PathVariable("id") long id) {
		try {
			if (behaviorService.isBehaviorExistsByName(dto.getName())) {
				return ResponseEntity.badRequest().body(new ResponseMessage(new Date(), HttpStatus.BAD_REQUEST.value(),
						"Bad Request", "Error: Behavior is exist already! Please try other behavior!!"));
			}
			dto.setId(id);
			behaviorService.save(dto);
			return ResponseEntity.ok(new ResponseMessage(new Date(), HttpStatus.OK.value(), "Updated successfully!"));
		} catch (Exception e) {
			ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.NOT_FOUND.value(), "Not Found",
					"Not found ID = " + id);
			return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/behavior/{id}")
	public ResponseEntity<?> deleteBehavior(@Valid @PathVariable("id") long id) {
		try {
			behaviorService.delete(id);
			return ResponseEntity.ok(new ResponseMessage(new Date(), HttpStatus.OK.value(), "Deleted successfully!"));
		} catch (Exception e) {
			ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.NOT_FOUND.value(), "Not Found",
					"Not found ID = " + id);
			return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/behavior")
	public ResponseEntity<?> deleteBehavior(@Valid @RequestBody List<Long> ids) {
		try {
			int index = 0;
			ArrayList<Long> arrID = new ArrayList<Long>();
			for (int i = 0; i < ids.size(); i++) {
				if (!(behaviorService.isBehaviorExistsById(ids.get(i)))) {
					index++;
					arrID.add(ids.get(i));
				}
			}
			for (Long id : ids) {
				if (index == 0) {
					behaviorService.delete(id);
				} else {
					String strID = arrID.stream().map(Object::toString).collect(Collectors.joining("-"));
					ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.NOT_FOUND.value(), "Not Found",
							"Not found ID = [" + strID + "]");
					return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
				}
			}
			return ResponseEntity.ok(new ResponseMessage(new Date(), HttpStatus.OK.value(), "Deleted successfully!"));
		} catch (Exception e) {
			ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.NOT_FOUND.value(), "Not Found",
					"Not found ID");
			return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
		}
	}

}
