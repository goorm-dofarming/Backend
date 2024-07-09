package goorm.dofarming.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "goorm.dofarming.domain.jpa"
)
public class JpaConfig {
}
