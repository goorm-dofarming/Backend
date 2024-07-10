package goorm.dofarming;

import goorm.dofarming.global.config.JpaConfig;
import goorm.dofarming.global.config.MongoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@Import({JpaConfig.class, MongoConfig.class})
public class DofarmingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DofarmingApplication.class, args);
	}

}
