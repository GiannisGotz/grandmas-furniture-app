package gr.aueb.cf.grandmasfurnitureapp.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Arrays;
import java.util.List;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server")
                ))
                .info(new Info()
                        .title("Grandma's Furniture API")
                        .version("1.0.0")
                        .description("API for buying and selling old and antique furniture. " +
                                "To authenticate, use the login endpoint with username: Admin1 and password: Cosmote1@. " +
                                "For multipart requests (create ad with image), ensure the 'ad' field has Content-Type: application/json and 'image' field contains the image file.")
                        .contact(new Contact()
                                .name("Giannis")
                                .email("mathsgotzaridis@yahoo.gr")
                                .url("https://cf.gr"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }

    /**
     * Configure MappingJackson2HttpMessageConverter to support application/octet-stream media type.
     * This fixes the Swagger UI multipart request issue where the 'ad' field is sent as application/octet-stream.
     * It is required for Swagger CreateAd scenario testing.
     */
//    @Bean
//    public MappingJackson2HttpMessageConverter octetStreamJsonConverter() {
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setSupportedMediaTypes(Arrays.asList(new MediaType("application", "octet-stream")));
//        return converter;
//    }
}