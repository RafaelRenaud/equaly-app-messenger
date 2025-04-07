package com.br.equaly.messenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.br.equaly.messenger")
public class EqualyAppMessengerApplication {

	private static ConfigurableApplicationContext context;

	public static void start() {
		if (context == null) {
			context = SpringApplication.run(EqualyAppMessengerApplication.class);
		}
	}

	public static ConfigurableApplicationContext getContext() {
		start();
		return context;
	}
}
