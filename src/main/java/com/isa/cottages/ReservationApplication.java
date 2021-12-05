package com.isa.cottages;

import com.isa.cottages.Controller.CottageController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;

@SpringBootApplication
public class ReservationApplication {

	public static void main(String[] args) {

		SpringApplication.run(ReservationApplication.class, args);
	}

}
