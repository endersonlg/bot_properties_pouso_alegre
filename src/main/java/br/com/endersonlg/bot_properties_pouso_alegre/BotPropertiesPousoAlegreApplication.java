package br.com.endersonlg.bot_properties_pouso_alegre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BotPropertiesPousoAlegreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BotPropertiesPousoAlegreApplication.class, args);
	}

}
