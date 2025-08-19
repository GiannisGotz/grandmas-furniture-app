package gr.aueb.cf.grandmasfurnitureapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication // Marks this class as the primary configuration class and enables component scanning
@EnableJpaAuditing // Enables auditing for JPA entities (e.g., @CreatedDate, @LastModifiedDate)
public class GrandmasfurnitureappApplication {


	/**
	 * Launches the Spring Boot application.
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(GrandmasfurnitureappApplication.class, args);
	}

}
