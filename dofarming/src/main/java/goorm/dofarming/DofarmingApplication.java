package goorm.dofarming;

import goorm.dofarming.global.config.JpaConfig;
import goorm.dofarming.global.config.MongoConfig;
import io.netty.resolver.dns.DnsNameResolverBuilder;
import io.netty.resolver.dns.macos.MacOSDnsServerAddressStreamProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@Import({JpaConfig.class, MongoConfig.class})
public class DofarmingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DofarmingApplication.class, args);
	}

}
