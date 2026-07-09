package lv.bootcamp.shelter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI shelterOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Shelter API")
                        .version("1.0.0")
                        .description("REST API for managing shelter animals: browsing, adding, and adopting."));
    }
}