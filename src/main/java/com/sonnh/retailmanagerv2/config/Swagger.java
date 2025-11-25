package com.sonnh.retailmanagerv2.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {
    @Bean
    public OpenAPI customOpenAPI() {
        return (new OpenAPI()).info((new Info()).title("Retail Manager").description("Developed By Sonnh").contact((new Contact()).email("nguyenhaison8999@gmail.com").name("Retail Manager")).license((new License()).name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html")).version("1.0.0")).components((new Components()).addSecuritySchemes("BEARER_JWT", (new SecurityScheme()).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").in(SecurityScheme.In.HEADER).name("Authorization"))).addSecurityItem((new SecurityRequirement()).addList("BEARER_JWT"));
    }

    @Bean
    public GroupedOpenApi adminGroup() {
        return GroupedOpenApi.builder().group("Admin API").pathsToMatch(new String[]{"/api/admin/**"}).build();
    }

    @Bean
    public GroupedOpenApi staffGroup() {
        return GroupedOpenApi.builder().group("Staff API").pathsToMatch(new String[]{"/api/staff/**"}).build();
    }

    @Bean
    public GroupedOpenApi customerGroup() {
        return GroupedOpenApi.builder().group("Customer API").pathsToMatch(new String[]{"/api/customer/**"}).build();
    }

}
