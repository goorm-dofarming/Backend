package goorm.dofarming.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "goorm.dofarming.domain.mongo"
)
public class MongoConfig {
}
