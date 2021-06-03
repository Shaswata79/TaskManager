package shaswata.taskmanager.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket swaggerApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiDetails());
    }


    private ApiInfo apiDetails(){
        return new ApiInfoBuilder()
                .title("TaskManager REST API")
                .description("RESTful APIs for Task Manager application")
                .version("1.0")
                .contact(new Contact("Shaswata Bhattacharyya", "https://github.com/Shaswata79", "shaswatabhatt2030@gmail.com"))
                .build();
    }


}
