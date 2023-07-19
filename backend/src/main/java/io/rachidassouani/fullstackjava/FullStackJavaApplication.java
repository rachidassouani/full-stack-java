package io.rachidassouani.fullstackjava;

import com.github.javafaker.Faker;
import io.rachidassouani.fullstackjava.customer.Customer;
import io.rachidassouani.fullstackjava.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FullStackJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FullStackJavaApplication.class, args);
	}

	

	@Bean
	CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
		return args -> {
			var name = new Faker().name();
			String firstname = name.firstName();
			String lastname = name.lastName();
			Customer customer = new Customer(
					firstname,
					lastname,
					firstname.toLowerCase()+"."+lastname.toLowerCase()+"@rachidassouani.com");

			customerRepository.save(customer);
		};
	}
}