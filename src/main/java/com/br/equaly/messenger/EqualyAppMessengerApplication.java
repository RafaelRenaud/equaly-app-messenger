package com.br.equaly.messenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class EqualyAppMessengerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EqualyAppMessengerApplication.class, args);
	}

}
