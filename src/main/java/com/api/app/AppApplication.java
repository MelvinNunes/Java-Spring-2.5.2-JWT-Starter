package com.api.app;

import com.api.app.models.Role;
import com.api.app.models.User;
import com.api.app.services.RoleService;
import com.api.app.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;

@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	private void createSuperUser(RoleService roleService, UserService userService) throws ParseException {
		// Date of Birth of Admin
		String AdminDate = "01/01/2001";
		Date dateConverted = new SimpleDateFormat("dd/MM/yyyy").parse(AdminDate);
		// Creating an Admin and Adding Role Admin
		User user = new User(null, "Melvin",
				"Nunes", "melvinnunes@gmail.com",
				"admin", "melvinnunes", dateConverted,
				"842807039", null, null, "Av Eduardo Mondlane",null,
				new HashSet<>(), false, true, null, null, null,
				Date.from(Instant.ofEpochSecond(System.currentTimeMillis())), null, null);
		userService.createUser(user);
		roleService.addRoleToUser("admin", "ROLE_ADMIN");
	}

	private void createStandardRoles(RoleService roleService){
		roleService.createRole(new Role(null, "ROLE_ADMIN"));
		roleService.createRole(new Role(null, "ROLE_CLIENTE"));
	}

	// Will Automatically Run On Start
	@Bean
	CommandLineRunner run (RoleService roleService, UserService userService){
		return args -> {
			if (roleService.getAllRoles().isEmpty()){
				createStandardRoles(roleService);
			}
			if (userService.getAllUsers().isEmpty()){
				createSuperUser(roleService, userService);
			}
		};
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}
}
