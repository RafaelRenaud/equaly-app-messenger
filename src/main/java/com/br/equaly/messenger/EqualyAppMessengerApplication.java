package com.br.equaly.messenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EqualyAppMessengerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EqualyAppMessengerApplication.class, args);
	}

}
