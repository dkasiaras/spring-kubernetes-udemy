package com.kasiarakos.accountservice.adapters.storage.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
    info =
        @Info(
            title = "Account Microservice Rest api documentation",
            description = "EazyBank Accounts microservice Rest API Documentation",
            version = "v1",
            contact = @Contact(name = "Dimitris Kasiaras", email = "kasiarakos@gmail.com"),
            license = @License(name = "Apache 2.0")))
public class OpenApiConfiguration {}
