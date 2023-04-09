package com.technarts.airline.Airline;

import com.technarts.airline.Airline.model.entity.ERole;
import com.technarts.airline.Airline.model.entity.Role;
import com.technarts.airline.Airline.model.entity.User;
import com.technarts.airline.Airline.repository.RoleRepository;
import com.technarts.airline.Airline.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.util.Set;

@SpringBootApplication
public class AirlineApplication {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;


	public static void main(String[] args) {
		SpringApplication.run(AirlineApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder encoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CommandLineRunner initDatabase() {
		return args -> {
			try {
				// Create the ADMIN_ROLE
				Role adminRole = new Role();
				adminRole.setName(ERole.ROLE_ADMIN);

				Role userRole = new Role();
				userRole.setName(ERole.ROLE_USER);

				// Save the ADMIN_ROLE to the database
				Role savedAdminRole = roleRepository.save(adminRole);
				Role savedUserRole = roleRepository.save(userRole);

				// Create a new User with the specified username and password
				User user = new User("user", encoder().encode("1234"));
				User mustafa = new User("mustafa", encoder().encode("1234"));

				// Associate the ADMIN_ROLE with the User
				user.setRoles(Set.of(savedAdminRole));
				mustafa.setRoles(Set.of(savedUserRole));

				// Save the User to the database
				userRepository.save(user);
				userRepository.save(mustafa);
			} catch (Exception e) {

			}
		};
	}

}
