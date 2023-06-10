package io.rachidassouani.fullstackjava;

import io.rachidassouani.fullstackjava.customer.Customer;
import io.rachidassouani.fullstackjava.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class FullstackjavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FullstackjavaApplication.class, args);
	}


	@Bean
	CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
		return args -> {
			Customer customer1 = new Customer("Rachid", "Assouani", "rachid@gmail.com");
			Customer customer2 = new Customer("Rachida", "Assouani", "rachida@gmail.com");
			Customer customer3 = new Customer("Rachidaa", "Assouani", "rachidaa@gmail.com");

			customerRepository.saveAll(List.of(customer1, customer2, customer3));
		};
	}
}