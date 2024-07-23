package goorm.dofarming.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
        basePackages = {
                "goorm.dofarming.domain.jpa",
                "goorm.dofarming.infra.tourapi"
        }
)
public class JpaConfig {
}
