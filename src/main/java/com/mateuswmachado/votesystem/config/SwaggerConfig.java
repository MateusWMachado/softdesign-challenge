package com.mateuswmachado.votesystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** Swagger Configuration Class and API Documentation
 *
 * @author Mateus W. Machado
 * @since 17/07/2023
 * @version 1.0.0
 */


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mateuswmachado.votesystem.adapter.inbound.rest"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(buildApiInfo());
    }

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title("Desafio backend Soft Design")
                .description("Rest Api cadastro e votação de pautas")
                .version("1.0.0")
                .contact(new Contact("Mateus W. Machado", "github/MateusWMachado", null))
                .build();
    }
}