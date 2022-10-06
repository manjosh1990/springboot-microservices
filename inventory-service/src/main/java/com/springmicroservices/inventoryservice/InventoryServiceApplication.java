package com.springmicroservices.inventoryservice;

import com.springmicroservices.inventoryservice.model.Inventory;
import com.springmicroservices.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("iphone_13");
			inventory.setQuantity(100);
			inventoryRepository.save(inventory);
			inventory = new Inventory();
			inventory.setSkuCode("iphone_14");
			inventory.setQuantity(100);
			inventoryRepository.save(inventory);
			inventory = new Inventory();
			inventory.setSkuCode("iphone_13_red");
			inventory.setQuantity(0);
			inventoryRepository.save(inventory);
		};
	}
}
