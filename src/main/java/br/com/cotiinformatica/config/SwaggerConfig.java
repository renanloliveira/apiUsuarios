package br.com.cotiinformatica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration

public class SwaggerConfig {
	@Bean
	OpenAPI customOpenApi() {
		OpenAPI openAPI = new OpenAPI().components(new Components())
				.info(new Info().title("API Usuários - COTI Informática")
						.description("API para cadastro e autenticação de usuários.").version("v1"));
		return openAPI;
	}
}
