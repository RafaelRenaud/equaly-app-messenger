package com.br.equaly.messenger;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class EqualyAppMessengerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EqualyAppMessengerApplication.class, args);
	}

}
