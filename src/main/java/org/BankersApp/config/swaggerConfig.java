package org.BankersApp.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.ExternalDocumentation;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;


@ApplicationPath("/")
@OpenAPIDefinition(
info = @Info(title = "Customers_Api",
description = "API for managing customer records",
version = "1.0",
contact = @Contact(name = "@mock",url = "https://mock")),
externalDocs = @ExternalDocumentation( description= "All Customer account api code mention below",url = "https://mock"),
tags= {
@Tag(name = "Customers Api", description = "Public API")
}
)
public class swaggerConfig extends Application {
}