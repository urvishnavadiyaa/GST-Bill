package com.exampl.GST.Bill;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BillApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.out.println("Loaded SID: " + dotenv.get("TWILIO_ACCOUNT_SID"));
		SpringApplication.run(BillApplication.class, args);
	}
}
