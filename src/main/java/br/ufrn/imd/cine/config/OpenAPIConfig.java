package br.ufrn.imd.cine.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

	@Value("${simplecine.dev-url}")
	private String devUrl;

	@Value("${simplecine.prod-url}")
	private String prodUrl;

	@Bean
	OpenAPI myOpenAPI() {
		Server devServer = new Server();
		devServer.setUrl(devUrl);
		devServer.setDescription("Server URL in Development environment");

		Server prodServer = new Server();
		prodServer.setUrl(prodUrl);
		prodServer.setDescription("Server URL in Production environment");

		Contact contact = new Contact();
		contact.setEmail("bezkoder@gmail.com");
		contact.setName("BezKoder");
		contact.setUrl("https://www.bezkoder.com");

		License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

		Info info = new Info().title("Simple Cine IMD API").version("1.0").contact(contact)
				.description("This API exposes endpoints to manage the Cine IMD application.")
				.termsOfService("https://www.bezkoder.com/terms").license(mitLicense);

		SecurityScheme jwtAuthScheme = new SecurityScheme().type(Type.HTTP).scheme("bearer").bearerFormat("JWT")
				.in(In.HEADER).name("Authorization");

		SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

		return new OpenAPI().info(info).addServersItem(devServer).addServersItem(prodServer)
				.addSecurityItem(securityRequirement)
				.components(new io.swagger.v3.oas.models.Components().addSecuritySchemes("bearerAuth", jwtAuthScheme));
	}
}
