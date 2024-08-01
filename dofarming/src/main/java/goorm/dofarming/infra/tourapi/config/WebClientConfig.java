package goorm.dofarming.infra.tourapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfig {

    // 내 키
//    public final static String SERVICE_KEY = "qR6tI%2BnsyPolriwrrMXbxu3BMliH02gg9FVLp%2F09CsteVUoKGJ5a5PUydtAlvVkSXaalt9mHaOIi51LGXB1iug%3D%3D";
    // 태우님 키
//    public final static String SERVICE_KEY = "vpMRPaIIqaPZXFNvjQDR7x4N%2BKH7znrJvy0gSJeWXr33uRQ9TwIjiEdlowd%2FVANtxoj8m4U11rNi4QvRGfYkYA%3D%3D";
    // 소희님 키
//    public final static String SERVICE_KEY = "hJujlDHVK0rhAoo%2BrMv1VwvhmIXXsttkEHOnOWGHJLACsJKSu%2F3ihAeq7Iqsdko7mN9t6p2M5tsVxanbAGWktA%3D%3D";
    // 근재님 키
    public final static String SERVICE_KEY = "H4AghKETJoCMBx3vYjxCKnw2Faksg8gUWlUBjlt73czwAuHT6va2rBNHt%2BKVvfdQ4pbwMWrAGciN6QlIIpaPdQ%3D%3D";
    // 호성님 키
//    public final static String SERVICE_KEY = "ocESuTmeyrKIfb1ow%2B6OrXfXHKNR%2BTBBbN7ilvuxAEAQXAxqo27UHuZ3YfPfeqcai5oeFsLyFOOV79V7JfnP6w%3D%3D";

    @Bean
    public DefaultUriBuilderFactory builderFactory() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        return factory;
    }

    @Bean
    public WebClient webClient() {

//        ConnectionProvider provider = ConnectionProvider.builder("custom")
//                .maxConnections(500)  // 최대 연결 수
//                .pendingAcquireMaxCount(15000)  // 대기열 최대 크기
//                .build();
//
//        HttpClient httpClient = HttpClient.create(provider);

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Accept", "/;q=0.9");
//
//        WebClient webClient = WebClient.builder()
//                .defaultHeaders(httpHeaders -> {
//                    httpHeaders.addAll(headers);
//                })
//                .build();

        return WebClient.builder()
                .uriBuilderFactory(builderFactory())
                .build();
    }

    public static String getServiceKey() {
        return SERVICE_KEY;
    }
}
