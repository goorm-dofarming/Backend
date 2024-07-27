package goorm.dofarming;

import goorm.dofarming.global.config.JpaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({JpaConfig.class})
public class DofarmingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DofarmingApplication.class, args);
	}

}
